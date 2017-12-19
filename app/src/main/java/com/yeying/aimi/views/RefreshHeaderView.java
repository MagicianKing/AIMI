package com.yeying.aimi.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.yeying.aimi.R;

/**
 * Created by tanchengkeji on 2017/8/11.
 */

public class RefreshHeaderView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private LayoutInflater inflater;
    private AnimationDrawable animation;
    private ImageView mImg_loading;

    public RefreshHeaderView(Context context) {
        this(context,null,0);
    }

    public RefreshHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater=LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view=inflater.inflate(R.layout.loading_anim_layout,null,false);
        addView(view,lp);
        mImg_loading= (ImageView) view.findViewById(R.id.mImg_loading);
        animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.kspleft),200);
        animation.addFrame(getResources().getDrawable(R.drawable.kspmiddle),200);
        animation.addFrame(getResources().getDrawable(R.drawable.kspright),200);
        animation.addFrame(getResources().getDrawable(R.drawable.kspmiddle),200);
        animation.setOneShot(false);
        mImg_loading.setBackgroundDrawable(animation);
    }

    @Override
    public void onRefresh() {
        if (!animation.isRunning()){
            animation.start();
        }
    }

    @Override
    public void onPrepare() {
        //setText("");
        if (!animation.isRunning()){
            animation.start();
        }
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                //setText("");
            } else {
                //setText("");
            }
        } else {
            //setText("");
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        if (animation.isRunning()){
            animation.stop();
        }
    }

    @Override
    public void onReset() {
        //setText("");
    }
}
