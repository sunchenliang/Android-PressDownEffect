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

    /**
     * 点击按钮时的收缩动画
     */
    private ScaleAnimation scaleInAnimation;
    /**
     * 松开按钮时的回放动画
     */
    private ScaleAnimation scaleOutAnimation;

    /**
     * onClick事件
     */
    private View.OnClickListener onClickListener;
    /**
     * onTouch事件
     */
    private View.OnTouchListener onTouchListener;

    /**
     * 记录下touch事件类型
     */
    private int eventAction;
    /**
     * 需要添加动画效果的对象引用
     */
    private View view;

    /**
     * 正常状态的收缩比例
     */
    private final static float NORMAL_SCALE_FACTOR = 1f;

    /**
     * 点击状态的收缩比例
     */
    private final static float PRESSED_SCALE_FACTOR = 0.95f;
    /**
     * 收缩锚点X值
     */
    private final static float PIVOT_X_VALUE = 0.5f;
    /**
     * 收缩锚点Y值
     */
    private final static float PIVOT_Y_VALUE = 0.5f;

    /**
     * 缩放动画播放时间
     */
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
            public void onAnimationEnd(Animation animation) {//等动画播放完了在触发click事件。
                if (eventAction == MotionEvent.ACTION_UP && onClickListener != null) {//判断当此时的touch事件是ACTION_UP且设置了onClickListener事件时，触发click时间
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

    /**
     * 重载onTouch时间来播放动画
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        eventAction = event.getAction();
        view.setOnClickListener(new View.OnClickListener() { //TODO 如果不设置点击事件，onTouch中，
            // 除ACTION_DOWN事件外，其余事件不能触发，
            // 目前还没有研究是什么原因。
            @Override
            public void onClick(View v) {
            }
        });
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
