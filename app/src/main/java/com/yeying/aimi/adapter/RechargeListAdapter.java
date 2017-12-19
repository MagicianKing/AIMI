package com.yeying.aimi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.bean.ChargeListBean;

import java.util.List;

public class RechargeListAdapter extends BaseAdapter {

    private Context context;
    private List<ChargeListBean> list;
    private LayoutInflater inflater;
    private StringBuilder mStringBuilder;

    public RechargeListAdapter(Context context, List<ChargeListBean> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public ChargeListBean getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup partent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.item_recharge_layout, partent, false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }
        holder.itemLayout.setBackgroundResource(R.drawable.bag_recharge_white_style);
        if (list.get(position).isChecked()) {
            holder.itemLayout.setBackgroundResource(R.drawable.yellow_light_bg);
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.bag_recharge_white_style);
        }
        holder.maoTv.setText(list.get(position).getsNumber() + "猫币");
        mStringBuilder = new StringBuilder(list.get(position).getsMoney());
        //holder.moneyTv.setText(mStringBuilder.substring(0,list.get(position).getsMoney().length()-2) + "元");
        StringBuilder stringBuilder = new StringBuilder(list.get(position).getsMoney());
        int i = stringBuilder.lastIndexOf(".");
        String front = stringBuilder.substring(0,i);
        String behind = stringBuilder.substring(i,stringBuilder.length());
        if (behind.length()>2){
            holder.moneyTv.setText(stringBuilder);
        }else {
            holder.moneyTv.setText(front + "元");
        }
        //holder.moneyTv.setText(Integer.valueOf(list.get(position).getsMoney()) + "元");
        return itemView;
    }

    public void setChecked(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setChecked(true);
            } else {
                list.get(i).setChecked(false);
            }
        }
    }

    public void addData(List<ChargeListBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView maoTv;
        private TextView moneyTv;
        private RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            maoTv = (TextView) itemView.findViewById(R.id.recharge_mao_text);
            moneyTv = (TextView) itemView.findViewById(R.id.recharge_money_text);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_recharge_layout);
        }
    }

}
