package com.yeying.aimi.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.TotalBean;
import com.yeying.aimi.bean.TotalPicInfo;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.dynamics_detail.DynamicsDetail;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P20116;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.RoundTransform;
import com.yeying.aimi.utils.PromptUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by tanchengkeji on 2017/9/13.
 */

public class Fragment_Total extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "Fragment_Total";

    private View mView;

    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;

    private int pageIndex = 1;
    private int sex = 3;
    private int area = 1;
    private SessionCache session;

    private List<TotalBean> words;
    private String img_head;
    private List<TotalPicInfo> info_list = new ArrayList<>();
    private LayoutInflater inflater;
    private int windowWidth;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private HomePageAdapter homePageAdapter;
    private P20116 p20116 = new P20116();
    private boolean isRefresh;

    private boolean isNewFaBu=false;
    private boolean isLocationCheck = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView != null) {
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mView);
            }
            return mView;
        }
        this.inflater = inflater;
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowWidth = displayMetrics.widthPixels;
        mView = inflater.inflate(R.layout.fragment_total, container, false);
        initView(mView);


        initFirstInfo();
        //双击回调
        ((HomeActivity) getActivity()).setDealDoubleClick(new HomeActivity.DealDoubleClick() {
            @Override
            public void dealDoubleClick() {
                mRecyclerView.scrollToPosition(0);
                autoRefresh();
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationCheck){
            ((HomeActivity) getActivity()).checkLocationPermission(isLocationCheck);
            isLocationCheck = true;
        }
        //有新发布的照片显示到首位
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("FaBuBean", Context.MODE_PRIVATE);
        isNewFaBu = sharedPreferences.getBoolean("is_new",false);
        if (isNewFaBu){
            HomeActivity.isMineRefresh = true;//用户发布图片后 改变值 然后再点击我的界面时通知刷新
            TotalPicInfo bean = new TotalPicInfo();
            bean.setIsrc(sharedPreferences.getString("bean_Isrc",""));
            bean.setHeight(sharedPreferences.getInt("bean_height",0));
            bean.setWidth(sharedPreferences.getInt("bean_width",0));
            bean.setMessgeid(sharedPreferences.getString("bean_msg_id",""));
            bean.setMsg(sharedPreferences.getString("bean_msg",""));
            bean.setDitance(sharedPreferences.getString("bean_distance",""));
            bean.setBarName(sharedPreferences.getString("bean_barName",""));
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("is_new",false);
            edit.apply();
            info_list.add(0,bean);
            homePageAdapter.notifyDataSetChanged();
            isNewFaBu = false;
        }
    }

    private void initView(@NonNull final View itemView) {
        mSwipeToLoadLayout = (SwipeToLoadLayout) itemView.findViewById(R.id.swipeToLoadLayout_home);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.swipe_target)    ;
        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager sgl = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sgl);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homePageAdapter = new HomePageAdapter();
        mRecyclerView.setAdapter(homePageAdapter);
    }

    private void initFirstInfo() {
        generateP20116(pageIndex);
    }

    @Override
    public void onRefresh() {
        ((HomeActivity) getActivity()).checkLocationPermission(true);
        isRefresh = true;
        pageIndex = 1;
        generateP20116(pageIndex);
        loginHX();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        pageIndex++;
        generateP20116(pageIndex);
    }

    private void generateP20116(int pageIndex){
        session = SessionCache.getInstance(getActivity());
        p20116.req.userId = session.userId;
        p20116.req.sessionId = session.sessionId;
        p20116.req.sex = sex;
        p20116.req.area = area;
        p20116.req.pageNum = pageIndex;
        p20116.req.pageSize = 40;
        p20116.req.locationX = session.locationX;
        p20116.req.locationY = session.locationY;
        new RequestInfoTask().execute(getActivity().getApplicationContext(), p20116);
    }

    /**
     * 数据请求
     */
    private class RequestInfoTask extends DefaultTask {
        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
            Log.e(TAG, "onProgress: 大小:"+obj.getTotal()+"......进度"+obj.getBytes()+"......"+obj.isRead());
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mSwipeToLoadLayout.isRefreshing()){
                mSwipeToLoadLayout.setRefreshing(false);
            }
            if (mSwipeToLoadLayout.isLoadingMore()){
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P20116 p20116 = (P20116) protocol;
            if (p20116.resp.transcode.equals("9999")) {
                PromptUtils.showToast(getActivity(), p20116.resp.msg);
                if (p20116.resp.msg.contains("用户未登录")) {
                    session.phone = "";
                    session.save();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                if (mSwipeToLoadLayout.isRefreshing()){
                    mSwipeToLoadLayout.setRefreshing(false);
                }
                if (mSwipeToLoadLayout.isLoadingMore()){
                    mSwipeToLoadLayout.setLoadingMore(false);
                }
            } else {
                //图片前缀
                img_head = p20116.resp.imgUrl;
                //数据集合
                words = p20116.resp.words;
                List<TotalPicInfo> list = new ArrayList<>();
                for (TotalBean bean : words) {
                    TotalPicInfo duiTangInfo = new TotalPicInfo();
                    duiTangInfo.setIsrc(img_head + bean.getPic_urls().get(0).getPic_url());
                    duiTangInfo.setMsg(bean.getMessage());
                    duiTangInfo.setHeight(bean.getPic_urls().get(0).getPic_height());
                    duiTangInfo.setMessgeid(bean.getMessage_id());
                    duiTangInfo.setDitance(bean.distance);
                    duiTangInfo.setWidth(bean.getPic_urls().get(0).getPic_width());
                    duiTangInfo.setBarName(bean.getBarName());
                    list.add(duiTangInfo);
                }
                if (isRefresh){
                    info_list.clear();
                    mSwipeToLoadLayout.setRefreshing(false);
                    info_list.addAll(list);
                    homePageAdapter.notifyDataSetChanged();
                }else{
                    mSwipeToLoadLayout.setLoadingMore(false);
                    int size = info_list.size();
                    info_list.addAll(list);
                    homePageAdapter.notifyItemInserted(size);
                }
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    /**
     * 适配器
     */
    private class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageAdapter_VH> {

        Intent intent = new Intent(getActivity(), DynamicsDetail.class);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f,1f);
        @Override
        public HomePageAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.newsinfos_list, parent, false);
            return new HomePageAdapter_VH(itemView);
        }

        @Override
        public void onBindViewHolder(HomePageAdapter_VH holder,final int position) {
            holder.mPicNews.setAdjustViewBounds(true);
            //距离
            final String info = info_list.get(position).getBarName().equals("") ? info_list.get(position).getDitance()+"km" : info_list.get(position).getBarName();
            holder.mNewsTitleKsp.setText(info);
            //图片加载
            ViewGroup.LayoutParams layoutParams = holder.mPicNews.getLayoutParams();
            double scaleSize = 1 ;
            if (windowWidth/2>info_list.get(position).getWidth()){
                scaleSize =(double)(windowWidth/2)/(double)(info_list.get(position).getWidth());
                layoutParams.height = (int) (scaleSize * info_list.get(position).getHeight());
            }else {
                scaleSize =(double)(info_list.get(position).getWidth())/(double)(windowWidth/2);
                layoutParams.height = (int) (info_list.get(position).getHeight()/scaleSize);
            }
            if (layoutParams.height>windowWidth){
                layoutParams.height = windowWidth;
            }
            holder.mPicNews.setLayoutParams(layoutParams);
            holder.mPicNews.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getActivity())
                    .load(info_list.get(position).getIsrc())
                    .placeholder(R.drawable.grey_round_bg)
                    .error(R.drawable.grey_round_bg)
                    .transform(new RoundTransform(AIMIApplication.getContext(),info_list.get(position).getIsrc()))
                    .into(holder.mPicNews);
            //item动画设置
            ObjectAnimator.ofPropertyValuesHolder(holder.mNews_list,pvhY,pvhZ).setDuration(500).start();
            //单击事件
            holder.mPicNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("messageCode", info_list.get(position).getMessgeid());
                    intent.putExtra("messageName", info_list.get(position).getMsg());
                    intent.putExtra("backUrl", info_list.get(position).getIsrc());
                    intent.putExtra("wight", info_list.get(position).getWidth());
                    intent.putExtra("height", info_list.get(position).getHeight());
                    intent.putExtra("info",info);
                    getActivity().startActivityForResult(intent,123);
                }
            });
        }

        @Override
        public int getItemCount() {
            return info_list == null ? 0 : info_list.size();
        }

        class HomePageAdapter_VH extends RecyclerView.ViewHolder {

            private ImageView mPicNews;
            private TextView mTitleNews;
            private TextView mNewsTitleKsp;
            private LinearLayout mMengban;
            private LinearLayout mNews_list;

            public HomePageAdapter_VH(View itemView) {
                super(itemView);
                mPicNews = (ImageView) itemView.findViewById(R.id.news_pic);
                mTitleNews = (TextView) itemView.findViewById(R.id.news_title);
                mNewsTitleKsp = (TextView) itemView.findViewById(R.id.ksp_news_title);
                mMengban = (LinearLayout) itemView.findViewById(R.id.mengban);
                mNews_list = (LinearLayout) itemView.findViewById(R.id.news_list);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==123){
                //删除动态的回调
                if (data!=null){
                    if (data.getBooleanExtra("flag",false)){
                        mRecyclerView.scrollToPosition(0);
                        autoRefresh();
                    }
                }
            }
        }
    }

    /**
     * 自动刷新
     */
    private void autoRefresh() {
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void loginHX() {
        if (EMChat.getInstance().isLoggedIn()) {
            Log.e("success", "已经登陆成功");
        } else {
            Log.e("success", "重新登录");
            SessionCache session = SessionCache.getInstance(getActivity());
            EMChatManager.getInstance().login(session.userId, session.userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.e("success", "登陆聊天服务器成功！");
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.e("success", "登陆聊天服务器失败！");
                }
            });
        }
    }

    AsyncTask
}
