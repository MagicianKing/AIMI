package com.yeying.aimi.mode.mine;

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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.FansBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10150;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.views.RoundImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class Fragment_Fans extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "Fragment_Fans";

    private View mView;
    private RecyclerView mTargetSwipe;
    private SwipeToLoadLayout mSwipeFans;
    private List<FansBean> mList = new ArrayList<>();
    private FansAdapter mFansAdapter;
    private LayoutInflater inflater;
    private String head;
    private RelativeLayout fans_nodata;
    private SessionCache session;
    private int isAttention;

    private View view_guanzhu;
    private PopupWindow popupWindow_guanzhu;
    private String friendId;
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
        session = SessionCache.getInstance(getActivity());
        mView = inflater.inflate(R.layout.fragment_fans, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View itemView) {
        fans_nodata = (RelativeLayout) itemView.findViewById(R.id.fans_nodata);
        mTargetSwipe = (RecyclerView) itemView.findViewById(R.id.swipe_target);
        mSwipeFans = (SwipeToLoadLayout) itemView.findViewById(R.id.fans_swipe);
        mSwipeFans.setLoadMoreEnabled(false);
        mSwipeFans.setRefreshEnabled(false);
        mTargetSwipe.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFansAdapter = new FansAdapter();
        mTargetSwipe.setAdapter(mFansAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_close:
                if (popupWindow_guanzhu != null && popupWindow_guanzhu.isShowing()){
                    popupWindow_guanzhu.dismiss();
                }
                break;
            case R.id.bt_sure:
                if (popupWindow_guanzhu != null && popupWindow_guanzhu.isShowing()){
                    popupWindow_guanzhu.dismiss();
                }
                addNotice(friendId,2);
                break;
        }

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
            holder.fans_a_follow.setImageResource(R.drawable.follow);
            Glide.with(getActivity()).load(AIMIApplication.dealHeadImg(mList.get(position).getImgUrl())).placeholder(R.drawable.default_icon).into(holder.fans_a_head);
            Log.e(TAG, "BindViewHolder: "+head+mList.get(position).getImgUrl());
            holder.fans_a_name.setText(mList.get(position).getUserName());
            holder.fans_a_msg.setText(mList.get(position).getAge()+"岁"+" "+mList.get(position).getConstellation());
            if (mList.get(position).getSex() == 2){
                holder.fans_a_sex.setImageResource(R.drawable.woman);
            }else {
                holder.fans_a_sex.setImageResource(R.drawable.man);
            }
            if (mList.get(position).getIsFriend() == 1){
                holder.fans_a_follow.setImageResource(R.drawable.huxiangguanzhu);
            }else {
                holder.fans_a_follow.setImageResource(R.drawable.follow);
            }

            holder.fans_a_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).getIsFriend() == 1){
                        // 已经互相关注，点击取消关注
                        createPopWindow_guanzhu();
                        friendId = mList.get(position).getUserId();
                    }else {
                        //点击关注
                        addNotice(mList.get(position).getUserId(),1);
                    }
                }
            });
            holder.fans_a_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).getUserId() != null){
                        if (mList.get(position).getUserId().equals(mSessionCache.userId)){
                            MineHomepage.toOtherHomePage(getActivity(),
                                    mList.get(position).getUserId(),
                                    TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),
                                    TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),true);
                        }else {
                            OtherHomepage.toOtherHomePage(getActivity(),
                                    mList.get(position).getUserId(),
                                    TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),
                                    TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),false);
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

    public void getList(List<FansBean> list,String head){
        this.head = head;
        if (list == null || list.size() == 0){
            fans_nodata.setVisibility(View.VISIBLE);
        }else {
            fans_nodata.setVisibility(View.GONE);
            mList.clear();
            Collections.reverse(list);
            mList.addAll(list);
            mFansAdapter.notifyDataSetChanged();
        }
    }

    private void createPopWindow_guanzhu(){
        if (view_guanzhu==null){
            view_guanzhu= LayoutInflater.from(getActivity()).inflate(R.layout.del_msg_window,null,false);
            TextView tv_msg= (TextView) view_guanzhu.findViewById(R.id.tv_msg);
            TextView bt_close= (TextView) view_guanzhu.findViewById(R.id.bt_close);
            TextView bt_sure= (TextView) view_guanzhu.findViewById(R.id.bt_sure);
            tv_msg.setText("确认不再关注TA吗？");
            bt_close.setText("取消");
            bt_sure.setText("确定");
            tv_msg.setOnClickListener(this);
            bt_sure.setOnClickListener(this);
            bt_close.setOnClickListener(this);
        }
        if (popupWindow_guanzhu==null){
            popupWindow_guanzhu=new PopupWindow(view_guanzhu, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        popupWindow_guanzhu.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
    }

    public void addNotice(String friendId,int type) {
        if (friendId != null) {
            P10150 p10150 = new P10150();
            p10150.req.sessionId = session.sessionId;
            p10150.req.transcode = "10150";
            p10150.req.type = type;
            p10150.req.friendId = friendId;
            new addNoteTask().execute(getActivity().getApplicationContext(), p10150);
        }

    }

    public class addNoteTask extends DefaultTask {
        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10150 p10150 = (P10150) protocol;
            if (p10150.resp.transcode.equals("9999")) {
                PromptUtils.showToast(getActivity(), p10150.resp.msg);
            } else {
                isAttention = p10150.resp.isAttention;
                if (p10150.resp.state == 1) {
                    if (popupWindow_guanzhu!=null&&popupWindow_guanzhu.isShowing()){
                        popupWindow_guanzhu.dismiss();
                    }
                    //关注成功 回调activity的方法 进行刷新
                    ((FollowAndFans)getActivity()).requestFollowsFans();
                } else {
                    //关注失败
                }
            }
        }
    }
}
