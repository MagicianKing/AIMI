package com.yeying.aimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.BarScreenBean;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P23104;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;


/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/25 下午5:32
 */

public class PermissionAdapter extends BaseAdapter implements View.OnClickListener {
    private  LayoutInflater mLayoutInflater;
    private List<BarScreenBean> mList;
    private Context mContext;
    private String imgUrl;
    private  BarScreenDeleteTask mBarScreenDeleteTask;
    private P23104 mP23104;
    private SessionCache mSessionCache;
    private int mPosition;

    public PermissionAdapter(List<BarScreenBean> list, Context context, String imgUrl) {
        mList = list;
        mContext = context;
        this.imgUrl = imgUrl;
        mLayoutInflater = LayoutInflater.from(context);
        mBarScreenDeleteTask = new BarScreenDeleteTask();
        mSessionCache = SessionCache.getInstance(context);
        initDelete();
    }

    private void initDelete() {
        mP23104 = new P23104();
        mP23104.req.sessionId = mSessionCache.sessionId;
        mP23104.req.barId = mSessionCache.barId;
        mP23104.req.userId = mSessionCache.userId;

    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        BarScreenBean bean = mList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_permission, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        initData((ViewHolder) convertView.getTag(), bean, position);

        return convertView;
    }

    private void initData(ViewHolder viewHolder, BarScreenBean bean, int position) {
        viewHolder.mContent.setText(bean.getContent() + "");
        viewHolder.mDelete.setTag(position);
        viewHolder.mName.setText(bean.getNickName() + "");
        viewHolder.mContent.setText(bean.getContent() + "");
        if(bean.getPlayStatus()==0){
            viewHolder.mDelete.setEnabled(true);
        }else if(bean.getPlayStatus()==1){
            PromptUtils.showToast((Activity) mContext,"该霸屏已播放完成");
            viewHolder.mDelete.setEnabled(false);
        }else{
            PromptUtils.showToast((Activity) mContext,"该霸屏已删除");
            viewHolder.mDelete.setEnabled(false);
        }
        viewHolder.mDelete.setOnClickListener(this);
        Glide.with(mContext).load(imgUrl+bean.getHeadImg()).into(viewHolder.mHead);
        Glide.with(mContext).load(imgUrl+bean.getPicUrl()).into(viewHolder.mRoundImageView);
    }

    @Override
    public void onClick(View v) {
        this.notifyDataSetChanged();
        mPosition = (int) v.getTag();
        String id = mList.get(mPosition).getScreenOrderId();
        mP23104.req.screenOrderId = id;
        if(!TextUtils.isEmpty(id)){
            mBarScreenDeleteTask.execute(mContext,mP23104);
        }

    }


    class ViewHolder {

        private final RoundImageView mHead;
        private final RoundImageView mRoundImageView;
        private final Button mDelete;
        private final TextView mName;
        private final TextView mContent;


        public ViewHolder(View view) {
            mHead = (RoundImageView) view.findViewById(R.id.img_head);
            mRoundImageView = (RoundImageView) view.findViewById(R.id.img_barscreen);
            mDelete = (Button) view.findViewById(R.id.btn_delete);
            mName = (TextView) view.findViewById(R.id.tv_name);
            mContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
    private class BarScreenDeleteTask extends DefaultTask{
        @Override
        public void preExecute() {
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P23104 p23104 = (P23104) protocol;
            int status = p23104.resp.status;
            if(status==0){
                PromptUtils.showToast((Activity) mContext,"删除失败");

            }else{
                PromptUtils.showToast((Activity) mContext,"删除成功");
                mList.remove(mPosition);
            }
            notifyDataSetChanged();
        }
    }
}
