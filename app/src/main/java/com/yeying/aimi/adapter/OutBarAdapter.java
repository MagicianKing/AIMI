package com.yeying.aimi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.BarUserPicList;
import com.yeying.aimi.bean.YeMaoList;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;


/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/19 上午10:56
 * 店外适配器(夜猫列表和动态列表)
 */

public class OutBarAdapter extends RecyclerView.Adapter<OutBarAdapter.ViewHolder> {
    public static final int NIGHTCAT_LIST = 0;
    public static final int DYNAMIC_LIST = 1;
    private final String imgurl;
    private List<Object> mObjects;
    private int type;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    /**
     *
     * @param context 上下文
     * @param objects 集合
     * @param type 类型
     * @param imgurl 图片前缀地址
     */
    public OutBarAdapter(Context context,List<Object> objects, int type,String imgurl) {
        mObjects = objects;
        this.type = type;
        mContext = context;
        this.imgurl=imgurl;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = null;
        if(type==NIGHTCAT_LIST){
           view =  mLayoutInflater.inflate(R.layout.item_circlehead,parent);
        }else if(type==DYNAMIC_LIST){
            view =  mLayoutInflater.inflate(R.layout.item_dynamic,parent);
        }
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(type == NIGHTCAT_LIST){
            YeMaoList yeMaoList = (YeMaoList) mObjects.get(position);
            Glide.with(mContext).load(imgurl+yeMaoList.getHeadImg()).into(holder.mRoundImageView);
        }else if(type==DYNAMIC_LIST){
            BarUserPicList barUserPicList = (BarUserPicList) mObjects.get(position);
            Glide.with(mContext).load(imgurl+barUserPicList.getPicUrl()).into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.isEmpty()?0:mObjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RoundImageView mRoundImageView;
        private ImageView mImageView;
        public ViewHolder(View itemView) {
        super(itemView);
            if(type==NIGHTCAT_LIST){
               mRoundImageView = (RoundImageView) itemView.findViewById(R.id.out_circle);
            }else if(type==DYNAMIC_LIST){
                mImageView = (ImageView) itemView.findViewById(R.id.out_rect);
            }
        }
    }
}
