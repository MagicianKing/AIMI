package com.yeying.aimi.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.yeying.aimi.R;

/**
 * Created by tanchengkeji on 2017/8/11.
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
        setText("上拉加载更多...");
        setTextColor(Color.WHITE);
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText("上拉加载更多...");
                setTextColor(Color.WHITE);
            } else {
                setText("上拉加载更多...");
                setTextColor(Color.WHITE);
            }
        } else {
            setText("上拉加载更多...");
            setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onRelease() {
        setText("上拉加载更多...");
        setTextColor(Color.WHITE);
    }

    @Override
    public void onComplete() {
        setText("上拉加载更多...");
        setTextColor(Color.WHITE);
    }

    @Override
    public void onReset() {
        setText("");
    }
}

