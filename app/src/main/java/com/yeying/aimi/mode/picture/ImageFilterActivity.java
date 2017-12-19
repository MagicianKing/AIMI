package com.yeying.aimi.mode.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.adapter.ImageFilterAdapter;
import com.yeying.aimi.utils.GPUImageFilterTools;
import com.yeying.aimi.utils.PromptUtils;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class ImageFilterActivity extends Activity implements View.OnClickListener, ImageFilterAdapter.onItemClickLisener, GPUImageView.OnPictureSavedListener {

    public static final String KEY_IMAGE = "URI";
    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;
    private HorizontalScrollView mFilterList;
    private GPUImageFilterTools mTools;
    private List<GPUImageFilter> mGpuImageFilters;
    private ImageFilterAdapter mAdapter;
    private GPUImageView mSfPreview;
    private PopupWindow mPopupWindow;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        mTools = new GPUImageFilterTools();
        mGpuImageFilters = mTools.getGPUImageFilters();
        initView();
        if (getIntent() == null) {
            new RuntimeException("Please pass the picture URI");
            return;
        }
        //初始化滤镜
        initImage(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initImage(Intent intent) {
        String s = intent.getStringExtra(KEY_IMAGE);
        bitmap = BitmapFactory.decodeFile(s);
        mSfPreview.setImage(bitmap);//设置图片
        mAdapter = new ImageFilterAdapter(this,mGpuImageFilters,bitmap,mFilterList);
        mAdapter.setOnItemClickLisener(this);
    }

    private void initView() {

        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_right_tv.setText("下一步");
        title_right_tv.setBackgroundResource(R.drawable.round_yellow_bg);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setOnClickListener(this);
        title_left.setOnClickListener(this);
        title_center_view.setText("编辑");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);

        mSfPreview = (GPUImageView) findViewById(R.id.sf_preview);
        mFilterList = (HorizontalScrollView) findViewById(R.id.filter_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right_tv:
                mPopupWindow = PromptUtils.getProgressDialogPop(ImageFilterActivity.this);
                mPopupWindow.showAtLocation(title_right_tv, Gravity.CENTER,0,0);
                String fileName = System.currentTimeMillis() + ".png";
                Log.e("这是Gpu", fileName);

                mSfPreview.saveToPictures("GPUImage", fileName, bitmap.getWidth(), bitmap.getHeight(), this);
                break;
        }
    }

    @Override
    public void itemClickListener(int position) {
        mSfPreview.setFilter(mGpuImageFilters.get(position));
    }

    public static void toImageFilter(Context context,String filePath){
        Intent intent = new Intent(context,ImageFilterActivity.class);
        intent.putExtra(KEY_IMAGE,filePath);
        context.startActivity(intent);
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

    @Override
    public void onPictureSaved(Uri uri) {
        if (mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
        UserFaBuActivity.toUserFaBu(ImageFilterActivity.this,getAbsoluteImagePath(uri));
    }

    //图片uri转变为路径
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
