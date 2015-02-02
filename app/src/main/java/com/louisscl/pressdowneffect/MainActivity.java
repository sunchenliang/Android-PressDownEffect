package com.louisscl.pressdowneffect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.louisscl.pressdowneffectlib.PressDownTouchListener;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textview = (TextView) findViewById(R.id.hello_world);
        PressDownTouchListener pressDownTouchListener = new PressDownTouchListener(textview);
        pressDownTouchListener.setOnClickListener(new View.OnClickListener() {//此处定义点击事件
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "hello world clicked!",Toast.LENGTH_SHORT).show();
            }
        });
        pressDownTouchListener.setOnTouchListener(new View.OnTouchListener() {//此处定义touch事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("touch event", "" + event);
                return false;
            }
        });
        textview.setOnTouchListener(pressDownTouchListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
