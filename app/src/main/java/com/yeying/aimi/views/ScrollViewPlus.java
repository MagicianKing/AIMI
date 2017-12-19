package com.yeying.aimi.views;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.utils.WeakHandler;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class ScrollViewPlus extends NestedScrollView {

    private int height;
    private ChangeTitle mChangeTitle;
    private HandlerPlus mHandlerPlus;
    private boolean flag = true;
    private boolean flag1 = true;
    private Context mContext;

    private static final String TAG = "ScrollViewPlus";
    public ScrollViewPlus(Context context) {
        this(context,null);
    }

    public ScrollViewPlus(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollViewPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mHandlerPlus = new HandlerPlus(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: "+getScrollY());
        if (getScrollY()>height-200){
            if (flag){
                mChangeTitle.changeTitle();
                flag = false;
            }
        }else{
            if (!flag){
                mChangeTitle.resetTitle();
                flag = true;
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP){
            mHandlerPlus.sendEmptyMessageDelayed(0,200);
        }
        return super.dispatchTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
            mHandlerPlus.sendEmptyMessageDelayed(0,200);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
    }

    public void setLimitHeight(int height){
        this.height = height;
    }

    public interface ChangeTitle{
        void changeTitle();
        void resetTitle();
    }

    public void setChangeTitleListener(ChangeTitle changeTitleListener){
        mChangeTitle = changeTitleListener;
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private class HandlerPlus extends WeakHandler{

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity activity = null;
            if (mContext instanceof HomeActivity){
                activity = (HomeActivity) getObjct();
            }else if (mContext instanceof OtherHomepage){
                activity = (OtherHomepage) getObjct();
            }

            if (activity != null){
                if (msg.what == 0){
                    Log.e(TAG, "handleMessage: --->接收延时消息");
                    if (getScrollY()>height-200){
                        if (flag){
                            mChangeTitle.changeTitle();
                            flag = false;
                        }
                    }else{
                        if (!flag){
                            mChangeTitle.resetTitle();
                            flag = true;
                        }
                    }
                }
            }
        }
    }

}
