package com.yeying.aimi.mode.otherdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.otherdetails.photoview.PhotoView;


public class PreViewShowActivity extends BaseActivity {

    private PhotoView mPhotoView;
    public static final String ARG_IMG = "img";
    private String mPath;
    public static PreViewShowActivity mActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view_show);
        mActivity = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            mPath = getIntent().getStringExtra(ARG_IMG);
        }
        Glide.with(this).load(mPath).placeholder(R.drawable.default_icon).into(mPhotoView);
    }

    private void initView() {
        mPhotoView = (PhotoView) findViewById(R.id.photoView);
        mPhotoView.canZoom();

    }

    public static PreViewShowActivity getIntentce() {

        return mActivity;
    }

    public static void toPreView(Context context , String imgUrl){
        Intent intent = new Intent(context,PreViewShowActivity.class);
        intent.putExtra(ARG_IMG,imgUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}
