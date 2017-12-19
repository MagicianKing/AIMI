package com.yeying.aimi.mode.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.MineWordsBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.dynamics_detail.DynamicsDetail;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.views.RoundImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class Fragment_Photo extends BaseFragment {

    private View mView;
    private RecyclerView photo_recycler;
    private LayoutInflater inflater;
    private PhotoF_Adapter mPhotoF_adapter;
    private List<MineWordsBean> mMineWordsBeanList = new ArrayList<>();
    private String head;
    private RelativeLayout photo_nodata;

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
        mView = inflater.inflate(R.layout.fragment_photo,container,false);
        initViews();
        return mView;

    }

    private void initViews() {
        photo_nodata = (RelativeLayout) mView.findViewById(R.id.photo_nodata);
        photo_recycler = (RecyclerView) mView.findViewById(R.id.photo_recycler);
        photo_recycler.setNestedScrollingEnabled(false);
        photo_recycler.setFocusable(false);
        photo_recycler.setLayoutManager(new GridLayoutManager(getActivity(),4));
        mPhotoF_adapter = new PhotoF_Adapter();
        photo_recycler.setAdapter(mPhotoF_adapter);
    }


    class PhotoF_Adapter extends RecyclerView.Adapter<PhotoF_Adapter.PhotoF_Adapter_VH>{

        Intent mIntent = new Intent(getActivity(), DynamicsDetail.class);

        @Override
        public PhotoF_Adapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.adapter_mine,parent,false);
            PhotoF_Adapter_VH vh = new PhotoF_Adapter_VH(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(PhotoF_Adapter_VH holder, final int position) {
            Glide.with(getActivity())
                    .load(AIMIApplication.dealHeadImg(mMineWordsBeanList.get(position).getPic_urls().get(0).getPic_url()))
                    .placeholder(R.drawable.default_icon)
                    .transform(new CompressTransform(AIMIApplication.getContext() , mMineWordsBeanList.get(position).getPic_urls().get(0).getPic_url()))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(holder.imageViewWeakReference);
            holder.imageViewWeakReference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicsDetail.toDynamics(getActivity(),
                            mMineWordsBeanList.get(position).getMessage_id(),
                            mMineWordsBeanList.get(position).getMessage(),
                            AIMIApplication.dealHeadImg(mMineWordsBeanList.get(position).getPic_urls().get(0).getPic_url()),
                            0,0,"");
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMineWordsBeanList != null ? mMineWordsBeanList.size() : 0;
        }

        class PhotoF_Adapter_VH extends RecyclerView.ViewHolder{

            private RoundImageView imageViewWeakReference;

            public PhotoF_Adapter_VH(View itemView) {
                super(itemView);
                RoundImageView photo_photo = (RoundImageView) itemView.findViewById(R.id.photo_photo);
               imageViewWeakReference = (RoundImageView) new WeakReference<RoundImageView>(photo_photo).get();

            }
        }
    }

    public void setPhotoList(List<MineWordsBean> list,String tag){
        mMineWordsBeanList.clear();
        mMineWordsBeanList.addAll(list);
        mPhotoF_adapter.notifyDataSetChanged();
        if (mMineWordsBeanList.size() == 0){
            photo_nodata.setVisibility(View.VISIBLE);
        }else {
            photo_nodata.setVisibility(View.GONE);
        }
        Log.e(TAG, "setPhotoList:mMineWordsBeanList.size()------> "+mMineWordsBeanList.size());
        head = tag;
    }

    private float getViewWidth(View view){
        int width = View.MeasureSpec.makeMeasureSpec(0 , View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0 , View.MeasureSpec.UNSPECIFIED);
        view.measure(width , height);
        return view.getMeasuredWidth();
    }

    private int dp2px(int dpValue){
        float density = getActivity().getResources().getDisplayMetrics().density;
        return (int) (density*dpValue+0.5);
    }

}
