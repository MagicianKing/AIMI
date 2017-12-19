package com.yeying.aimi.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by king on 2017/10/11.
 * 圆形裁剪
 */

public class CircleTransform extends BitmapTransformation {
    private String id;
    private Context mContext;
    public CircleTransform(Context context,String id) {
        super(context);
        mContext = context;
        this.id = id;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        /*//源图片
        Bitmap source = Bitmap.createBitmap(toTransform,0,0,toTransform.getWidth(),toTransform.getHeight());
        //空白图
        Bitmap bitmap = pool.get(toTransform.getWidth(),toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        if(bitmap==null){
            bitmap = Bitmap.createBitmap(toTransform.getWidth(),toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(R.color.black));
        Canvas canvas = new Canvas(bitmap);
        BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        canvas.drawCircle(toTransform.getWidth()/2,toTransform.getHeight()/2,toTransform.getHeight()/2,paint);
        *//*Bitmap bitmap = circleCrop(pool , toTransform);*//*
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

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length, options);*/
        return circleCrop(pool , toTransform);
    }

    @Override
    public String getId() {
        return id;
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap toTransform) {
        if (toTransform == null) return null;

        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

}
