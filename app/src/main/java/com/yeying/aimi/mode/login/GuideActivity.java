package com.yeying.aimi.mode.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivityWithoutSwipeBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/17.
 */

public class GuideActivity extends BaseActivityWithoutSwipeBack implements View.OnClickListener {

    private ViewPager guide_vp;
    private TextView guide_tv;
    private List<ImageView> mImageViews = new ArrayList<>();
    private int [] imgValues = new int[3];
    private GuidePageAdapter mGuidePageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initData();
        initView();
    }

    private void initData() {
        imgValues[0] = R.drawable.guide_one;
        imgValues[1] = R.drawable.guide_two;
        imgValues[2] = R.drawable.guide_three;
        for (int i = 0 ; i < 3 ; i ++){
            ImageView imageView = new ImageView(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgValues[i]);
            mImageViews.add(imageView);
        }
    }

    private void initView() {
        guide_tv = (TextView) findViewById(R.id.guide_tv);
        guide_tv.setOnClickListener(this);
        guide_vp = (ViewPager) findViewById(R.id.guide_vp);
        mGuidePageAdapter = new GuidePageAdapter();
        guide_vp.setAdapter(mGuidePageAdapter);
        guide_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageViews.size()-1){
                    guide_tv.setVisibility(View.VISIBLE);
                }else {
                    guide_tv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guide_tv:
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                intent.putExtra("is_reback" , false);
                startActivity(intent);
                finish();
                break;
        }
    }

    class GuidePageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViews == null ? 0 : mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            container.removeView(imageView);
        }
    }
}
