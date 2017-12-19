package com.yeying.aimi.views;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tanchengkeji on 2017/7/12.
 */


public class TextViewPlus_Rel extends TextView{
    private Context context;
    public TextViewPlus_Rel(Context context) {
        this(context,null);
    }

    public TextViewPlus_Rel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewPlus_Rel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString()))
                    + getCompoundPaddingTop() + getCompoundPaddingBottom()+10;
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineHeight(String str) {
        float screenW = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        //float parentWidth=((RelativeLayout)this.getParent()).getMeasuredWidth();
        float paddingLeft = ((RelativeLayout)this.getParent()).getPaddingLeft();
        float paddingReft = ((RelativeLayout)this.getParent()).getPaddingRight();
        //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
        int line = (int) Math.ceil( (this.getPaint().measureText(str)/(screenW-paddingLeft-paddingReft)));

        float height = (this.getPaint().getFontMetrics().descent-this.getPaint().getFontMetrics().ascent)*line;
        return height;
    }

}
