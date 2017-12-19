package com.yeying.aimi.mode.bar_info;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.CatAdapter;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.NightCatBean;
import com.yeying.aimi.bean.YeMaoBean;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.protocol.impl.P20313;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.views.LoadMoreFooterView;
import com.yeying.aimi.views.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

public class BarMemberActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mImgLeftBack;
    private TextView mTvMiddleTitle;
    private TextView mTvRightTxt;
    private ImageView mImgRightImg;
    private RecyclerView catRecycler;
    private RefreshHeaderView mSwipeRefreshHeader;
    private LoadMoreFooterView mSwipeLoadMoreFooter;
    private SwipeToLoadLayout mSwipeToLoadLayout;
    private int fromIndex = 1;
    private int toIndex = 16;
    private boolean isRefresh;
    private P20313 mP20313;
    private SessionCache mSessionCache;
    private List<NightCatBean> mNightCatBeen;


    private String barId;
    private String headUrl;
    private List<YeMaoBean> mYeMaoLists = new ArrayList<>();
    private CatAdapter mCatAdapter;
    private String mBarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_member);
        initView();
        initIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initIntent() {
        barId = getIntent().getStringExtra(API.BAR_ID);
        headUrl = getIntent().getStringExtra("headUrl");
        mBarName = getIntent().getStringExtra("barName");
        ArrayList<YeMaoBean> catList = (ArrayList<YeMaoBean>) getIntent().getSerializableExtra("catList");
        mCatAdapter = new CatAdapter(mYeMaoLists,this,headUrl);
        mCatAdapter.setHeadClickListener(new CatAdapter.HeadClick() {
            @Override
            public void onHeadClick(int position) {
                if (mYeMaoLists.get(position).getUserId().equals(mSessionCache.userId)){
                    MineHomepage.toOtherHomePage(BarMemberActivity.this,
                            mYeMaoLists.get(position).getUserId(),
                            TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                            TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),true);
                }else {
                    OtherHomepage.toOtherHomePage(BarMemberActivity.this,
                            mYeMaoLists.get(position).getUserId(),
                            TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                            TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),false);
                }

            }
        });
        catRecycler.setAdapter(mCatAdapter);
        mYeMaoLists.addAll(catList);
        mCatAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mImgLeftBack = (ImageView) findViewById(R.id.bar_back);
        mImgLeftBack.setOnClickListener(this);
        mTvMiddleTitle = (TextView) findViewById(R.id.bar_title);
        mTvRightTxt = (TextView) findViewById(R.id.tv_right_txt);
        mImgRightImg = (ImageView) findViewById(R.id.img_right_img);
        catRecycler = (RecyclerView) findViewById(R.id.swipe_target);
        mSwipeRefreshHeader = (RefreshHeaderView) findViewById(R.id.swipe_refresh_header);
        mSwipeLoadMoreFooter = (LoadMoreFooterView) findViewById(R.id.swipe_load_more_footer);
        mSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        catRecycler.setLayoutManager(gridLayoutManager);
        mSessionCache = SessionCache.getInstance(AIMIApplication.getContext());
        mNightCatBeen = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_back:
                finish();
                break;
        }
    }
}
