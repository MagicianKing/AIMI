package com.yeying.aimi.mode.person;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10310;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 意见反馈
 */
public class FankuiActivity extends BaseActivity implements OnClickListener {
    private Button bt_submit;
    private EditText et_fankui;
    private EditText et_phone;

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mActivity = this;
        setContentView(R.layout.activity_fankui);
        initView();
    }

    private void initView() {

        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_left.setOnClickListener(this);
        title_center_view.setText("意见反馈");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);

        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        et_fankui = (EditText) findViewById(R.id.et_fankui);
        et_phone = (EditText) findViewById(R.id.et_phone);

    }

    public void onClick(View v) {
        if (v.getId() == title_left.getId()) {
            finish();
        } else if (v.getId() == R.id.bt_submit) {
            //准备提交
            String str = et_fankui.getText().toString();
            String phone = et_phone.getText().toString();
            if (TextUtils.isEmpty(str.trim())) {
                //PromptUtil.showToast(mActivity,"请填写内容");
            }else{
                if (!TextUtils.isEmpty(phone)){
                    Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
                    Matcher m = p.matcher(phone);
                    if (!m.matches()){
                        PromptUtils.showToast(mActivity,"请输入正确手机号！");
                        return;
                    }
                }
                SessionCache session = SessionCache.getInstance(mActivity);
                P10310 p = new P10310();
                p.req.userId = session.userId;
                p.req.sessionId = session.sessionId;
                p.req.feedback_title = "意见反馈";
                p.req.feedback_content = str;
                p.req.email = phone;
                new FanKuiTask().execute(mActivity, p);
            }
        }
    }

    /**
     * 提交意见反馈
     */
    public class FanKuiTask extends DefaultTask {

        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10310 p = (P10310) protocol;
            if (p.resp.transcode.equals("9999")) {
                //Toast.makeText(mActivity, " " + p.resp.msg, Toast.LENGTH_SHORT).show();
                PromptUtils.showToast(mActivity," " + p.resp.msg);
            } else {
                //Toast.makeText(mActivity, "提交成功！", Toast.LENGTH_SHORT).show();
                PromptUtils.showToast(mActivity,"提交成功！");
                finish();
            }
        }
    }


}
