package com.yeying.aimi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.bean.ScreenOptionBean;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/13.
 */

public class BPMoneyAdapter extends RecyclerView.Adapter<BPMoneyAdapter.BPMoneyAdapter_VH> {

    private List<ScreenOptionBean> mScreenOptionBeanList;
    private Context mContext;
    private LayoutInflater inflater;
    private onItemClick mOnItemClick;

    public BPMoneyAdapter(List<ScreenOptionBean> screenOptionBeanList, Context context) {
        mScreenOptionBeanList = screenOptionBeanList;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BPMoneyAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.pop_bp_recycler,parent,false);

        return new BPMoneyAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(BPMoneyAdapter_VH holder, final int position) {
        holder.bp_container.setBackgroundResource(R.drawable.bp_money_bg2);
        if (mScreenOptionBeanList.get(position).isChoose()){
            holder.bp_container.setBackgroundResource(R.drawable.bp_money_bg1);
            holder.bp_money.setTextColor(mContext.getResources().getColor(R.color.whit));
            holder.bp_time.setTextColor(mContext.getResources().getColor(R.color.whit));
        }else {
            holder.bp_container.setBackgroundResource(R.drawable.bp_money_bg2);
            holder.bp_money.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.bp_time.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.bp_time.setText(mScreenOptionBeanList.get(position).getContinueTime()+"秒");
        holder.bp_money.setText(mScreenOptionBeanList.get(position).getDesc());
        holder.bp_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScreenOptionBeanList != null ? mScreenOptionBeanList.size() : 0;
    }

    class BPMoneyAdapter_VH extends RecyclerView.ViewHolder{

        private LinearLayout bp_container;
        private TextView bp_time,bp_money;

        public BPMoneyAdapter_VH(View itemView) {
            super(itemView);
            bp_container = (LinearLayout) itemView.findViewById(R.id.bp_container);
            bp_time = (TextView) itemView.findViewById(R.id.bp_time);
            bp_money = (TextView) itemView.findViewById(R.id.bp_money);
        }
    }

    public interface onItemClick{
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(onItemClick onItemClick){
        mOnItemClick = onItemClick;
    }

}
