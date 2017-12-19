package com.yeying.aimi.mode.person;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.photopicker.AlbmActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.FormFile;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protoco.SocketBean;
import com.yeying.aimi.protoco.SocketHttpRequester;
import com.yeying.aimi.protocol.impl.P10103;
import com.yeying.aimi.protocol.impl.P620002;
import com.yeying.aimi.protocol.impl.P620005;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.FileUtils;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.Utils;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.RoundImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PersonActivity extends BaseActivity implements View.OnClickListener ,DatePickerDialog.OnDateSetListener{

    private ImageView mImgLeftBack;
    private TextView mTvMiddleTitle;
    private RoundImageView mImgHead;
    private RelativeLayout mLayoutHead;
    private TextView mTvNickname;
    private RelativeLayout mLayoutNickname;
    private TextView mTvSex;
    private RelativeLayout mLayoutSex;
    private TextView mTvBirth;
    private RelativeLayout mLayoutBirth;
    private TextView mTvConstellation;
    private RelativeLayout mLayoutConstellation;
    private TextView mTvExplain;
    private LinearLayout mLayoutExplain;
    private SessionCache mSessionCache;
    private P10103 mP10103;
    private Myhandler mMyhandler = new Myhandler(this);
    private DataTask mDataTask;
    private P620005 mP620005;
    private DatePickerDialog mDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    private P620002 mP620002;
    private Date mDate;
    private DataSaveTask mDataSaveTask;
    private boolean changeFlag = false;
    private String bitmapUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mP10103 = new P10103();
        mP620005 = new P620005();
        mSessionCache = SessionCache.getInstance(this);
        mP10103.req.userId = mSessionCache.userId;
        mP10103.req.sessionId = mSessionCache.sessionId;
        mDataTask = new DataTask();
        mDataSaveTask = new DataSaveTask();
        mP620005.req.searchUserId = mSessionCache.userId;
        mP620005.req.sessionId = mSessionCache.sessionId;

        mP620002 = new P620002();
        mP620002.req.sessionId = mSessionCache.sessionId;
        mP620002.req.userId = mSessionCache.userId;
        initView();
        initDialog();

    }
    private void initDialog() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mDialog = new DatePickerDialog(this, this, mYear, mMonth, mDay);
        DatePicker view = mDialog.getDatePicker();
        view.setMaxDate(new Date().getTime());
    }

    private void initData(P620005 p620005) {

        mTvMiddleTitle.setText("个人资料");

        mTvNickname.setText(p620005.resp.userName + "");
        if (p620005.resp.gender != 0) {
            if (p620005.resp.gender==1) {
                mTvSex.setText("男");
            } else {
                mTvSex.setText("女");
            }
        }
        Glide.with(this).load(AIMIApplication.dealHeadImg(p620005.resp.headImg))
                .placeholder(R.drawable.default_icon)
                .into(mImgHead);
        Date birthday = mP620005.resp.birthday;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String s = format.format(birthday);
        mTvBirth.setText( s+ "");

        mTvConstellation.setText(mP620005.resp.constellation + "");
        if (!TextUtils.isEmpty(mP620005.resp.autograph)) {
            mTvExplain.setText(mP620005.resp.autograph + "");
        }else {
            mTvExplain.setText(R.string.default_dis);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (changeFlag){
            Glide.with(this).load(bitmapUrl)
                    .placeholder(R.drawable.default_icon)
                    .into(mImgHead);
            changeFlag = false;
        }else {
            mDataTask.execute(this,mP620005);
        }
    }

    private void initView() {
        mImgLeftBack = (ImageView) findViewById(R.id.img_left_back);
        mImgLeftBack.setOnClickListener(this);
        mTvMiddleTitle = (TextView) findViewById(R.id.tv_middle_title);
        mImgHead = (RoundImageView) findViewById(R.id.img_head);
        mImgHead.setOnClickListener(this);
        mLayoutHead = (RelativeLayout) findViewById(R.id.layout_head);
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);
        mLayoutNickname = (RelativeLayout) findViewById(R.id.layout_nickname);
        mLayoutNickname.setOnClickListener(this);
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        mLayoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        mTvBirth = (TextView) findViewById(R.id.tv_birth);
        mLayoutBirth = (RelativeLayout) findViewById(R.id.layout_birth);
        mLayoutBirth.setOnClickListener(this);
        mTvConstellation = (TextView) findViewById(R.id.tv_constellation);
        mLayoutConstellation = (RelativeLayout) findViewById(R.id.layout_constellation);
        mTvExplain = (TextView) findViewById(R.id.tv_explain);
        mLayoutExplain = (LinearLayout) findViewById(R.id.layout_explain);
        mLayoutExplain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left_back:
                finish();
                break;
            case R.id.img_head:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                        @Override
                        public void onAllGranted() {
                            AlbmActivity.toAlbmWithResult(PersonActivity.this,API.DONGTAI_PHOTO,true,true,false);
                        }

                        @Override
                        public void onGranted(List<String> grantPermissions) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {

                        }
                    });
                } else {
                    AlbmActivity.toAlbmWithResult(PersonActivity.this,API.DONGTAI_PHOTO,true,true,false);
                }
                break;
            case R.id.layout_nickname:
                EditDataActivity.startEditData(this, EditDataActivity.TYPE_NICKNAME, mTvNickname.getText().toString());
                break;
            case R.id.layout_birth:
                mDialog.show();
                break;
            case R.id.layout_explain:
                EditDataActivity.startEditData(this, EditDataActivity.TYPE_DIS, mTvExplain.getText().toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == API.DONGTAI_PHOTO )) {
            if (data != null) {
                String photo_list = data.getStringExtra(AlbmActivity.filePath);
                bitmapUrl = photo_list;
                final Bitmap bitmap = BitmapFactory.decodeFile(bitmapUrl);
                changeFlag = true;
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateFile(bitmap, mP10103);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }
    }


    //上传头像
    public void updateFile(Bitmap bitmap, IProtocol protocol) {
        mP10103 = (P10103) protocol;
        Map<String, String> params = new HashMap<>();
        params.put("content", JSON.toJSONString(mP10103.req));
        params.put("transcode", mP10103.req.transcode);
        params.put("sessionId", mSessionCache.sessionId);
        try {

            File mTmpFile = FileUtils.createTmpFile(this, ".jpg");
            Utils.compressBmpToFile(bitmap, mTmpFile);
            FormFile file = new FormFile(mTmpFile.getName(), Utils.getBytesFromFile(mTmpFile), "imgBase", null);
            SocketBean bean = SocketHttpRequester.post("", params, file);
            if (bean.isSuccess) {
                mMyhandler.sendEmptyMessage(0);
                P10103.Resp p10103 = JSON.parseObject(bean.content, P10103.Resp.class);

                mSessionCache.headimgUrl = p10103.headImg;
                mSessionCache.save();
            } else {
                mMyhandler.sendEmptyMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mTvBirth.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
        mDate = new Date((year-1900), monthOfYear, dayOfMonth);
        mP620002.req.birthday = mDate;
        mDialog.dismiss();
        mDataSaveTask.execute(this, mP620002);

    }

    public static class Myhandler extends WeakHandler {

        public Myhandler(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PersonActivity objct = (PersonActivity) getObjct();
            if (objct == null) {
                return;
            }
            if (msg.what == 0) {
                PromptUtils.showToast(objct, "头像上传成功");

            } else {
                PromptUtils.showToast(objct, "头像上传失败");
            }
        }
    }
    //请求资料
    private class DataTask extends DefaultTask{

        @Override
        public void preExecute() {
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P620005 p620005 = (P620005) protocol;
            initData(p620005);
        }
    }
    class DataSaveTask extends DefaultTask {


        private PopupWindow mProgressDialogPop;

        @Override
        public void preExecute() {
            super.preExecute();
            mProgressDialogPop = PromptUtils.getProgressDialogPop(PersonActivity.this);
            mProgressDialogPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mProgressDialogPop != null && mProgressDialogPop.isShowing()){
                mProgressDialogPop.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if(protocol instanceof P620002) {
                if (mProgressDialogPop != null && mProgressDialogPop.isShowing()){
                    mProgressDialogPop.dismiss();
                }
                P620002 p620002 = (P620002) protocol;
                //1 true
                int i = p620002.resp.state;
                if(i==1){
                    PromptUtils.showToast(PersonActivity.this, "正在保存");
                }
                String constellation = p620002.resp.constellation;
                mTvConstellation.setText(constellation+"");
                mSessionCache.constellation = constellation;
                mSessionCache.save();
            }
        }

    }
}
