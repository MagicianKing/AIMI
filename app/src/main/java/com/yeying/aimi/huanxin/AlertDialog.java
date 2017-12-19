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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.yeying.aimi.R;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.mode.signlechat.SingleChat;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10208;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AlertDialog extends Activity {
    public static int reSendMsg = 1;
    public static int uploadPic = 2;
    private TextView mTextView;
    private Button mButton;
    private int position;
    private ImageView imageView;
    private EditText editText;
    private boolean isEditextShow;
    private int method;
    private String myGroupId;
    private Button bt_todo;
    private String imagePath;
    private Bitmap bitmap;
    private String localFilePath;
    private String secret;
    private String localUrl;
    private SessionCache session;
    private PopupWindow dialog;
    private AlertDialog mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);
        mActivity = this;
        session = SessionCache.getInstance(mActivity);
        method = getIntent().getIntExtra("method", 0);
        myGroupId = getIntent().getStringExtra("myGroupId");
        imagePath = getIntent().getStringExtra("imagePath");
        secret = getIntent().getStringExtra("secret");
        localUrl = getIntent().getStringExtra("localUrl");

        bt_todo = (Button) findViewById(R.id.bt_todo);
        if (method == this.uploadPic) {
            bt_todo.setText("上传");
        }
        mTextView = (TextView) findViewById(R.id.title);
        mButton = (Button) findViewById(R.id.btn_cancel);
        imageView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edit);
        //提示内容
        String msg = getIntent().getStringExtra("msg");
        //提示标题
        String title = getIntent().getStringExtra("title");
        position = getIntent().getIntExtra("position", -1);
        //是否显示取消标题
        boolean isCanceTitle = getIntent().getBooleanExtra("titleIsCancel", false);
        //是否显示取消按钮
        boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
        //是否显示文本编辑框
        isEditextShow = getIntent().getBooleanExtra("editTextShow", false);
        //转发复制的图片的path
        String path = getIntent().getStringExtra("forwardImage");
        //
        String edit_text = getIntent().getStringExtra("edit_text");

        if (msg != null)
            ((TextView) findViewById(R.id.alert_message)).setText(msg);
        if (title != null)
            mTextView.setText(title);
        if (isCanceTitle) {
            mTextView.setVisibility(View.GONE);
        }
        if (isCanceShow)
            mButton.setVisibility(View.VISIBLE);
        if (path != null) {
            //优先拿大图，没有去取缩略图
            if (!new File(path).exists())
                path = DownloadImageTask.getThumbnailImagePath(path);
            imageView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.alert_message)).setVisibility(View.GONE);
            if (ImageCache.getInstance().get(path) != null) {
                imageView.setImageBitmap(ImageCache.getInstance().get(path));
            } else {
                Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
                imageView.setImageBitmap(bm);
                ImageCache.getInstance().put(path, bm);
            }

        }
        if (isEditextShow) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(edit_text);
        }
    }

    public void ok(View view) {
        if (method == this.reSendMsg) {
            //点击确定重发消息
            setResult(RESULT_OK, new Intent().putExtra("position", position).
                            putExtra("edittext", editText.getText().toString())
                    /*.putExtra("voicePath", voicePath)*/);
            if (position != -1){
                Activity_BaPing.resendPos = position;
                SingleChat.resendPos = position;
            }
            finish();
        } else if (method == this.uploadPic) {
            //点击确定上传图片到群相册
            Log.i("", "点击确定上传图片到群相册");
            Log.i("", "myGroupId=" + myGroupId);
            Log.i("", "imagePath=" + imagePath);
            Log.i("", "localUrl=" + localUrl);
            //显示loading框
            dialog = PromptUtils.getProgressDialogPop(mActivity);
            dialog.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

            if (localUrl != null && !"".equals(localUrl)) {
                bitmap = decodeFile(localUrl);
            }
            if (bitmap != null) {
                Log.i("", "本地图片的bitmap=" + bitmap);
                //如果有本地图片的bitmap就调用群组上传图片接口

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                String dataStr = Base64.encodeToString(bytes, Base64.DEFAULT);

                P10208 p = new P10208();
                p.req.groupId = myGroupId;
                p.req.userId = session.userId;
                p.req.sessionId = session.sessionId;
                p.req.imgCode = dataStr;
                p.req.type = "3";
                new UpdatePicTask().execute(mActivity, p);
            } else {
                //显示loading框
                dialog = PromptUtils.getProgressDialogPop(mActivity);
                dialog.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

                //去网络上下载图片再调用群组上传图片接口
                Map<String, String> maps = new HashMap<String, String>();
                if (!TextUtils.isEmpty(secret)) {
                    maps.put("share-secret", secret);
                }
                downloadImage(imagePath, maps);
            }
        }


    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }


    private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
        String str1 = getResources().getString(R.string.Download_the_pictures);
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
                        if (bitmap != null) {
                            Log.i("", "准备上传的图片的bitmap=" + bitmap);


                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes = baos.toByteArray();
                            String dataStr = Base64.encodeToString(bytes, Base64.DEFAULT);

                            P10208 p = new P10208();
                            p.req.groupId = myGroupId;
                            p.req.userId = session.userId;
                            p.req.sessionId = session.sessionId;
                            p.req.imgCode = dataStr;
                            p.req.type = "3";
                            new UpdatePicTask().execute(mActivity, p);

                        } else {
                            //隐藏loading框
                            dialog.dismiss();

                            Toast.makeText(mActivity, "获取图片资源失败", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }

                    }
                });
            }

            public void onError(int error, String msg) {
                EMLog.e("", "offline file transfer error:" + msg);
                File file = new File(localFilePath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //隐藏loading框
                        dialog.dismiss();
                    }
                });
            }

            public void onProgress(final int progress, String status) {
                EMLog.d("", "Progress: " + progress);
                final String str2 = getResources().getString(R.string.Download_the_pictures_new);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };

        EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath, headers, callback);
    }

    /**
     * 通过远程URL，确定下本地下载后的localurl
     *
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

    public Bitmap decodeFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inSampleSize = 4;//代表容量变为以前容量的1/4
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

    //调用上传群图片接口
    public class UpdatePicTask extends DefaultTask {


        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10208 p = (P10208) protocol;
            if (p.resp.transcode.equals("9999")) {
                Toast.makeText(getApplicationContext(), "系统错误", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
            dialog.dismiss();
        }
    }

}
