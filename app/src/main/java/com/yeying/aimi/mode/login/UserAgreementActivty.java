package com.yeying.aimi.mode.login;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;

/**
 * Created by zhengj on 2017/6/22.
 */

public class UserAgreementActivty extends BaseActivity{

    private RelativeLayout back;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_agreement);
        back = (RelativeLayout) findViewById(R.id.ll_userAgreement_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAgreementActivty.this.finish();
            }
        });
    }


}
