package com.yeying.aimi.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.NewBarListBean;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.mode.bar_info.OutBarActivity;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10112;
import com.yeying.aimi.protocol.impl.P20111;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/13.
 */

public class Fragment_Search extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "Fragment_Search";

    private View mView;

    private LayoutInflater inflater;

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;

    private String imgUrl;
    private List<NewBarListBean> barList = new ArrayList<>();
    private SessionCache mSessionCache;
    private SearchAdapter mSearchAdapter;
    private BardetailTask mBardetailTask;
    private P10112 mP10112;
    private PopupWindow mPopupWindow;
    private boolean isLocationCheck = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;

        if (mView != null) {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null) {
                group.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mSessionCache = SessionCache.getInstance(getActivity());
        initView(mView);
        mBardetailTask = new BardetailTask();
        mP10112 = new P10112();
        mP10112.req.sessionId = mSessionCache.sessionId;
        mP10112.req.userId = mSessionCache.userId;
        return mView;
    }

    private void initView(View view) {
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout_search);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(mSearchAdapter);
        initBarListInfoTask();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        ((HomeActivity) getActivity()).checkLocationPermission(true);
        initBarListInfoTask();
    }

    @Override
    public void onLoadMore() {
        initBarListInfoTask();
    }

    private void initBarListInfoTask() {
        P20111 p20111 = new P20111();
        mSessionCache = SessionCache.getInstance(getActivity());
        p20111.req.sessionId = mSessionCache.userId;
        p20111.req.sessionId = mSessionCache.sessionId;
        p20111.req.locationX = mSessionCache.locationX;
        p20111.req.locationY = mSessionCache.locationY;
        new BarListInfoTask().execute(getActivity().getApplicationContext(), p20111);
    }

    public class BarListInfoTask extends DefaultTask {

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (swipeToLoadLayout.isLoadingMore()) {
                swipeToLoadLayout.setLoadingMore(false);
            }
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            PromptUtils.showToast(getActivity(), getResources().getString(R.string.bad_network_msg));
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P20111 p20111 = (P20111) protocol;
            if (swipeToLoadLayout.isLoadingMore()) {
                swipeToLoadLayout.setLoadingMore(false);
            }
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);
            }
            if (p20111.resp.transcode.equals("9999")) {
                PromptUtils.showToast(getActivity(), p20111.resp.msg);
                //TODO 用户未登录
                if (p20111.resp.msg.contains("用户未登录")) {
                    mSessionCache.phone = "";
                    mSessionCache.save();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            } else {
                imgUrl = p20111.resp.imgUrl;
                barList.clear();
                barList.addAll(p20111.resp.barList);
                mSearchAdapter.notifyDataSetChanged();
            }
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapter_VH> implements View.OnClickListener {

        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);

        @Override
        public SearchAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = inflater.inflate(R.layout.adapter_bar_list, parent, false);

            return new SearchAdapter_VH(itemView);
        }

        @Override
        public void onBindViewHolder(SearchAdapter_VH holder, int position) {
            //item动画设置
            holder.search_bg_ceng.getBackground().setAlpha(130);
            holder.search_distance.setVisibility(View.VISIBLE);
            holder.search_barname.setVisibility(View.VISIBLE);
            holder.search_nums.setVisibility(View.VISIBLE);
            ObjectAnimator.ofPropertyValuesHolder(holder.search_parent, pvhY, pvhZ).setDuration(500).start();
            holder.search_nums.setText(barList.get(position).getHotNumber() + "");
            holder.search_distance.setText(barList.get(position).getDistance() + "km");
            holder.search_barname.setText(barList.get(position).getBarName());
            Glide.with(getActivity()).load(imgUrl + barList.get(position).getPicUrl()).into(holder.search_bg);
            holder.search_parent.setTag(position);
            holder.search_parent.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return barList != null ? barList.size() : 0;
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.search_parent:
                    mPopupWindow = PromptUtils.getProgressDialogPop(getActivity());
                    mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    loginHXRepte(position);
                    break;
            }
        }

        class SearchAdapter_VH extends RecyclerView.ViewHolder {

            private ImageView search_bg , search_bg_ceng;
            private TextView search_nums, search_distance, search_barname;
            private FrameLayout search_parent;

            public SearchAdapter_VH(View itemView) {
                super(itemView);
                search_bg_ceng = (ImageView) itemView.findViewById(R.id.search_bg_ceng);
                search_bg = (ImageView) itemView.findViewById(R.id.search_bg);
                search_nums = (TextView) itemView.findViewById(R.id.search_nums);
                search_distance = (TextView) itemView.findViewById(R.id.search_distance);
                search_barname = (TextView) itemView.findViewById(R.id.search_barname);
                search_parent = (FrameLayout) itemView.findViewById(R.id.search_parent);
            }
        }
    }

    class BardetailTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void cancel() {
            super.cancel();

        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }

            P10112 p10112 = (P10112) protocol;
            Intent intent;
            if (p10112.resp.flag == 1) {
                intent = new Intent(getActivity(), Activity_BaPing.class);
                intent.putExtra(Activity_BaPing.DATA, p10112.resp);
                intent.putExtra(API.BAR_ID, p10112.resp.barId);
                intent.putExtra(API.BAR_NAME, p10112.resp.barName);
                intent.putExtra(API.TO_CHAT_USERID, p10112.resp.chatGroupId);
                startActivity(intent);
            } else if (p10112.resp.flag == 0) {
                intent = new Intent(getActivity(), OutBarActivity.class);
                intent.putExtra(OutBarActivity.OUT_DATA, p10112.resp);
                startActivity(intent);
            }
        }
    }

    /**
     * 环信登录操作 知道登录成功后再进行业务逻辑
     */
    public void loginHXRepte(final int position) {
        if (EMChat.getInstance().isLoggedIn()) {
            Log.e(TAG, "onSuccess: 环信登录成功");
            loginHXSuccessed(position);
        } else {
            SessionCache session = SessionCache.getInstance(getActivity());
            EMChatManager.getInstance().login(session.userId, session.userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    Log.e(TAG, "onSuccess: 环信登录成功");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            loginHXSuccessed(position);
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.e(TAG, "onError: 环信登录失败");
                    loginHXRepte(position);
                }
            });

        }
    }

    /**
     * 登录环信成功后的业务逻辑
     */
    public void loginHXSuccessed(int position){
        mP10112.req.barId = barList.get(position).getBarId();
        mP10112.req.locationX = TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX);
        mP10112.req.locationY = TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY);
        if (TextUtils.isEmpty(mP10112.req.barId)) {
            return;
        }
        mBardetailTask.execute(getActivity(), mP10112);
    }

}
