package com.yeying.aimi.mode;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.yeying.aimi.API;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.login.GuideActivity;
import com.yeying.aimi.mode.login.LoginActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {
    private static int DELAY_TIME = 1500;
    private Timer timer;
    private TimerTask task;
    private SharedPreferences mSharedPreferences;
    private PermissionCheckedLister mPermissionCheckedLister = new PermissionCheckedLister() {
        @Override
        public void onAllGranted() {
            inNext();
        }

        @Override
        public void onGranted(List<String> grantPermissions) {

        }

        @Override
        public void onDenied(List<String> deniedPermissions) {
            inNext();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_start);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, "", mPermissionCheckedLister);
        } else {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    inNext();
                }
            };
            timer.schedule(task, DELAY_TIME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPermissions(null,null,null);
        mPermissionCheckedLister = null;
        requestPermissions(null , null , null);
    }

    /**
     * 判断用户是否首次安装
     * @param sharedPreferences
     * @return
     */
    private boolean isFirst(SharedPreferences sharedPreferences){
        if (sharedPreferences == null){
            sharedPreferences = getSharedPreferences(API.IS_FIRST,MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(API.IS_FIRST , true);
    }

    /**
     * 保存首次安装的状态
     * @param sharedPreferences
     */
    private void saveFirst(SharedPreferences sharedPreferences){
        if (sharedPreferences == null){
            sharedPreferences = getSharedPreferences(API.IS_FIRST , MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(API.IS_FIRST,false);
        edit.commit();
    }

    /**
     * 首次安装进去导航页面 不是首次打开进入登录界面
     */
    private void inNext(){
        if (isFirst(mSharedPreferences)){
            Intent intent = new Intent(StartActivity.this, GuideActivity.class);
            startActivity(intent);
            saveFirst(mSharedPreferences);
            finish();
        }else {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            intent.putExtra("is_reback" , false);
            startActivity(intent);
            finish();
        }

    }


}
