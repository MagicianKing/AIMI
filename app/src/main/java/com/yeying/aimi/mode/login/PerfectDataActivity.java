package com.yeying.aimi.mode.login;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.photopicker.AlbmActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.FormFile;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protoco.SocketBean;
import com.yeying.aimi.protoco.SocketHttpRequester;
import com.yeying.aimi.protocol.impl.P10103;
import com.yeying.aimi.protocol.impl.P620001;
import com.yeying.aimi.protocol.impl.P620002;
import com.yeying.aimi.protocol.impl.P620003;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.FileUtils;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.Utils;
import com.yeying.aimi.views.RoundImageView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfectDataActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ImageView mImgbtnBack;
    private RoundImageView mImgHead;
    private EditText mEdNickname;
    private TextView mTvNums;
    private TextView mTvBirth;
    private TextView mTvSex;
    private Button mBtnJoin;
    private Date mDate;
    private DatePickerDialog mDialog;
    private PopupWindow mPopwin;
    private P620001 mP620001;
    private SessionCache mSessionCache;
    private P10103 mP10103;
    private DataSaveTask mDataSaveTask;
    private P620002 mP620002;
    private P620003 mP620003;
    private String mHeadImg;
    private int mNameState;
    private int mBirthState;
    private int mSexState;
    private boolean mIsCanshow = true;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int sexFlag = 1;
    private String birth = "";
    public static String PERFECT_PHONE = "phone";
    private String oldPhone;
    private boolean mFromRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_data);
        mSessionCache = SessionCache.getInstance(this);
        initIntent();
        initView();
        //协议
        initNickNmae();
        initPopWindow();
        initDialog();
        initRequestBody();

    }

    private void initIntent() {
        oldPhone = getIntent().getStringExtra(PERFECT_PHONE);
        mFromRegister = getIntent().getBooleanExtra("fromRegister", false);
    }

    private void initRequestBody() {
        mDataSaveTask = new DataSaveTask();
        mP620001 = new P620001();
        mP620001.req.sessionId = mSessionCache.sessionId;
        mP620001.req.userId = mSessionCache.userId;

        mP10103 = new P10103();
        mP10103.req.sessionId = mSessionCache.sessionId;
        mP10103.req.userId = mSessionCache.userId;

        mP620002 = new P620002();
        mP620002.req.sessionId = mSessionCache.sessionId;
        mP620002.req.userId = mSessionCache.userId;

        mP620003 = new P620003();
        mP620003.req.sessionId = mSessionCache.sessionId;
        mP620003.req.userId = mSessionCache.userId;
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


    private void initNickNmae() {
        mEdNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvNums.setText(12 - s.length() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
                mP620001.req.nick_name = s.toString();
            }
        });
    }


    private void initView() {
        mImgbtnBack = (ImageView) findViewById(R.id.imgbtn_back);
        mImgbtnBack.setOnClickListener(this);
        mImgHead = (RoundImageView) findViewById(R.id.img_head);
        mImgHead.setOnClickListener(this);
        mEdNickname = (EditText) findViewById(R.id.ed_nickname);
        mTvNums = (TextView) findViewById(R.id.tv_nums);
        mTvBirth = (TextView) findViewById(R.id.tv_birth);
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        mBtnJoin = (Button) findViewById(R.id.btn_join);
        mBtnJoin.setOnClickListener(this);
        mTvBirth.setOnClickListener(this);
        mTvSex.setOnClickListener(this);
        //设置名字
        if (mSessionCache.nickname != null && !mSessionCache.nickname.equals("")){
            mEdNickname.setText(mSessionCache.nickname);
        }
        //设置生日
        if (mSessionCache.birthday != null && !mSessionCache.birthday.equals("")){
            mTvBirth.setTextColor(getResources().getColor(R.color.white));
            mTvBirth.setText(mSessionCache.birthday);
        }
        //设置性别
        if (mSessionCache.sex != null){
            if (mSessionCache.sex.equals("1")){
                mTvSex.setTextColor(getResources().getColor(R.color.white));
                mTvSex.setText("男");
            }else if (mSessionCache.sex.equals("2")){
                mTvSex.setTextColor(getResources().getColor(R.color.white));
                mTvSex.setText("女");
            }
        }
        //设置头像
        if (mSessionCache.picPath != null && !mSessionCache.picPath.equals("")){
            Glide.with(this).load(mSessionCache.picPath).placeholder(R.drawable.default_icon).into(mImgHead);
            mHeadImg = mSessionCache.picPath;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == API.DONGTAI_PHOTO) {
            if (data != null) {
                String photo_list = data.getStringExtra(AlbmActivity.filePath);
                String bitmapUrl = photo_list;
                final Bitmap bitmap = BitmapFactory.decodeFile(bitmapUrl);
                mImgHead.setImageBitmap(bitmap);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                       updateFile(bitmap,mP10103);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();


            }
        }

    }
    public void updateFile(Bitmap bitmap,IProtocol protocol){
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
                P10103.Resp p10103 = JSON.parseObject(bean.content, P10103.Resp .class);
                Log.e(TAG, "updateFile: "+bean.content );
                mHeadImg = p10103.headImg;
                mSessionCache.picPath = p10103.headImg;
                mSessionCache.headimgUrl = p10103.headUrl;
                mSessionCache.imgUrl = p10103.imgUrl;
                mSessionCache.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_back:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra("is_reback",true);
                intent.putExtra("isIn",false);
                setResult(RESULT_OK , intent);
                finish();
                break;
            case R.id.img_head:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                        @Override
                        public void onAllGranted() {
                            AlbmActivity.toAlbmWithResult(PerfectDataActivity.this,API.DONGTAI_PHOTO,true,true,false);
                        }

                        @Override
                        public void onGranted(List<String> grantPermissions) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {

                        }
                    });
                } else {
                    AlbmActivity.toAlbmWithResult(PerfectDataActivity.this,API.DONGTAI_PHOTO,true,true,false);
                }
                break;
            case R.id.btn_join:
                join2Home();
                break;
            case R.id.tv_birth:
                if (TextUtils.isEmpty(mEdNickname.getText())) {
                    Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDataSaveTask.execute(this, mP620001);
                break;
            case R.id.tv_sex:
                if(mIsCanshow){
                    mPopwin.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                }
                break;
        }
    }

    /**
     * 点击进入的判断
     */
    private void join2Home() {
        if(TextUtils.isEmpty(mHeadImg)){
            PromptUtils.showToast(PerfectDataActivity.this,"请选择头像");
            return;
        }
        if(TextUtils.isEmpty(mEdNickname.getText())){
            PromptUtils.showToast(PerfectDataActivity.this,"请输入昵称");
            return;
        }
        if(mBirthState!=1){
            PromptUtils.showToast(PerfectDataActivity.this,"请选择生日");
            return;
        }
        if(mSexState!=1){
            mIsCanshow = true;
            PromptUtils.showToast(PerfectDataActivity.this,"请选择性别");
            return;
        }
        if (mFromRegister){
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("isIn",true);
            intent.putExtra("is_reback",true);
            setResult(RESULT_OK , intent);
            finish();
        }
    }

    //性别选择框
    private void initPopWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater.inflate(R.layout.pop_sex, null);
        mPopwin = new PopupWindow(popupWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopwin.setOutsideTouchable(true);
        popupWindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopwin.dismiss();
            }
        });
        mPopwin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mP620003.req.sex==0) {
                    Toast.makeText(PerfectDataActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();
                }else{
                    mDataSaveTask.execute(PerfectDataActivity.this, mP620003);
                }
            }

        });
        Button bt_man = (Button) popupWindow.findViewById(R.id.bt_man);
        bt_man.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mP620003.req.sex = 1;
                mTvSex.setTextColor(getResources().getColor(R.color.white));
                mTvSex.setText("男");
                sexFlag = 1;
                mPopwin.dismiss();
            }
        });

        Button bt_woman = (Button) popupWindow.findViewById(R.id.bt_woman);
        bt_woman.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mP620003.req.sex = 2;
                mTvSex.setTextColor(getResources().getColor(R.color.white));
                mTvSex.setText("女");
                sexFlag = 2;
                mPopwin.dismiss();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        birth = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth+"日";
        mTvBirth.setText(birth);
        mTvBirth.setTextColor(getResources().getColor(R.color.white));
        mDate = new Date((year-1900), monthOfYear, dayOfMonth);
        mP620002.req.birthday = mDate;
        mDialog.dismiss();
        mDataSaveTask.execute(this, mP620002);
    }

    class DataSaveTask extends DefaultTask {
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
            if (protocol instanceof P620001) {
                mDialog.show();
                P620001 p620001 = (P620001) protocol;
                //1 true
                mNameState = p620001.resp.state;
                //昵称保存
                mSessionCache.nickname = mEdNickname.getText().toString();
                mSessionCache.save();
            } else if(protocol instanceof P620002) {
                P620002 p620002 = (P620002) protocol;
                //1 true
                mBirthState = p620002.resp.state;
                String constellation = p620002.resp.constellation;

                //生日 星座保存
                mSessionCache.constellation = constellation;
                mSessionCache.birthday = birth;
                mSessionCache.save();
            }else if(protocol instanceof P620003){
                P620003 p620003 = (P620003) protocol;
                //1 true
                mSexState = p620003.resp.state;
                mIsCanshow = false;
                //性别保存
                mSessionCache.sex = String.valueOf(sexFlag);
                mSessionCache.save();
            }
        }
    }
}
