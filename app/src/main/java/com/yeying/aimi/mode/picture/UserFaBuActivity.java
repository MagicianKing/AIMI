package com.yeying.aimi.mode.picture;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.protoco.FormFile;
import com.yeying.aimi.protoco.SocketBean;
import com.yeying.aimi.protoco.SocketHttpRequester;
import com.yeying.aimi.protocol.impl.P10306;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.FileUtils;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.Utils;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.RoundImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanchengkeji on 2017/9/26.
 */

public class UserFaBuActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    private RoundImageView mImgFabu;
    private EditText mContentFabu;
    private Uri mUri;
    private SessionCache session;
    private Bitmap bitmap;

    private int type = 0;//发布的类型，默认是图片
    private static int TYPE_PIC = 0; //图片
    private static int TYPE_VIDEO = 1; //视频
    private static int RESULT_LOAD_IMAGE = 3;
    private static int RESULT_LOAD_VIDEO = 4;
    private static int RESULT_CROP_IMAGE = 5;
    private File mTmpFile;
    private String filePath = null;

    private FinishBroadCast mFinishBroadCast;
    private IntentFilter filter;

    public static final String FABU_FLAG = "fabu_flag";
    private PopupWindow mDialog;

    private HandlerPlus mHandlerPlus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        session = SessionCache.getInstance(this);
        mHandlerPlus = new HandlerPlus(this);
        initIntent();
        initView();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        /*mFinishBroadCast = new FinishBroadCast();
        filter = new IntentFilter("com.haibar.finish");
        registerReceiver(mFinishBroadCast,filter);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mFinishBroadCast);
    }

    private void initData() {
        Glide.with(this).load(filePath).into(mImgFabu);
        bitmap = BitmapFactory.decodeFile(filePath);
        bitmap.getHeight();
    }

    private void initIntent() {
        filePath = getIntent().getStringExtra("filePath");
    }

    private void initView() {

        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_right_tv.setText("发布");
        title_right_tv.setBackgroundResource(R.drawable.round_yellow_bg);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setOnClickListener(this);
        title_left.setOnClickListener(this);
        title_center_view.setText("发布");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);

        mDialog = PromptUtils.getProgressDialogPop(this);
        mTmpFile = FileUtils.createTmpFile(mActivity, null);
        mImgFabu = (RoundImageView) findViewById(R.id.fabu_img);
        mContentFabu = (EditText) findViewById(R.id.fabu_content);
    }

    public static void toUserFaBu(Context context , String filePath){
        Intent intent = new Intent(context,UserFaBuActivity.class);
        intent.putExtra("filePath",filePath);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right_tv:
               mDialog.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        //文件上传
                        uploadFile();

                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
        }
    }

    //上传图片源代码
    public void uploadFile() {
        try {
            Map<String, String> params = new HashMap<String, String>();
            P10306 p = new P10306();
            p.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
            p.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
            p.req.userId = session.userId;
            p.req.sessionId = session.sessionId;
            p.req.type = String.valueOf(0);
            p.req.imgBase = "";
            p.req.imgHeight = bitmap.getHeight();
            p.req.imgWidth = bitmap.getWidth();
            String content = mContentFabu.getText().toString();
            if (content == null){
                content = "";
                p.req.message = content;
            }else {
                p.req.message =content;
            }

            params.put("content", JSON.toJSONString(p.req));
            params.put("transcode", p.req.transcode);
            params.put("sessionId", session.sessionId);
            Log.e(TAG, "uploadFile: "+JSON.toJSONString(p.req));
            File mTmpFile = FileUtils.createTmpFile(this, ".jpg");
            Utils.compressBmpToFile(bitmap, mTmpFile);
            FormFile file = new FormFile(mTmpFile.getName(), Utils.getBytesFromFile(mTmpFile), "imgBase", null);
            SocketBean bean = SocketHttpRequester.post("", params, file);
            if (bean.isSuccess) {
                Message msg = mHandlerPlus.obtainMessage();
                msg.what = 1;
                msg.obj = bean;
                mHandlerPlus.sendMessage(msg);
            } else {
                mHandlerPlus.sendEmptyMessage(0);
            }

        } catch (Exception e) {
            mHandlerPlus.sendEmptyMessage(0);
        }
    }

    private class HandlerPlus extends WeakHandler{

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserFaBuActivity activity = (UserFaBuActivity) getObjct();
            if (activity != null){
                switch (msg.what){
                    case 0:
                        if (mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                        PromptUtils.showToast(UserFaBuActivity.this , "发布失败，请重试...");
                        break;
                    case 1:
                        SocketBean bean = (SocketBean) msg.obj;
                        P10306.Resp resp = JSON.parseObject(bean.content, P10306.Resp.class);
                        SharedPreferences sharedPreferences=UserFaBuActivity.this.getSharedPreferences("FaBuBean",MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean("is_new",true);
                        edit.putString("bean_Isrc", AIMIApplication.dealHeadImg(resp.image_url));
                        edit.putInt("bean_height",bitmap.getHeight());
                        edit.putInt("bean_width",bitmap.getWidth());
                        edit.putString("bean_msg_id",resp.message_id);
                        edit.putString("bean_msg",mContentFabu.getText().toString());
                        edit.putString("bean_distance","0.0");
                        edit.putString("bean_barName",session.barName);
                        edit.apply();
                        Intent intent = new Intent(UserFaBuActivity.this,HomeActivity.class);
                        intent.putExtra(FABU_FLAG,true);
                        startActivity(intent);
                        break;
                }
            }
        }
    }

}
