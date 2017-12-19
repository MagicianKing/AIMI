package com.yeying.aimi.mode.bar_info;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.OutBarAdapter_A;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.ActiveList;
import com.yeying.aimi.bean.BarPhoneList;
import com.yeying.aimi.bean.BarUserPicList;
import com.yeying.aimi.bean.OutBean;
import com.yeying.aimi.bean.YeMaoList;
import com.yeying.aimi.mode.dynamics_detail.DynamicsDetail;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.protocol.impl.P10112;
import com.yeying.aimi.storage.SessionCache;

import java.util.ArrayList;
import java.util.List;


public class OutBarActivity extends BaseActivity implements View.OnClickListener, BaseActivity.PermissionCheckedLister, OutBarAdapter_A.ItemClick {
    public static final String OUT_DATA = "OUT_DATA";
    private ImageView mImgHead;

    private TextView mTvLocation;
    private RecyclerView mRecPersonlist;

    private TextView mTvPhone;
    private TextView mTvAddress;
    private RecyclerView mDynamicList;
    private TextView mTvDis;

    private ImageView mTitleBack;
    private ImageView mTitleMenu;
    private TextView mTitleName;
    private TextView mTvActivetitle;
    private OutBarAdapter_A mNightCatAdapter;
    private OutBarAdapter_A mDynamicAdapter;
    private List<Object> mObjects = new ArrayList<>();
    private LinearLayout mLayoutNightcats;
    private LinearLayout mLayoutPhone;
    /**
     * dynamic
     */
    private TextView mTvDymaictitle;
    private LinearLayout mLayoutActives;
    private LinearLayout mLayoutAddress;
    private LinearLayout mLayoutDynamic;
    private List<YeMaoList> mYeMaoList;
    private List<BarUserPicList> mBarUserPicList;
    private String headUrl;
    private SessionCache mSessionCache;
    private TextView mPop_out_yes;
    private RelativeLayout mPop_out_parent;
    private PopupWindow mPopupWindow;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_bar);
        mSessionCache = SessionCache.getInstance(this);
        mSharedPreferences = getSharedPreferences("is_first_in_outbar" , MODE_PRIVATE);
        initView();
        if (getIntent() == null) {
            return;
        }
        initData(getIntent());
    }

    private void initData(Intent intent) {
        P10112.Resp data = (P10112.Resp) intent.getSerializableExtra(OUT_DATA);
        mTitleName.setText(data.barName + "");
        mTvLocation.setText(data.distance + "km");
        if (data.address != null) {
            mTvAddress.setText(data.city + data.area + data.address + "");
            mLayoutAddress.setVisibility(View.VISIBLE);
        }

        Glide.with(AIMIApplication.getContext()).load(data.imgUrl + data.picUrl).into(mImgHead);
        List<BarPhoneList> phoneList = data.phoneList;//电话列表
        Log.e("wwww", "initData: "+data.phoneList.size());
        if (phoneList!=null&&phoneList.size()>0) {
            mTvPhone.setText(phoneList.get(0).getPhone());
            mLayoutPhone.setVisibility(View.VISIBLE);
        }

        headUrl = data.imgUrl;
        //夜猫列表
        mYeMaoList = data.yeMaoList;
        if (mYeMaoList !=null&& mYeMaoList.size()>0) {
            List<OutBean> stringList = new ArrayList<>();
            for (YeMaoList y:data.yeMaoList) {
                OutBean o = new OutBean();
                o.setImgUrl(y.getHeadImg());
                o.setType(true);
                stringList.add(o);
            }
            mLayoutNightcats.setVisibility(View.VISIBLE);
            mNightCatAdapter = new OutBarAdapter_A(OutBarActivity.this, stringList , headUrl);
            mNightCatAdapter.setItemClickListener(this);
            mRecPersonlist.setAdapter(mNightCatAdapter);
        }
        //酒吧动态
        mBarUserPicList = data.barUserPicList;
        if (mBarUserPicList !=null&& mBarUserPicList.size()>0) {
            List<OutBean> stringList = new ArrayList<>();
            for (BarUserPicList b:data.barUserPicList) {
                OutBean o = new OutBean();
                o.setImgUrl(b.getPicUrl());
                o.setType(false);
                stringList.add(o);
            }
            mLayoutDynamic.setVisibility(View.VISIBLE);
            Log.e(TAG, "initData: "+data.imgUrl);
            Log.e(TAG, "initData: "+data.barUserPicList.get(0).getPicUrl());
            Log.e(TAG, "initData: "+data.imgUrl+data.barUserPicList.get(0).getPicUrl());
            mDynamicAdapter = new OutBarAdapter_A(OutBarActivity.this, stringList, headUrl);
            mDynamicAdapter.setItemClickListener(this);
            mDynamicList.setAdapter(mDynamicAdapter);
        }
        List<ActiveList> activityList = data.activityList;//活动列表
        if (activityList!=null&&activityList.size()>0) {
            mTvActivetitle.setText(activityList.get(0).getAct_title());
            mTvDis.setText(activityList.get(0).getActivityContent());
            mLayoutActives.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mImgHead = (ImageView) findViewById(R.id.img_head);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mRecPersonlist = (RecyclerView) findViewById(R.id.rec_personlist);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(OutBarActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecPersonlist.setLayoutManager(linearLayoutManager1);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mDynamicList = (RecyclerView) findViewById(R.id.dynamic_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OutBarActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDynamicList.setLayoutManager(linearLayoutManager);
        mTvDis = (TextView) findViewById(R.id.tv_dis);
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleMenu = (ImageView) findViewById(R.id.title_menu);
        mTitleName = (TextView) findViewById(R.id.title_name);
        mTitleBack.setOnClickListener(this);
        mTvActivetitle = (TextView) findViewById(R.id.tv_activetitle);

        mLayoutNightcats = (LinearLayout) findViewById(R.id.layout_nightcats);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_phone);
        mLayoutPhone.setOnClickListener(this);
        mTvDymaictitle = (TextView) findViewById(R.id.tv_dymaictitle);
        mLayoutActives = (LinearLayout) findViewById(R.id.layout_actives);
        mLayoutAddress = (LinearLayout) findViewById(R.id.layout_address);
        mLayoutDynamic = (LinearLayout) findViewById(R.id.layout_dynamic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.layout_phone:
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, "", this);
                break;
            case R.id.pop_outbar_yes:
                if (mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
                break;
        }
    }

    @Override
    public void onAllGranted() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mTvPhone.getText().toString()));
        //避免报错自动生成,不用管
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onGranted(List<String> grantPermissions) {

    }

    @Override
    public void onDenied(List<String> deniedPermissions) {

    }

    @Override
    public void onCatClick(int psn) {
        if (mYeMaoList.get(psn).getUserId().equals(mSessionCache.userId)){
            //自己的
            MineHomepage.toOtherHomePage(OutBarActivity.this,
                    mYeMaoList.get(psn).getUserId(),
                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),
                    true);
        }else {
            //他人的
            OtherHomepage.toOtherHomePage(OutBarActivity.this,
                    mYeMaoList.get(psn).getUserId(),
                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationY),
                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),
                    false);
        }

    }

    @Override
    public void onDynamicClick(int psn) {
        DynamicsDetail.toDynamics(OutBarActivity.this,
                mBarUserPicList.get(psn).getMessageId(),
                mTitleName.getText().toString(),
                AIMIApplication.dealHeadImg(mBarUserPicList.get(psn).getPicUrl()),
                mBarUserPicList.get(psn).getWidth(),
                mBarUserPicList.get(psn).getHeight(),
                "");
    }

    private void createPop(){
        mPopupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT , WindowManager.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(OutBarActivity.this).inflate(R.layout.pop_outbar , null , false);
        mPopupWindow.setContentView(view);
        mPop_out_parent = (RelativeLayout) view.findViewById(R.id.pop_outbar_parent);
        mPop_out_yes = (TextView) view.findViewById(R.id.pop_outbar_yes);
        mPop_out_parent.getBackground().setAlpha(100);
        mPop_out_yes.setOnClickListener(this);
        mPopupWindow.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isShowPop();
    }

    private void isShowPop(){
        boolean is_first_in_outbar = mSharedPreferences.getBoolean("is_first_in_outbar", true);
        if (is_first_in_outbar){
            createPop();
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putBoolean("is_first_in_outbar" , false);
            edit.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
