package com.yeying.aimi.games;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;

public class ExplainActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        initView();
    }

    private void initView() {
        mImgBack = (RelativeLayout) findViewById(R.id.title_left);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_left) {
            finish();
        }
    }
}
