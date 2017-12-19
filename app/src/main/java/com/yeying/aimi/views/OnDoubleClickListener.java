package com.yeying.aimi.views;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tanchengkeji on 2017/8/30.
 */

public class OnDoubleClickListener implements View.OnTouchListener{

    private int count = 0;
    private long firstClick = 0;
    private long secondClick = 0;

    private final int duration = 1000;

    private DoubleClickCallback mDoubleClickCallback;

    public interface DoubleClickCallback{
        void onDoubleClick();
    }

    public OnDoubleClickListener(DoubleClickCallback doubleClickCallback) {
        mDoubleClickCallback = doubleClickCallback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            count ++;
            if (count == 1){
                firstClick = System.currentTimeMillis();
            }else if (count == 2){
                secondClick = System.currentTimeMillis();
                if (secondClick - firstClick<duration){
                    if (mDoubleClickCallback!=null){
                        mDoubleClickCallback.onDoubleClick();
                    }
                    count = 0;
                    firstClick = 0;
                    return true;
                }else {
                    firstClick = secondClick;
                    count = 1;
                }
                secondClick = 0;
            }
        }

        return false;
    }
}
