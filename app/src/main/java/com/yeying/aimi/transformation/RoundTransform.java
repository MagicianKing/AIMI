package com.yeying.aimi.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by king on 2017/10/11.
 * 圆角裁剪
 */

public class RoundTransform extends BitmapTransformation {
    private Context mContext;
    private String id;
    public RoundTransform(Context context, String id) {
        super(context);
        mContext = context;
        this.id = id;
    }

    public RoundTransform(BitmapPool bitmapPool, String id) {
        super(bitmapPool);
        this.id = id;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCropP(pool , toTransform);
    }

    @Override
    public String getId() {
        return id;
    }

    private Bitmap roundCropP(BitmapPool pool, Bitmap toTransform) {
        if (toTransform == null) return null;

        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight());
        if (toTransform.getWidth()+toTransform.getHeight()>2000){
            canvas.drawRoundRect(rectF, 50, 50, paint);
        }else if (toTransform.getWidth()+toTransform.getHeight()>1000){
            canvas.drawRoundRect(rectF, 30, 30, paint);
        } else {
            canvas.drawRoundRect(rectF, 10, 10, paint);
        }
        return result;
    }

}
