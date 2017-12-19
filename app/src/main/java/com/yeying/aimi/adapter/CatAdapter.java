package com.yeying.aimi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.YeMaoBean;
import com.yeying.aimi.transformation.CircleTransform;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/18.
 */

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatAdapter_VH> {

    private List<YeMaoBean> mYeMaoLists;
    private Context mContext;
    private LayoutInflater inflater;
    private String headUrl;
    private HeadClick mHeadClick;

    public CatAdapter(List<YeMaoBean> yeMaoLists, Context context,String headUrl) {
        mYeMaoLists = yeMaoLists;
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.headUrl = headUrl;
    }

    @Override
    public CatAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.adapter_catlist,parent,false);

        return new CatAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(CatAdapter_VH holder, final int position) {
        Glide.with(mContext)
                .load(AIMIApplication.dealHeadImg(mYeMaoLists.get(position).getHeadImg()))
                .placeholder(R.drawable.default_icon)
                .transform(new CircleTransform(AIMIApplication.getContext(),mYeMaoLists.get(position).getHeadImg()))
                .into(holder.cat_head);
        holder.cat_name.setText(mYeMaoLists.get(position).getNickName());
        if(mYeMaoLists.get(position).getGender().equals("1")){//男
            holder.cat_name.setTextColor(Color.parseColor("#99ccff"));
        }else{
            holder.cat_name.setTextColor(Color.parseColor("#d9b4c5"));
        }
        holder.cat_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeadClick.onHeadClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mYeMaoLists != null ? mYeMaoLists.size() : 0;
    }

    class CatAdapter_VH extends RecyclerView.ViewHolder{

        private ImageView cat_head;
        private TextView cat_name;

        public CatAdapter_VH(View itemView) {
            super(itemView);
            cat_head = (ImageView) itemView.findViewById(R.id.cat_head);
            cat_name = (TextView) itemView.findViewById(R.id.cat_name);
        }
    }

    public interface HeadClick{
        void onHeadClick(int position);
    }

    public void setHeadClickListener(HeadClick headClickListener){
        mHeadClick = headClickListener;
    }
}
