package com.yeying.aimi.mode.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P620001;
import com.yeying.aimi.protocol.impl.P620004;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;


public class EditDataActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEdNickname;
    private ImageButton mImgbtnCancle;
    private LinearLayout mLayoutNickname;
    private EditText mEdDis;
    private RelativeLayout mLayoutDis;
    public static final int TYPE_NICKNAME = 0;
    public static final int TYPE_DIS = 1;
    private static final String KEY_MODE = "MODE";
    public static final String CONTENT = "content";
    private SessionCache mSessionCache;
    private int mInt;
    private P620001 mP620001;
    private DataSaveTask mDataSaveTask;
    private P620004 mP620004;
    /**
     * 20
     */
    private TextView mTvNums;

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        mSessionCache = SessionCache.getInstance(this);
        initView();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        initData(intent);
        initRquestBody();

    }

    private void initRquestBody() {
        mDataSaveTask = new DataSaveTask();
        //昵称
        mP620001 = new P620001();
        mP620001.req.sessionId = mSessionCache.sessionId;
        mP620001.req.userId = mSessionCache.userId;
        //签名
        mP620004 = new P620004();
        mP620004.req.userId = mSessionCache.userId;
        mP620004.req.sessionId = mSessionCache.sessionId;


    }

    private void initData(Intent intent) {
        mInt = intent.getIntExtra(KEY_MODE, -1);
        String s = intent.getStringExtra(CONTENT);
        if (mInt == 0) {//昵称模式
            title_center_view.setText("昵称");
            mEdNickname.setText(s);
            mLayoutNickname.setVisibility(View.VISIBLE);
        } else if (mInt == 1) {//个人简介
            title_center_view.setText("个人简介");
            if (!s.equals("这个人比较懒,什么也没有留下...")) {
                mEdDis.setText(s);
            }
            mLayoutDis.setVisibility(View.VISIBLE);
        }
        title_right_tv.setText("完成");


    }

    public static void startEditData(Context context, int type, String value) {
        Intent intent = new Intent(context, EditDataActivity.class);
        intent.putExtra(KEY_MODE, type);
        intent.putExtra(CONTENT, value);
        context.startActivity(intent);
    }

    private void initView() {

        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setTextColor(getResources().getColor(R.color.white));
        title_right_tv.setBackgroundColor(getResources().getColor(R.color.black));

        mEdNickname = (EditText) findViewById(R.id.ed_nickname);
        mImgbtnCancle = (ImageButton) findViewById(R.id.imgbtn_cancle);
        mImgbtnCancle.setOnClickListener(this);
        mLayoutNickname = (LinearLayout) findViewById(R.id.layout_nickname);
        mEdDis = (EditText) findViewById(R.id.ed_dis);
        mLayoutDis = (RelativeLayout) findViewById(R.id.layout_dis);
        mTvNums = (TextView) findViewById(R.id.tv_nums);
        mEdDis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvNums.setText(30 - s.length() + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.imgbtn_cancle:
                mEdNickname.setText("");
                break;
            case R.id.title_right:
                if (mInt == TYPE_NICKNAME) {
                    if (!TextUtils.isEmpty(mEdNickname.getText().toString().trim())) {
                        mP620001.req.nick_name = mEdNickname.getText().toString();
                        mDataSaveTask.execute(this, mP620001);
                    } else {
                        PromptUtils.showToast(this, "昵称不能为空");
                    }
                } else if (mInt == TYPE_DIS) {
                    mP620004.req.autograph = mEdDis.getText().toString().trim();
                    mDataSaveTask.execute(this, mP620004);
                }
                break;
        }
    }


    class DataSaveTask extends DefaultTask {

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(EditDataActivity.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }

            if (protocol instanceof P620001) {
                P620001 p620001 = (P620001) protocol;
                //1 true
                if (p620001.resp.state == 1) {
                    mSessionCache.nickname = mP620001.req.nick_name;
                    mSessionCache.save();
                    finish();
                } else {
                    PromptUtils.showToast(EditDataActivity.this, "保存失败");
                }

            } else if (protocol instanceof P620004) {
                P620004 p620004 = (P620004) protocol;
                //1 true
                if (p620004.resp.state == 1) {
                    mSessionCache.autograph = mP620004.req.autograph;
                    mSessionCache.save();
                    finish();
                } else {
                    PromptUtils.showToast(EditDataActivity.this, "保存失败");
                }
            }

        }

    }
}
