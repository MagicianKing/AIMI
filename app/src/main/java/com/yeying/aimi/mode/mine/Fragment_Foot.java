package com.yeying.aimi.mode.mine;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.FootMarkBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.mode.bar_info.OutBarActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10112;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class Fragment_Foot extends BaseFragment {

    private View mView;
    private LayoutInflater inflater;
    private List<FootMarkBean> mMarkBeanList = new ArrayList<>();
    private SearchAdapter mSearchAdapter;
    private RecyclerView foot_recycler;
    private String headUrl;//前缀
    private SessionCache mSessionCache;
    private P10112 mP10112;
    private BardetailTask mBardetailTask;
    private RelativeLayout foot_nodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (mView != null){
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null){
                group.removeView(mView);
            }
            return mView;
        }
        this.inflater = inflater;
        mSessionCache = SessionCache.getInstance(getActivity());
        mView = inflater.inflate(R.layout.fragment_foot,container,false);
        initView(mView);
        return mView;

    }

    private void initView(View view) {
        foot_nodata = (RelativeLayout) view.findViewById(R.id.foot_nodata);
        foot_recycler = (RecyclerView) view.findViewById(R.id.foot_recycler);
        foot_recycler.setNestedScrollingEnabled(false);
        foot_recycler.setFocusable(false);
        foot_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchAdapter = new SearchAdapter();
        foot_recycler.setAdapter(mSearchAdapter);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapter_VH> implements View.OnClickListener {

        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f,1f);
        @Override
        public SearchAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = inflater.inflate(R.layout.adapter_bar_list,parent,false);

            return new SearchAdapter_VH(itemView);
        }

        @Override
        public void onBindViewHolder(SearchAdapter_VH holder, int position) {
            //item动画设置
            ObjectAnimator.ofPropertyValuesHolder(holder.search_parent,pvhY,pvhZ).setDuration(500).start();
            holder.search_nums.setText(mMarkBeanList.get(position).getHotNumber()+"");
            holder.search_distance.setText(mMarkBeanList.get(position).getDistance()+"km");
            holder.search_barname.setText(mMarkBeanList.get(position).getBarName());
            Glide.with(getActivity()).load(headUrl+mMarkBeanList.get(position).getPicUrl()).into(holder.search_bg);
            holder.search_parent.setTag(position);
            holder.search_parent.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return mMarkBeanList != null ?mMarkBeanList.size() : 0;
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.search_parent:
                    mP10112 = new P10112();
                    mP10112.req.sessionId = mSessionCache.sessionId;
                    mP10112.req.barId = mMarkBeanList.get(position).getBarId();
                    mP10112.req.locationX = TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX);
                    mP10112.req.locationY = TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY);
                    if(TextUtils.isEmpty(mP10112.req.barId)){
                        return;
                    }
                    mBardetailTask = new BardetailTask();
                    mBardetailTask.execute(getActivity(),mP10112);
                    break;
            }
        }

        class SearchAdapter_VH extends RecyclerView.ViewHolder{

            private ImageView search_bg;
            private TextView search_nums,search_distance,search_barname;
            private FrameLayout search_parent;

            public SearchAdapter_VH(View itemView) {
                super(itemView);
                search_bg = (ImageView) itemView.findViewById(R.id.search_bg);
                search_nums = (TextView) itemView.findViewById(R.id.search_nums);
                search_distance = (TextView) itemView.findViewById(R.id.search_distance);
                search_barname = (TextView) itemView.findViewById(R.id.search_barname);
                search_parent = (FrameLayout) itemView.findViewById(R.id.search_parent);
            }
        }
    }

    public void setMarkBeanList(List<FootMarkBean> list , String head){
        mMarkBeanList.clear();
        mMarkBeanList.addAll(list);
        mSearchAdapter.notifyDataSetChanged();
        if (mMarkBeanList.size() == 0){
            foot_nodata.setVisibility(View.VISIBLE);
        }else {
            foot_nodata.setVisibility(View.GONE);
        }
        headUrl = head;
    }

    class BardetailTask extends DefaultTask {
        PopupWindow mPopupWindow;
        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(getActivity());
            mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            P10112 p10112 = (P10112) protocol;
            Intent intent;
            if(p10112.resp.flag==1){
                intent = new Intent(getActivity(),Activity_BaPing.class);
                intent.putExtra(Activity_BaPing.DATA,p10112.resp);
                intent.putExtra(API.BAR_ID,p10112.resp.barId);
                intent.putExtra(API.BAR_NAME,p10112.resp.barName);
                intent.putExtra(API.TO_CHAT_USERID,p10112.resp.chatGroupId);
                startActivity(intent);
            }else if(p10112.resp.flag==0){
                intent = new Intent(getActivity(), OutBarActivity.class);
                intent.putExtra(OutBarActivity.OUT_DATA,p10112.resp);
                startActivity(intent);
            }

        }
    }

}
