package com.yeying.aimi.mode.inform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.Adapter_InfromMessage;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.dynamics_detail.DynamicsDetail;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.storage.ReplyHistoryCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.WeakHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.yeying.aimi.R.id.swipetoloadlayout;

/**
 * Created by tanchengkeji on 2017/8/15.
 */

public class Fragment_InformLike extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, Adapter_InfromMessage.ItemClickListener, View.OnClickListener {

    private static final String TAG = "Fragment_InformLike";

    private View fragmentView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipe_target;

    private List<ReplyCacheBean> like_list = new ArrayList<>();
    private List<ReplyCacheBean> list_like = new LinkedList<>();

    private Adapter_InfromMessage adapter_InfromMessage;

    private int deletePsn;
    private String deleteMsgId;
    private PopupWindow popupWindow;
    private View deleteView;
    private TextView bt_close;
    private TextView bt_sure;
    private HandlerPlus handler;
    private SessionCache mSessionCache;
    private View view;
    private RelativeLayout mLayoutNone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentView != null) {
            ViewGroup viewGroup = (ViewGroup) fragmentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(fragmentView);
            }
            return fragmentView;
        }

        fragmentView = inflater.inflate(R.layout.fragment_informmessage, container, false);
        mSessionCache = SessionCache.getInstance(getActivity());
        deleteView = inflater.inflate(R.layout.del_msg_window, container, false);
        initDeleteView();

        initView();
        initMessage();
        handler = new HandlerPlus(this);

        return fragmentView;
    }

    private void initDeleteView() {
        bt_close = (TextView) deleteView.findViewById(R.id.bt_close);
        bt_sure = (TextView) deleteView.findViewById(R.id.bt_sure);
        bt_close.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter_InfromMessage = new Adapter_InfromMessage(list_like, getActivity(), getActivity());
        swipe_target.setAdapter(adapter_InfromMessage);
        adapter_InfromMessage.notifyDataSetChanged();
        adapter_InfromMessage.setItemClickListener(this);
    }

    private void initMessage() {
        list_like.clear();
        ReplyHistoryCache historyCache = ReplyHistoryCache.getCommentCache(getActivity());
        for (ReplyCacheBean bean : historyCache.getReplyHistoryList()) {
            if (bean.getType() == 1) {
                list_like.add(bean);
            }
        }
        Collections.reverse(list_like);
        if(list_like.size()<=0){
            mLayoutNone.setVisibility(View.VISIBLE);
            swipeToLoadLayout.setVisibility(View.GONE);
        }else{
            mLayoutNone.setVisibility(View.GONE);
            swipeToLoadLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) fragmentView.findViewById(swipetoloadlayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshEnabled(true);
        swipeToLoadLayout.setLoadMoreEnabled(true);
        swipe_target = (RecyclerView) fragmentView.findViewById(R.id.swipe_target);
        swipe_target.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLayoutNone = (RelativeLayout) fragmentView.findViewById(R.id.layout_none);
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    public void onLoadMore() {
        handler.sendEmptyMessageDelayed(1, 1500);
    }

    private class HandlerPlus extends WeakHandler {

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Fragment_InformLike fragment_InformLike = (Fragment_InformLike) getObjct();
            if (fragment_InformLike == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    if (swipeToLoadLayout.isRefreshing())
                        swipeToLoadLayout.setRefreshing(false);
                    break;
                case 1:
                    if (swipeToLoadLayout.isLoadingMore())
                        swipeToLoadLayout.setLoadingMore(false);
                    break;
            }
        }
    }

    @Override
    public void onItemViewClick(int psn) {
        int type = list_like.get(psn).getType();
        if (type == 1) {
            Intent messageintent2 = new Intent(getActivity(), DynamicsDetail.class);
            messageintent2.putExtra("backUrl", list_like.get(psn).getPic_img());
            messageintent2.putExtra("messageCode", list_like.get(psn).getId());
            messageintent2.putExtra("arg1", 1);
            startActivity(messageintent2);
        }
    }

    @Override
    public void onHeadImgClick(int psn) {
        int type = list_like.get(psn).getType();
        if (type == 1) {
            OtherHomepage.toOtherHomePage(getActivity(),
                    list_like.get(psn).getUserId(),
                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY), false);
        }
    }

    @Override
    public void onItemLongClick(int psn) {
        deleteMsgId = list_like.get(psn).getMsgId();
        deletePsn = psn;
        popupWindow = new PopupWindow(deleteView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.bt_sure:
                ReplyHistoryCache commentCache = ReplyHistoryCache.getCommentCache(getActivity());
                for (int i = 0; i < commentCache.getReplyHistoryList().size(); i++) {
                    if (commentCache.getReplyHistoryList().get(i).getMsgId() != null) {
                        if (commentCache.getReplyHistoryList().get(i).getMsgId().equals(deleteMsgId)) {
                            commentCache.getReplyHistoryList().remove(commentCache.getReplyHistoryList().get(i));
                            commentCache.save();
                        }
                    }
                }
                list_like.remove(deletePsn);
                adapter_InfromMessage.notifyDataSetChanged();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if(list_like.size()<=0){
                    mLayoutNone.setVisibility(View.VISIBLE);
                    swipeToLoadLayout.setVisibility(View.GONE);
                }else{
                    mLayoutNone.setVisibility(View.GONE);
                    swipeToLoadLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
