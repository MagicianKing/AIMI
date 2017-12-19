package com.yeying.aimi.mode.photopicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;

/**
 * Created by king.
 * on 2017/9/25 23:34
 * King大人!
 */

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentAdapter_VH> {
    private Context mContext;
    private List<ImgBean> mPath;
    private LayoutInflater mLayoutInflater;
    private ItemClick mItemClick;
    private static final int ITEM_PHOTO = 1;
    private static final int ITEM_CAMERA = 0;
    private int VIEW_TYPE = 1;

    public ParentAdapter(Context context, List<ImgBean> path) {
        mContext = context;
        mPath = path;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (mPath.get(position).isFirst()){
            return 0;
        }
        return 1;
    }

    @Override
    public ParentAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ITEM_CAMERA){
            VIEW_TYPE = ITEM_CAMERA;
            itemView = mLayoutInflater.inflate(R.layout.item_parent_camera,parent,false);
        }else {
            VIEW_TYPE = ITEM_PHOTO;
            itemView = mLayoutInflater.inflate(R.layout.item_parent,parent,false);
        }

        return new ParentAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(ParentAdapter_VH holder, final int position) {
        if (holder.getItemViewType() == ITEM_CAMERA){
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onCameraClick(position);
                }
            });
        }else {
            holder.select.setImageResource(R.drawable.select);
            if (mPath.get(position).isSelectFlag()){
                holder.select.setImageResource(R.drawable.selected);
            }else {
                holder.select.setImageResource(R.drawable.select);
            }
            Glide.with(mContext)
                    .load(mPath.get(position).getFilePath())
                    .transform(new CompressTransform(mContext,mPath.get(position).getFilePath()))
                    .centerCrop()
                    .into(holder.image);
            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onSelectClick(position);
                }
            });
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onImgClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mPath == null ? 0 : mPath.size();
    }


    class ParentAdapter_VH extends RecyclerView.ViewHolder{

        private RoundImageView image;
        private ImageView select;

        public ParentAdapter_VH(View itemView) {
            super(itemView);
            if (VIEW_TYPE == ITEM_CAMERA){
                image = (RoundImageView) itemView.findViewById(R.id.image);
            }else {
                image = (RoundImageView) itemView.findViewById(R.id.image);
                select = (ImageView) itemView.findViewById(R.id.select);
            }

        }
    }

    public interface ItemClick{
        void onSelectClick(int position);
        void onImgClick(int position);
        void onCameraClick(int position);
    }

    public void setItemClick(ItemClick itemClick){
        mItemClick = itemClick;
    }

}
