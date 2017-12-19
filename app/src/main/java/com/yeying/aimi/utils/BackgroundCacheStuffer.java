package com.yeying.aimi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.yeying.aimi.R;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/14 下午2:30
 * 绘制背景
 */

public class BackgroundCacheStuffer extends SpannedCacheStuffer {
    private Paint mPaint;
    private int TEXT_SIZE = 50; // 文字大小
    private Context mContext;
    public BackgroundCacheStuffer(Context context) {
        mContext = context;
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStrokeJoin(Paint.Join.ROUND);//圆角
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setTextSize(TEXT_SIZE);
        }
    }

    /**
     * 将图片变成圆形
     *
     * @param bitmap
     * @return
     */
    private  Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint mPaint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        canvas.drawBitmap(bitmap, rect, rect, null);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRoundRect(rectF, roundPx, roundPx, mPaint);
        return output;
    }

    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        danmaku.padding = 20;
        super.measure(danmaku, paint, true);
    }

    @Override
    protected void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
        mPaint.setColor(mContext.getResources().getColor(R.color.the_black));
        canvas.drawArc(new RectF(left,top,danmaku.paintHeight,danmaku.paintHeight),90,180,true,mPaint);
        canvas.drawRect(left + danmaku.paintHeight/2, top, left + danmaku.paintWidth-danmaku.paintHeight/2, top + danmaku.paintHeight, mPaint);
        canvas.drawArc(new RectF(danmaku.paintWidth-danmaku.paintHeight,top,danmaku.paintWidth,danmaku.paintHeight),90,-180,true,mPaint);
    }
}
