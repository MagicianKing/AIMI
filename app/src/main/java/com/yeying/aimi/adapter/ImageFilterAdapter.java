package com.yeying.aimi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yeying.aimi.R;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;


/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/26 上午10:40
 */

public class ImageFilterAdapter implements View.OnClickListener {
    private Context mContext;
    private List<GPUImageFilter> mGPUImageFilters;
    private Bitmap mBitmap;
    private  LayoutInflater mLayoutInflater;
    private HorizontalScrollView mScrollView;
    private  View mView;
    private  ViewHolder mViewHolder;

    public ImageFilterAdapter(Context context, List<GPUImageFilter> GPUImageFilters, Bitmap bitmap, HorizontalScrollView view) {
        mContext = context;
        mGPUImageFilters = GPUImageFilters;
        mBitmap = bitmap;
        mScrollView = view;
        mLayoutInflater = LayoutInflater.from(context);

        bindData();
    }
    private void bindData(){
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mScrollView.addView(linearLayout);
        for (int i = 0; i < getCount(); i++) {
            mView = mLayoutInflater.inflate(R.layout.item_imagefilter, null, false);
            linearLayout.addView(mView);
           View layout = linearLayout.getChildAt(i);
            layout.setOnClickListener(this);
            layout.setTag(i);
            mViewHolder = new ViewHolder(layout);
            mViewHolder.mSurfaceView.setImage(mBitmap);
            mViewHolder.mSurfaceView.setFilter(mGPUImageFilters.get(i));
        }
    }

    private int getCount(){
        return mGPUImageFilters==null?0:mGPUImageFilters.size();
    }

    @Override
    public void onClick(View v) {
        onItemClickLisener.itemClickListener((Integer) v.getTag());
    }
    private onItemClickLisener onItemClickLisener;

    public interface onItemClickLisener{
        void itemClickListener(int position);
    }
    public void setOnItemClickLisener(onItemClickLisener onItemClickLisener){
        this.onItemClickLisener = onItemClickLisener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private  GPUImageView mSurfaceView;

        public ViewHolder(View itemView) {
            super(itemView);
            mSurfaceView = (GPUImageView) itemView.findViewById(R.id.sf_preview);
        }
    }
}
