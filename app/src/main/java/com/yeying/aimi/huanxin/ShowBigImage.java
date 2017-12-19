/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yeying.aimi.huanxin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.utils.PromptUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 下载显示大图
 *
 */
public class ShowBigImage extends BaseActivity {
    private static final String TAG = "ShowBigImage";
    public static ShowBigImage activityInstance = null;
    public boolean isLongClock;
    private ProgressDialog pd;
    private PhotoView image;
    private int default_res = R.drawable.default_icon;
    private String localFilePath;
    private Bitmap bitmap;
    private boolean isDownloaded;
    private ProgressBar loadLocalPb;
    private boolean isFromMyPicdetail;
    private RelativeLayout rl_zhezhao;
    private PopupWindow menuWindow;
    private String imgpath;
    private Uri uri;
    private int position;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);
        activityInstance = this;
        image = (PhotoView) findViewById(R.id.image);
        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
        default_res = getIntent().getIntExtra("default_image", R.drawable.zhanwei2);
        uri = getIntent().getParcelableExtra("uri");
        imgpath = getIntent().getExtras().getString("imgpath");
        String remotepath = getIntent().getExtras().getString("remotepath");
        String secret = getIntent().getExtras().getString("secret");
        EMLog.d(TAG, "show big image uri:" + uri + " remotepath:" + remotepath);
        isFromMyPicdetail = getIntent().getBooleanExtra("isFromMyPicdetail", false);
        position = getIntent().getIntExtra("position", -1);

        //本地存在，直接显示本地的图片
        if ((uri != null && new File(uri.getPath()).exists()) || isFromMyPicdetail) {
            EMLog.d(TAG, "showbigimage file exists. directly show it");
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // int screenWidth = metrics.widthPixels;
            // int screenHeight =metrics.heightPixels;
            if (isFromMyPicdetail) {
                Log.i("imagepath", imgpath);
                bitmap = decodeFile(imgpath);
            } else {
                bitmap = ImageCache.getInstance().get(uri.getPath());
            }
            if (bitmap == null) {
                LoadLocalBigImgTask task = new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
                        ImageUtils.SCALE_IMAGE_HEIGHT);
                if (android.os.Build.VERSION.SDK_INT > 19) {
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    task.execute();
                }
            } else {
                image.setImageBitmap(bitmap);
            }
        } else if (remotepath != null) { //去服务器下载图片
            EMLog.d(TAG, "download remote image");
            Map<String, String> maps = new HashMap<String, String>();
            if (!TextUtils.isEmpty(secret)) {
                maps.put("share-secret", secret);
            }
            downloadImage(remotepath, maps);
        } else {
            image.setImageResource(default_res);
        }
        image.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                isLongClock = true;
                Log.i("", "onLongClick");
                getPopupWindow();
                menuWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                return true;
            }
        });


//		image.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.i("", "image onclick");
//				finish();
//			}
//		});
    }

    private void getPopupWindow() {
        showPopupWindow();

        if (null != menuWindow) {
            menuWindow.dismiss();
            return;
        } else {
        }
    }

    public void showPopupWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupView = layoutInflater
                .inflate(R.layout.menu_image_window, null);
        menuWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
//		menuWindow.setAnimationStyle(R.style.AnimationPreview);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                isLongClock = false;
            }
        });
        popupView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menuWindow.dismiss();
            }
        });

        Button bt_quxiao = (Button) popupView.findViewById(R.id.bt_quxiao);
        bt_quxiao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menuWindow.dismiss();
            }
        });
        Button bt_save = (Button) popupView.findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(uri.getPath()) && new File(uri.getPath()).exists()) {
                    String resource = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File destDir = new File(resource + File.separator + "saveimage");
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                            .format(new Date());
                    String fileName = "multi_image_" + timeStamp + ".jpg";

                    String newPath = destDir + File.separator + fileName;
                    copyFile(uri.getPath(), newPath);
                    PromptUtils.showToast(ShowBigImage.this, "图片保存在" + destDir + "目录下");
                } else {
                    PromptUtils.showToast(ShowBigImage.this, "图片不存在");
                }

                menuWindow.dismiss();
            }
        });

        Button bt_forward = (Button) popupView.findViewById(R.id.bt_forward);
        bt_forward.setVisibility(View.GONE);
        bt_forward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
                setResult(API.RESULT_CODE_FORWARD, new Intent().putExtra("position", position));
                finish();
            }
        });


    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
//	                   System.out.println(bytesum); 
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        } finally {
        }

    }

    /**
     * 通过远程URL，确定下本地下载后的localurl
     * @param remoteUrl
     * @return
     */
    public String getLocalFilePath(String remoteUrl) {
        String localPath;
        if (remoteUrl.contains("/")) {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
                    + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
        } else {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
        }
        return localPath;
    }

    /**
     * 下载图片
     *
     * @param remoteFilePath
     */
    private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
        String str1 = getResources().getString(R.string.Download_the_pictures);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(str1);
        pd.show();
        localFilePath = getLocalFilePath(remoteFilePath);
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int screenWidth = metrics.widthPixels;
                        int screenHeight = metrics.heightPixels;

                        bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
                        if (bitmap == null) {
                            image.setImageResource(default_res);
                        } else {
                            image.setImageBitmap(bitmap);
                            ImageCache.getInstance().put(localFilePath, bitmap);
                            isDownloaded = true;
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
            }

            public void onError(int error, String msg) {
                EMLog.e(TAG, "offline file transfer error:" + msg);
                File file = new File(localFilePath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        image.setImageResource(default_res);
                    }
                });
            }

            public void onProgress(final int progress, String status) {
                EMLog.d(TAG, "Progress: " + progress);
                final String str2 = getResources().getString(R.string.Download_the_pictures_new);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        pd.setMessage(str2 + progress + "%");
                    }
                });
            }
        };

        EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath, headers, callback);

    }

    @Override
    public void onBackPressed() {
        if (isDownloaded)
            setResult(RESULT_OK);
        finish();
    }

    //图片uri转变为路径
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public Bitmap decodeFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inSampleSize = 2;
        options.inPreferredConfig = Config.RGB_565;
        try {
            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(
                    options, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        options.inJustDecodeBounds = false;
        if (filePath != null) {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
}
