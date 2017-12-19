package com.yeying.aimi.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtil {

    public static boolean makeDirExist(String strPicCachePath) {
        // TODO Auto-generated method stub
        return true;
    }

    public static void deleteFileWithoutCheckReturnValue(File files) {
        // TODO Auto-generated method stub

    }

    public static void deleteFileWithoutCheckReturnValue(String strBmpFile) {
        // TODO Auto-generated method stub

    }

    /**
     * 获取裁剪后头像的缓存目录
     *
     * @return
     */
    public static File getHeaderCacheDir(Context ctx) {
        File cacheDir;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {// 如果SD卡存在
            // 获取SD卡路径
            cacheDir = Environment.getExternalStorageDirectory();
        } else {
            // 获取系统SD卡路径
            cacheDir = ctx.getCacheDir();
        }

        return cacheDir;
    }

    public static File getHeadimageCache(Context ctx) {
        File cacheDir;
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {// 如果SD卡存在
            // 获取SD卡路径
            cacheDir = Environment.getExternalStorageDirectory();
        } else {
            // 获取系统SD卡路径
            cacheDir = ctx.getCacheDir();
        }

        return cacheDir;
    }

}
