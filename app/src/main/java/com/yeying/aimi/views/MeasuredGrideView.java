package com.yeying.aimi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by tanchengkeji on 2017/11/1.
 */

public class MeasuredGrideView extends GridView {
    public MeasuredGrideView(Context context) {
        super(context);
    }

    public MeasuredGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasuredGrideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2 , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec , expandSpec);
    }
}
