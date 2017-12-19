package com.yeying.aimi.mode.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.FollowsBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.mode.signlechat.SingleChat;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.views.RoundImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class Fragment_Follows extends BaseFragment {

    private static final String TAG = "Fragment_Follows";

    private View mView;
    private RecyclerView mTargetSwipe;
    private SwipeToLoadLayout mSwipeFollows;
    private LayoutInflater inflater;
    private String head;
    private List<FollowsBean> mList = new ArrayList<>();
    private FansAdapter mFansAdapter;
    private RelativeLayout follows_nodata;
    private SessionCache mSessionCache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (mView != null) {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null) {
                group.removeView(mView);
            }
            return mView;
        }
        this.inflater = inflater;
        mSessionCache = SessionCache.getInstance(getActivity());
        mView = inflater.inflate(R.layout.fragment_follows, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View itemView) {
        follows_nodata = (RelativeLayout) itemView.findViewById(R.id.follows_nodata);
        mTargetSwipe = (RecyclerView) itemView.findViewById(R.id.swipe_target);
        mTargetSwipe.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFansAdapter = new FansAdapter();
        mTargetSwipe.setAdapter(mFansAdapter);
        mSwipeFollows = (SwipeToLoadLayout) itemView.findViewById(R.id.follows_swipe);
        mSwipeFollows.setLoadMoreEnabled(false);
        mSwipeFollows.setRefreshEnabled(false);
    }

   class FansAdapter extends RecyclerView.Adapter<FansAdapter.FansAdapter_VH>{


        @Override
        public FansAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.adapter_fans,parent,false);
            return new FansAdapter_VH(itemView);
        }

        @Override
        public void onBindViewHolder(FansAdapter_VH holder, final int position) {
            holder.fans_a_sex.setImageResource(R.drawable.man);
            holder.fans_a_follow.setImageResource(R.drawable.liaotain);
            Glide.with(getActivity()).load(AIMIApplication.dealHeadImg(mList.get(position).getImgUrl())).placeholder(R.drawable.default_icon).into(holder.fans_a_head);
            Log.e(TAG, "onBindViewHolder: "+head+mList.get(position).getImgUrl());
            holder.fans_a_name.setText(mList.get(position).getUserName());
            holder.fans_a_msg.setText(mList.get(position).getAge()+"岁"+" "+mList.get(position).getConstellation());
            if (mList.get(position).getSex() == 2){
                holder.fans_a_sex.setImageResource(R.drawable.woman);
            }else {
                holder.fans_a_sex.setImageResource(R.drawable.man);
            }
            holder.fans_a_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleChat.toSingleChat(getActivity(),mList.get(position).getUserName(),mList.get(position).getUserId(), API.CHATTYPE_SINGLE,AIMIApplication.dealHeadImg(mList.get(position).getImgUrl()));
                }
            });
            holder.fans_a_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).getUserId() != null){
                        if (mList.get(position).getUserId().equals(mSessionCache.userId)){
                            MineHomepage.toOtherHomePage(getActivity(),
                                    mList.get(position).getUserId(),
                                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),true);
                        }else {
                            OtherHomepage.toOtherHomePage(getActivity(),
                                    mList.get(position).getUserId(),
                                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),false);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        class FansAdapter_VH extends RecyclerView.ViewHolder{

            private RoundImageView fans_a_head;
            private TextView fans_a_name,fans_a_msg;
            private ImageView fans_a_follow,fans_a_sex;

            public FansAdapter_VH(View itemView) {
                super(itemView);
                fans_a_head = (RoundImageView) itemView.findViewById(R.id.fans_a_head);
                fans_a_name = (TextView) itemView.findViewById(R.id.fans_a_name);
                fans_a_msg = (TextView) itemView.findViewById(R.id.fans_a_msg);
                fans_a_follow = (ImageView) itemView.findViewById(R.id.fans_a_follow);
                fans_a_sex = (ImageView) itemView.findViewById(R.id.fans_a_sex);
            }
        }
    }

    public void getList(List<FollowsBean> list , String head){
        this.head = head;
        if (list == null || list.size() ==0){
            follows_nodata.setVisibility(View.VISIBLE);
        }else {
            follows_nodata.setVisibility(View.GONE);
            mList.clear();
            Collections.reverse(list);
            mList.addAll(list);
            mFansAdapter.notifyDataSetChanged();
        }
    }

}
