package com.yeying.aimi.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ZHENGJ on 2016/6/2.
 */
public class AutoMeasureImageView extends ImageView {
    public AutoMeasureImageView(Context context) {
        super(context);
    }

    public AutoMeasureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoMeasureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable drawable = getDrawable();
        if (drawable != null) {
            //重新定义测量规格
            int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
            int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

//            Logger.d(":originalWidth:"+originalWidth+",originalHeight:"+originalHeight);

            int minimumWidth = drawable.getMinimumWidth();
            int minimumHeight = drawable.getMinimumHeight();
            float scale = (float) minimumWidth/minimumHeight;
//            Logger.d(":minimumWidth:"+minimumWidth+",minimumHeight:"+minimumHeight+",scale:"+scale);

//            int measuredWidth = (int) (originalWidth/scale);
            int measuredHeight = (int) (originalWidth / scale);
//            Logger.d("measuredHeight:"+measuredHeight+",measuredWidth:");

           //重新赋值测量规格
           heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
//            widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);





    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
