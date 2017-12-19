package com.yeying.aimi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/11 上午11:36
 */

public class CircleImageView extends ImageView {
    //初始化画笔
    private Paint paint;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    //画圆形图片
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            //获得运算过后的图像
            Bitmap b = getCilrcleBitmap(bitmap);
            //绘制区域
            Rect rect = new Rect(0, 0, b.getWidth(), b.getHeight());
            //绘制大小
            Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            //绘制一张图片在 rect 这么大的区域 绘制图片面积为 rectDest 这么大
            canvas.drawBitmap(b, rect, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    //获取圆形图片
    private Bitmap getCilrcleBitmap(Bitmap bitmap) {//源图像
        int min = Math.min(bitmap.getWidth(),bitmap.getHeight());
        //新建一个画布
        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        //再此画布上做图像运算
        Canvas canvas = new Canvas(output);

        //打开抗锯齿
        paint.setAntiAlias(true);

        //绘制目标图像
        canvas.drawCircle(min / 2,min / 2,min / 2, paint);
        //设置图像混合模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制区域
        Rect rect = new Rect(0, 0, min, min);
        //绘制 bitmap
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //回收画笔
        paint.reset();

        return output;
    }
}
