package com.yeying.aimi.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类 Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static File createTmpFile(Context context) {

        // String state = Environment.getExternalStorageState();
        // if(state.equals(Environment.MEDIA_MOUNTED)){
        // // 已挂载
        // File pic =
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // if (!pic .exists() && !pic .isDirectory())
        // {
        // Log.i("", "111Pictures目录不存在，创建");
        // pic.mkdir();
        // }
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        // Locale.CHINA).format(new Date());
        // String fileName = "multi_image_"+timeStamp+"";
        // File tmpFile = new File(pic, fileName+".jpg");
        // return tmpFile;
        // }else{
        // File cacheDir = context.getCacheDir();
        // if (!cacheDir .exists() && !cacheDir .isDirectory())
        // {
        // Log.i("", "222Pictures目录不存在，创建");
        // cacheDir.mkdir();
        // }
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        // Locale.CHINA).format(new Date());
        // String fileName = "multi_image_"+timeStamp+"";
        // File tmpFile = new File(cacheDir, fileName+".jpg");
        // return tmpFile;
        // }

        String resource = Environment.getExternalStorageDirectory().getAbsolutePath();
//		String resource = context.getApplicationContext().getFilesDir().getAbsolutePath();
        // 创建照片存储文件夹
        File destDir = new File(resource + File.separator + "image");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                .format(new Date());
        String fileName = "multi_image_" + timeStamp + "";
        File tmpFile = new File(destDir, fileName + ".jpg");
        Log.i("", "tmpFile = " + tmpFile.getAbsolutePath());
        return tmpFile;

    }

    public static File createTmpFile(Context context, String type) {
        String resource = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 创建照片存储文件夹
        File destDir = new File(resource + File.separator + "image");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String fileName = "multi_image";
        File tmpFile = new File(destDir, fileName + type);
        Log.i("", "tmpFile = " + tmpFile.getAbsolutePath());
        return tmpFile;


    }

    public static File createTmpFile(Context context,String filePath, String type) {
        String resource = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 创建照片存储文件夹
        File destDir = new File(resource + File.separator + "image");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String fileName = filePath;
        File tmpFile = new File(destDir, fileName + type);
        Log.i("", "tmpFile = " + tmpFile.getAbsolutePath());
        return tmpFile;


    }

}
