package com.yeying.aimi.mode.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;


/**
 * 免责声明
 */
public class MianZeShengMingActivity extends BaseActivity implements OnClickListener {
    private TextView title;
    private ImageView back_view;
    private TextView tv_first;
    private TextView tv_content;
    private ImageButton title_menu;
    public static final String FLAG = "flag";
    private boolean flag = false;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.simple_text_activity);
        initIntent();
        initView();
    }

    private void initIntent() {
        flag = getIntent().getBooleanExtra(FLAG , false);
    }

    private void initView() {
        title_menu = (ImageButton) findViewById(R.id.title_menu);
        title_menu.setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title_name);
        back_view = (ImageView) this.findViewById(R.id.title_back);
        back_view.setOnClickListener(this);
        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_content = (TextView) findViewById(R.id.tv_content);
        if (flag){
            //显示免责声明
            title.setText("免责声明");
            tv_first.setText("OWL  APP免责声明");
            tv_content.setText(R.string.app_statement);
        }else {
            //显示隐私条款
            title.setText("隐私条款");
            tv_first.setText("Appstore隐私政策");
            tv_content.setText(R.string.private_statement);
        }

    }

    public void onClick(View v) {
        if (v.getId() == back_view.getId()) {
            finish();
        }
    }

    public static void toMianZe(Context context , boolean flag){
        Intent intent = new Intent(context , MianZeShengMingActivity.class);
        intent.putExtra(FLAG , flag);
        context.startActivity(intent);
    }

}
