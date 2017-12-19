package com.yeying.aimi.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 图片bitmap 处理工具类性
 *
 * @author shahuaitao
 */
public class ImageBitmapUtil {


    public static Bitmap getMatrixBitmap(Bitmap btm, int gweight, int gheight) {
        Bitmap mDefauleBitma = null;
        if (btm != null) {
            int width = btm.getWidth();
            int height = btm.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) gweight) / width,
                    ((float) gheight) / height);
            mDefauleBitma = Bitmap.createBitmap(btm, 0, 0, width, height,
                    matrix, true);
        }

        return mDefauleBitma;
    }
}
