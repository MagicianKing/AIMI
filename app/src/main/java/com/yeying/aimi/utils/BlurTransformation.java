package com.yeying.aimi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/22 下午4:43
 * 图片模糊
 */

public class BlurTransformation extends BitmapTransformation {
    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;
    private Context context;
    private   String key;
    public BlurTransformation(Context context,String key) {
        super(context);
        this.key = key;
        this.context = context;

    }



    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        Bitmap bitmap = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);

        if(bitmap==null){
            bitmap = Bitmap.createBitmap(toTransform.getWidth(),toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) DEFAULT_DOWN_SAMPLING, 1 / (float) DEFAULT_DOWN_SAMPLING);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);
        //canvas.drawBitmap(bitmap,new Rect(0,0,outWidth,outHeight),new RectF(0,0,width,outHeight),paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(context, bitmap, MAX_RADIUS);
            } catch (RSRuntimeException e) {
                bitmap = FastBlur.blur(bitmap, MAX_RADIUS, true);
            }
        } else {
            bitmap = FastBlur.blur(bitmap, MAX_RADIUS, true);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 70 , byteArrayOutputStream);
        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        if (bitmap.getByteCount()/(1024*1024)>3){
            options.inSampleSize = 5;
        }else if (bitmap.getByteCount()/(1024*1024)>1){
            options.inSampleSize = 3;
        }else {
            options.inSampleSize = 2;
        }

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length, options);



        return bitmap1;
    }

    @Override
   public String getId() {
        return key;
    }


}
