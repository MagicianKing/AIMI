package com.yeying.aimi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.ReplyBean;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.views.RoundImageView;
import com.yeying.aimi.views.TextViewPlus;
import com.yeying.aimi.views.TextViewPlus_Rel;

import java.util.List;


public class MyDetailsAdapter extends BaseAdapter {
    private static final int TYPE = 1;
    private List<ReplyBean> list;
    private Context context;
    private String imageurl;
    private SessionCache mSessionCache;

    public MyDetailsAdapter(List<ReplyBean> list, Context context, String imageurl) {
        super();
        this.list = list;
        this.context = context;
        this.imageurl = imageurl;
        mSessionCache = SessionCache.getInstance(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int arg0, View contextView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        MyViewHolder vHolder;
        if (contextView == null) {
            contextView = View.inflate(context, R.layout.details, null);
            vHolder = new MyViewHolder();
            vHolder.headImage = (RoundImageView) contextView.findViewById(R.id.details_head_xml);
            vHolder.content = (TextViewPlus) contextView.findViewById(R.id.details_conten_xml);
            vHolder.date = (TextViewPlus_Rel) contextView.findViewById(R.id.details_date_xml);
            vHolder.title = (TextViewPlus_Rel) contextView.findViewById(R.id.details_title_xml);
            contextView.setTag(vHolder);

        } else {
            vHolder = (MyViewHolder) contextView.getTag();
        }
        vHolder.content.setText(list.get(arg0).getContent());
        String dateString = API.getDateStr(list.get(arg0).getReply_time());
        vHolder.date.setText(dateString);
        vHolder.title.setText(list.get(arg0).getUser_name());
        Glide.with(context).load(AIMIApplication.dealHeadImg(list.get(arg0).getUser_url())).into(vHolder.headImage);
        vHolder.headImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (list.get(arg0).getUser_id() != null){
                    if (list.get(arg0).getUser_id().equals(mSessionCache.userId)){
                        MineHomepage.toOtherHomePage(context,list.get(arg0).getUser_id(), TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),true);
                    }else {
                        OtherHomepage.toOtherHomePage(context,list.get(arg0).getUser_id(),TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY),false);
                    }
                }

            }
        });
        return contextView;
    }

    class MyViewHolder {
        RoundImageView headImage;
        TextViewPlus_Rel title;
        TextViewPlus content;
        TextViewPlus_Rel date;
    }
}
