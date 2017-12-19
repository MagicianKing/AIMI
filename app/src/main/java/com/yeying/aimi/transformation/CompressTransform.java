package com.yeying.aimi.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by tanchengkeji on 2017/10/20.
 */

public class CompressTransform extends BitmapTransformation {

    private String urlId;


    public CompressTransform(Context context , String urlId) {
        super(context);
        this.urlId = urlId;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        Bitmap source = Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight());
        Bitmap bitmap = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
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
        if (bitmap.getByteCount()/(1024*1024)>8){
            options.inSampleSize = 8;
        } else if (bitmap.getByteCount()/(1024*1024)>5){
            options.inSampleSize = 6;
        } else if (bitmap.getByteCount()/(1024*1024)>3){
            options.inSampleSize = 5;
        }else if (bitmap.getByteCount()/(1024*1024)>1){
            options.inSampleSize = 3;
        }else {
            options.inSampleSize = 2;
        }

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length, options);

        bitmap.recycle();

        return bitmap1;
    }

    @Override
    public String getId() {
        return urlId;
    }
}
