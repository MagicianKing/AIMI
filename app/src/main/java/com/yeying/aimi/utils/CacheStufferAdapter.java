package com.yeying.aimi.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.database.HBData;

import java.util.concurrent.ExecutionException;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/14 下午2:36
 * 处理文字
 */

public class CacheStufferAdapter extends BaseCacheStuffer.Proxy {
    private DanmakuView mDanmakuView;
    private Context mContext;
    private Bitmap mBitmap;

    public CacheStufferAdapter(DanmakuView danmakuView, Context context) {
        mDanmakuView = danmakuView;
        mContext = context;

    }

    //准备加载
    @Override
    public void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread) {
        if(!fromWorkerThread){
            return;
        }
        if (danmaku.tag == null) {
            return;
        }
        HBData packet = (HBData) danmaku.tag;
        try {
            mBitmap = Glide.with(mContext).load(packet.getImgUrl())
                    .asBitmap() //必须
                    .centerCrop()
                    .transform(new CornerBitmapTransformation(mContext, packet.getImgUrl()))
                    .into(50, 50)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_default);
        }
        danmaku.tag = packet;
        danmaku.padding = 0;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = true;
        danmaku.textSize = 35;
        danmaku.text = createSpannable(packet, mBitmap);
    }

    @Override
    public void releaseResource(BaseDanmaku danmaku) {
        // TODO 清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        danmaku.text = null;
        danmaku.tag = null;
    }

    private SpannableStringBuilder createSpannable(HBData packetBean, Bitmap bitmap) {
        String text = "bitmap";//图像位置
        String name = "\t" + packetBean.getUser_name() + "\t";//名字位置
        String value = packetBean.getRemark();//值
        String tx = text + name + value;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tx);
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        ForegroundColorSpan nameSpan = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan valueSpan = new ForegroundColorSpan(Color.BLUE);
        spannableStringBuilder.setSpan(imageSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(nameSpan, text.length(), text.length() + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(valueSpan, text.length() + name.length(), tx.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 将图片变成圆形
     *
     * @param bitmap
     * @return
     */
    private Bitmap makeRoundCorner(Bitmap bitmap) {
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
        canvas.drawARGB(0, 0, 0, 0);
        mPaint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, mPaint);
        return output;
    }






}


