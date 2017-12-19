package com.yeying.aimi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.NightCatBean;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/15 下午4:57
 */

public class MembersAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private List<NightCatBean> mNightCatBeen;
    private Context mContext;

    public MembersAdapter(List<NightCatBean> nightCatBeen, Context context) {
        mNightCatBeen = nightCatBeen;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mNightCatBeen == null ? 0 : mNightCatBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mNightCatBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_member, parent);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        initData(viewHolder,mNightCatBeen.get(position),position);
        return convertView;
    }

    private void initData(ViewHolder viewHolder, NightCatBean nightCatBean, int position) {
        Glide.with(mContext).load(nightCatBean.getHeadImg()).into(viewHolder.mImgHead);
        viewHolder.mTvName.setText(nightCatBean.getGender()+"");
    }

    class ViewHolder {
        View view;
        RoundImageView mImgHead;
        TextView mTvName;

        ViewHolder(View view) {
            this.view = view;
            this.mImgHead = (RoundImageView) view.findViewById(R.id.img_head);
            this.mTvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
