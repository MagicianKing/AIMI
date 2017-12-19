package com.yeying.aimi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.YeMaoList;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;


/**
 * Created by tanchengkeji on 2017/9/11.
 */

public class BPHeadAdapter extends RecyclerView.Adapter<BPHeadAdapter.BPHeadAdapter_VH> {

    private static final String TAG = "BPHeadAdapter";

    private Context mContext;
    private List<YeMaoList> mList;
    private LayoutInflater inflater;
    private String headUrl;
    private HeadClick mHeadClick;
    private Animation mAnimation;

    public BPHeadAdapter(Context context, List<YeMaoList> list , String headUrl) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(context);
        this.headUrl = headUrl;
    }

    @Override
    public BPHeadAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.adapter_bp_head,parent,false);

        return new BPHeadAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(BPHeadAdapter_VH holder, final int position) {
        Log.e(TAG, "onBindViewHolder: "+position);
        Glide.with(mContext).load(AIMIApplication.dealHeadImg(mList.get(position).getHeadImg()))
                .error(R.drawable.default_icon)
                .placeholder(R.drawable.default_icon)
                .transform(new CompressTransform(mContext,headUrl+mList.get(position).getHeadImg()))
                .centerCrop()
                .into(holder.bp_head);
        Log.e(TAG, "onBindViewHolder: "+mList.get(position).getHeadImg());
        holder.bp_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeadClick.onHeadClick(position);
            }
        });
        if(position==0||position==1||position==2){
            Log.e(TAG, "---------: "+position);
            try {
                mAnimation = AnimationUtils.loadAnimation(mContext , R.anim.head_rotate);
                mAnimation.setInterpolator(new LinearInterpolator());
                holder.bp_head_bg.startAnimation(mAnimation);
                /*holder.bp_head_bg.setForeground(mContext.getResources().getDrawable(R.drawable.anim_head));
                AnimationDrawable drawable = (AnimationDrawable) holder.bp_head_bg.getForeground();
                drawable.setOneShot(false);
                drawable.start();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            holder.bp_head_bg.setVisibility(View.GONE);
        }


    }

    @Override
    public void onViewAttachedToWindow(BPHeadAdapter_VH holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if(position==0||position==1||position==2){
            Log.e(TAG, "---------: "+position);
            try {
                mAnimation = AnimationUtils.loadAnimation(mContext , R.anim.head_rotate);
                mAnimation.setInterpolator(new LinearInterpolator());
                holder.bp_head_bg.startAnimation(mAnimation);
                /*holder.bp_head_bg.setForeground(mContext.getResources().getDrawable(R.drawable.anim_head));
                AnimationDrawable drawable = (AnimationDrawable) holder.bp_head_bg.getForeground();
                drawable.setOneShot(false);
                drawable.start();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            holder.bp_head_bg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewDetachedFromWindow(BPHeadAdapter_VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.bp_head_bg.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    class BPHeadAdapter_VH extends RecyclerView.ViewHolder{

        private ImageView bp_head_bg;
        private RoundImageView bp_head;

        public BPHeadAdapter_VH(View itemView) {
            super(itemView);
            bp_head_bg = (ImageView) itemView.findViewById(R.id.bp_head_bg);
            bp_head = (RoundImageView) itemView.findViewById(R.id.bp_head);
        }
    }



    public interface HeadClick{
        void onHeadClick(int position);
    }

    public void setOnHeadClickListener(HeadClick headClickListener){
        mHeadClick = headClickListener;
    }
}
