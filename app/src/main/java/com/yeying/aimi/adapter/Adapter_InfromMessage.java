package com.yeying.aimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/8/14.
 */

public class Adapter_InfromMessage extends RecyclerView.Adapter<Adapter_InfromMessage.Adapter_InfromMessage_VH> {

    private List<ReplyCacheBean> list;
    private Context context;
    private LayoutInflater inflater;

    private Activity activity;

    private ItemClickListener itemClickListener;

    private SpannableString spannableString = null;
    private SessionCache mSessionCache;


    public Adapter_InfromMessage(List<ReplyCacheBean> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
        this.activity=activity;
        mSessionCache = SessionCache.getInstance(context);
    }

    @Override
    public Adapter_InfromMessage_VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=inflater.inflate(R.layout.inform_layout,parent,false);
        return new Adapter_InfromMessage_VH(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_InfromMessage_VH holder, final int position) {
        final ReplyCacheBean bean = list.get(position);
        holder.message_img_head.setVisibility(View.GONE);
        holder.message_img_right.setVisibility(View.GONE);
        holder.message_name.setVisibility(View.GONE);
        holder.message_content.setVisibility(View.GONE);
        holder.message_fatecard.setVisibility(View.GONE);
        if (bean.getTypeInt()==1){//关注
            holder.message_img_head.setVisibility(View.VISIBLE);
           /* RequestOptions options = new RequestOptions()*/

            Glide.with(context)
                    .load(dealCMDHead(AIMIApplication.dealHeadImg(bean.getUserHeadImg())))
                    .placeholder(R.drawable.default_icon)
                    .transform(new CompressTransform(context , bean.getUserHeadImg()))
                    .centerCrop()
                    .error(R.drawable.default_icon)
                    .into(holder.message_img_head);
            Log.e("yyyy", "onBindViewHolder: "+dealCMDHead(AIMIApplication.dealHeadImg(bean.getUserHeadImg())));
            holder.message_name.setVisibility(View.VISIBLE);
            holder.message_name.setText(bean.getUserName());
            holder.message_content.setVisibility(View.VISIBLE);
            holder.message_content.setText("关注了你");
            holder.message_time.setVisibility(View.VISIBLE);
            holder.message_time.setText(AIMIApplication.dealTime(bean.getUserTime()));
        }else if (bean.getTypeInt()==4){//抢到相同的缘分牌
            holder.message_img_head.setVisibility(View.VISIBLE);
            holder.message_img_head.setImageResource(R.drawable.fatecard_img);
            holder.message_fatecard.setVisibility(View.VISIBLE);
            holder.message_fatecard.setText("遇见 TA！");
            holder.message_time.setVisibility(View.VISIBLE);
            holder.message_time.setText(AIMIApplication.dealTime(bean.getTheMsgTime()));
        }else if (bean.getTypeInt()==5){//缘分牌配对结果
            holder.message_img_head.setVisibility(View.VISIBLE);
            holder.message_img_head.setImageResource(R.drawable.fatecard_img);
            holder.message_fatecard.setVisibility(View.VISIBLE);
            holder.message_fatecard.setText("缘分牌配对成功");
            holder.message_time.setVisibility(View.VISIBLE);
            holder.message_time.setText(AIMIApplication.dealTime(bean.getTheMsgTime()));
        }else if (bean.getTypeInt()==6){//缘分牌每轮结束匹配失败消息
            holder.message_img_head.setVisibility(View.VISIBLE);
            holder.message_img_head.setImageResource(R.drawable.fatecard_img);
            holder.message_name.setVisibility(View.VISIBLE);
            holder.message_name.setText("缘分牌到手，坐等有缘人");
            holder.message_content.setVisibility(View.VISIBLE);
            holder.message_content.setText("活动结束");
            holder.message_time.setVisibility(View.VISIBLE);
            holder.message_time.setText(AIMIApplication.dealTime(bean.getTheMsgTime()));
        }else if (bean.getTypeInt()==7){//霸屏排行榜
            holder.message_img_head.setVisibility(View.VISIBLE);
            holder.message_img_head.setImageResource(R.drawable.rank_baping);
            holder.message_name.setVisibility(View.VISIBLE);
            holder.message_name.setMaxLines(2);
            holder.message_name.setText("今晚霸屏玩的嗨，您夺得今夜霸屏榜第"+bean.getRank()+"名");
            holder.message_time.setVisibility(View.VISIBLE);
            holder.message_time.setText(AIMIApplication.dealTime(bean.getTheMsgTime()));
        }else {
            holder.message_img_head.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(dealCMDHead(AIMIApplication.dealHeadImg(bean.getImg())))
                    .placeholder(R.drawable.default_icon)
                    .transform(new CompressTransform(context , bean.getImg()))
                    .centerCrop()
                    .error(R.drawable.default_icon)
                    .into(holder.message_img_head);
            holder.message_img_right.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(AIMIApplication.dealHeadImg(bean.getPic_img()))
                    .placeholder(R.drawable.default_icon)
                    .transform(new CompressTransform(context , bean.getPic_img()))
                    .centerCrop()
                    .error(R.drawable.default_icon)
                    .into(holder.message_img_right);
            if (bean.getType()==1){
                holder.message_fatecard.setVisibility(View.GONE);
                holder.message_name.setVisibility(View.VISIBLE);
                holder.message_name.setText(bean.getName());
                holder.message_time.setVisibility(View.GONE);
                holder.message_content.setVisibility(View.VISIBLE);
                holder.message_content.setText("赞了你");
            }else{
                spannableString = new SpannableString(bean.getName()+" 回复");
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.grey)),bean.getName().length(),spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new RelativeSizeSpan(0.9f),bean.getName().length(),spannableString.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.message_fatecard.setVisibility(View.GONE);
                holder.message_name.setVisibility(View.VISIBLE);
                holder.message_name.setText(spannableString);
                holder.message_content.setVisibility(View.VISIBLE);
                holder.message_content.setText(bean.getContent());
                holder.message_time.setVisibility(View.GONE);
            }
        }
        holder.message_img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onHeadImgClick(position);
            }
        });
        holder.message_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemViewClick(position);
            }
        });
        holder.message_rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemLongClick(position);
                return true;
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class Adapter_InfromMessage_VH extends RecyclerView.ViewHolder{

        private RoundImageView message_img_head,message_img_right;
        private TextView message_name,message_content,message_fatecard,message_time;
        private RelativeLayout message_rl;

        public Adapter_InfromMessage_VH(View itemView) {
            super(itemView);
            message_img_head= (RoundImageView) itemView.findViewById(R.id.message_img_head);
            message_img_right= (RoundImageView) itemView.findViewById(R.id.message_img_right);
            message_name= (TextView) itemView.findViewById(R.id.message_name);
            message_content= (TextView) itemView.findViewById(R.id.message_content);
            message_fatecard= (TextView) itemView.findViewById(R.id.message_fatecard);
            message_time= (TextView) itemView.findViewById(R.id.message_time);
            message_rl= (RelativeLayout) itemView.findViewById(R.id.message_rl);

        }
    }

    public interface ItemClickListener{
        void onItemViewClick(int psn);
        void onHeadImgClick(int psn);
        void onItemLongClick(int psn);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    private String dealCMDHead(String headUrl){
        if (headUrl.contains("q.qlogo") || headUrl.contains("sinaimg") || headUrl.contains("wx.qlogo")){
            headUrl = headUrl.substring(mSessionCache.imgUrl.length() , headUrl.length());
        }
        return headUrl;
    }
}
