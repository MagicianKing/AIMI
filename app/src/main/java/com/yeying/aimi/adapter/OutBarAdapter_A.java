package com.yeying.aimi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.OutBean;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/11.
 */

public class OutBarAdapter_A extends RecyclerView.Adapter<OutBarAdapter_A.OutBarAdapter_A_VH> {

    private static final String TAG = "OutBarAdapter_A";

    private Context mContext;
    private List<OutBean> mStringList;
    private LayoutInflater inflater;
    private int TYPE = 0;
    private static final int TYPE_PIC = 0 ;
    private static final int TYPE_NIGHT = 1;
    private String imgUrl;
    private ItemClick mItemClick;

    public OutBarAdapter_A(Context context, List<OutBean> stringList , String imgUrl) {
        mContext = context;
        mStringList = stringList;
        this.imgUrl = imgUrl;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public OutBarAdapter_A_VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE == TYPE_NIGHT){
            view = inflater.inflate(R.layout.item_circlehead,parent,false);
        }else {
            view = inflater.inflate(R.layout.item_dynamic,parent,false);
        }
        return new OutBarAdapter_A_VH(view);
    }

    @Override
    public void onBindViewHolder(OutBarAdapter_A_VH holder, final int position) {
        if (TYPE == TYPE_NIGHT){
            Glide.with(mContext).load(AIMIApplication.dealHeadImg(mStringList.get(position).getImgUrl())).placeholder(R.drawable.default_icon).into(holder.mRoundImageView);
            /*holder.mRoundImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onCatClick(position);
                }
            });*/
        }else {
            Glide.with(mContext).load(AIMIApplication.dealHeadImg(mStringList.get(position).getImgUrl())).placeholder(R.drawable.default_icon).into(holder.mImageView);
            Log.e(TAG, "imgUrl+mStringList.get(position).getImgUrl()-----> "+imgUrl+mStringList.get(position).getImgUrl());
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onDynamicClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStringList == null ? 0 : mStringList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mStringList.get(position).isType()){
            TYPE = TYPE_NIGHT;
        }else {
            TYPE = TYPE_PIC;
        }
        return TYPE;
    }

    class OutBarAdapter_A_VH extends RecyclerView.ViewHolder{

        private RoundImageView mRoundImageView;
        private ImageView mImageView;

        public OutBarAdapter_A_VH(View itemView) {
            super(itemView);
            if (TYPE == TYPE_NIGHT){
                mRoundImageView = (RoundImageView) itemView.findViewById(R.id.out_circle);
            }else {
                mImageView = (ImageView) itemView.findViewById(R.id.out_rect);
            }
        }
    }

    public interface ItemClick{
        void onCatClick(int psn);
        void onDynamicClick(int psn);
    }

    public void setItemClickListener(ItemClick itemClickListener){
        mItemClick = itemClickListener;
    }

}
