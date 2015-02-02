package com.louisscl.pressdowneffectlib;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by louisscl on 15/2/2.
 */
public class PressDownTouchListener implements View.OnTouchListener {

    private ScaleAnimation scaleInAnimation;
    private ScaleAnimation scaleOutAnimation;

    private View.OnClickListener onClickListener;

    private View.OnTouchListener onTouchListener;

    private int eventAction;
    private View view;

    private final static float NORMAL_SCALE_FACTOR = 1f,
            PRESSED_SCALE_FACTOR = 0.95f,
            PIVOT_X_VALUE = 0.5f,
            PIVOT_Y_VALUE = 0.5f;

    private final static int SCALE_ANIMATION_DURATION_IN_MILLISECOND = 300;


    /**
     * @param view 需要实现动画效果的对象
     */
    public PressDownTouchListener(final View view) {
        if (view == null) {
            throw new RuntimeException("view can not be null");
        }
        this.view = view;
        scaleInAnimation = new ScaleAnimation(NORMAL_SCALE_FACTOR, PRESSED_SCALE_FACTOR,
                NORMAL_SCALE_FACTOR, PRESSED_SCALE_FACTOR, Animation.RELATIVE_TO_SELF,
                PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE);

        scaleInAnimation.setDuration(SCALE_ANIMATION_DURATION_IN_MILLISECOND);
        scaleInAnimation.setFillAfter(true);
        scaleInAnimation.setInterpolator(new DecelerateInterpolator());

        scaleOutAnimation = new ScaleAnimation(PRESSED_SCALE_FACTOR, NORMAL_SCALE_FACTOR,
                PRESSED_SCALE_FACTOR, NORMAL_SCALE_FACTOR, Animation.RELATIVE_TO_SELF,
                PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE);
        scaleOutAnimation.setFillAfter(true);
        scaleOutAnimation.setDuration(SCALE_ANIMATION_DURATION_IN_MILLISECOND);
        scaleOutAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (eventAction == MotionEvent.ACTION_UP && onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        });
    }

    /**
     * 设置点击事件。View的点击事件在这里设置，不要在View.setOnClickListener中设置，防止在动画执行过程中被调用，
     * 导致UI线程堵塞，动画不流畅
     *
     * @param onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 动画是通过重载onTouch事件实现的，如果需要添加自己的onTouch事件，可以在这里设置
     *
     * @param onTouchListener
     */
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        eventAction = event.getAction();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Log.d("onTouch", "event = " + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.startAnimation(scaleInAnimation);
                break;
            case MotionEvent.ACTION_UP:
                view.startAnimation(scaleOutAnimation);
                break;
            case MotionEvent.ACTION_CANCEL:
                view.startAnimation(scaleOutAnimation);
                break;
        }
        if (onTouchListener == null) {
            return false;
        } else {
            return onTouchListener.onTouch(v, event);
        }
    }
}
