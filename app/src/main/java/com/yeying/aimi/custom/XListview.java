package com.yeying.aimi.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/19 上午9:58
 */

public class XListview extends ListView {
    public XListview(Context context) {
        super(context);
    }

    public XListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
