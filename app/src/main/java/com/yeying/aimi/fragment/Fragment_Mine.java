package com.yeying.aimi.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.FootMarkBean;
import com.yeying.aimi.bean.MineWordsBean;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.mode.mine.FollowAndFans;
import com.yeying.aimi.mode.mine.Fragment_Foot;
import com.yeying.aimi.mode.mine.Fragment_Photo;
import com.yeying.aimi.mode.person.PersonActivity;
import com.yeying.aimi.mode.person.SetActivity;
import com.yeying.aimi.mode.wallet.WalletActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10308;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.utils.BlurTransformation;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.views.RoundImageView;
import com.yeying.aimi.views.ScrollViewPlus;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by tanchengkeji on 2017/9/13.
 */

public class Fragment_Mine extends BaseFragment implements View.OnClickListener {

    private View mView;

    private RelativeLayout mine_topbar;
    private ImageView mine_money,mine_set,mine_topphoto;
    private TextView mine_photo,mine_foot,mine_line_left,mine_line_right,mine_name,mine_fans,mine_attention;
    private RoundImageView mine_head;
    private   RoundImageView mine_edit;
    private Fragment_Photo mFragment_photo;
    private Fragment_Foot mFragment_foot;
    private FragmentManager mFragmentManager;
    private ScrollViewPlus mine_scroll;
    private RelativeLayout mine_rel;
    private SessionCache session;
    private FrameLayout top_bg;

    private List<MineWordsBean> mineWordsBeanList = new ArrayList<>();
    private List<FootMarkBean> footMarkBeanList = new ArrayList<>();

    public Fragment_Mine instance;
    private String headUrl;

    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimator1;
    private PopupWindow mProgressDialogPop;
    private long mBeforeTimeMillis = 0;
    private long mCurrentTimeMillis;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView != null){
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null){
                group.removeView(mView);
            }
            return mView;
        }
        instance = this;
        session = SessionCache.getInstance(getActivity());
        mView = inflater.inflate(R.layout.fragment_mine,container,false);
        mFragmentManager = getChildFragmentManager();
        mProgressDialogPop = PromptUtils.getProgressDialogPop(getActivity());
        initViews();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AIMIApplication.isMineFragmentShow){
            //10秒以后再请求 避免请求过多
            mCurrentTimeMillis = System.currentTimeMillis();
            if (mCurrentTimeMillis - mBeforeTimeMillis > 10*1000){
                requestData();
            }
            //requestData();
        }
    }

    private void initViews() {
        top_bg = (FrameLayout) mView.findViewById(R.id.top_bg);
        mine_rel = (RelativeLayout) mView.findViewById(R.id.mine_rel);
        mine_scroll = (ScrollViewPlus) mView.findViewById(R.id.mine_scroll);
        mine_head = (RoundImageView) mView.findViewById(R.id.mine_head);
        mine_edit = (RoundImageView) mView.findViewById(R.id.mine_edit);
        mine_name = (TextView) mView.findViewById(R.id.mine_name);
        mine_fans = (TextView) mView.findViewById(R.id.mine_fans);
        mine_attention = (TextView) mView.findViewById(R.id.mine_attention);
        mine_topbar = (RelativeLayout) mView.findViewById(R.id.mine_topbar);
        mine_money = (ImageView) mView.findViewById(R.id.mine_money);
        mine_set = (ImageView) mView.findViewById(R.id.mine_set);
        mine_topphoto = (ImageView) mView.findViewById(R.id.mine_topphoto);
        mine_photo = (TextView) mView.findViewById(R.id.mine_photo);
        mine_foot = (TextView) mView.findViewById(R.id.mine_foot);
        mine_line_left = (TextView) mView.findViewById(R.id.mine_line_left);
        mine_line_right = (TextView) mView.findViewById(R.id.mine_line_right);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mFragment_foot = new Fragment_Foot();
        mFragment_photo = new Fragment_Photo();
        fragmentTransaction.add(R.id.mine_container,mFragment_foot);
        fragmentTransaction.add(R.id.mine_container,mFragment_photo);
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(mFragment_photo);
        fragmentTransaction.commit();
        dealScrollView(mine_scroll);//处理高度变化
        mine_edit.setOnClickListener(this);
        mine_set.setOnClickListener(this);
        mine_photo.setOnClickListener(this);
        mine_foot.setOnClickListener(this);
        mine_attention.setOnClickListener(this);
        mine_fans.setOnClickListener(this);
        mine_money.setOnClickListener(this);
        mine_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.mine_photo:
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                hideColor();
                hideFragment(fragmentTransaction);
                mine_line_left.setBackgroundResource(R.drawable.line_yellow);
                if (mFragment_photo == null){
                    mFragment_photo = new Fragment_Photo();
                    fragmentTransaction.add(R.id.mine_container,mFragment_photo);
                }else {
                    fragmentTransaction.show(mFragment_photo);
                }
                fragmentTransaction.commit();
                break;
            case R.id.mine_foot:
                FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
                hideColor();
                hideFragment(fragmentTransaction1);
                mine_line_right.setBackgroundResource(R.drawable.line_yellow);
                if (mFragment_foot == null){
                    mFragment_foot = new Fragment_Foot();
                    fragmentTransaction1.add(R.id.mine_container,mFragment_foot);
                    mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                }else {
                    fragmentTransaction1.show(mFragment_foot);
                    mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                }
                fragmentTransaction1.commit();
                break;
            case R.id.mine_fans:
                FollowAndFans.toFollowFans(getActivity(),session.userId,true);
                break;
            case R.id.mine_attention:
                FollowAndFans.toFollowFans(getActivity(),session.userId,false);
                break;
            case R.id.mine_money:
                //点击进入钱包
                WalletActivity.toWallet(getActivity());
                break;
            case R.id.mine_set:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.mine_head:
            case R.id.mine_edit:
                startActivity(new Intent(getActivity(), PersonActivity.class));
                break;
        }

    }

    /**
     * 处理scrollview高度变化
     * @param scrollView
     */
    private void dealScrollView(ScrollViewPlus scrollView){
        scrollView.setLimitHeight(getViewHeight(mine_topphoto));
        scrollView.setContext(getActivity());
        scrollView.setChangeTitleListener(new ScrollViewPlus.ChangeTitle() {
            @Override
            public void changeTitle() {
                top_bg.setVisibility(View.VISIBLE);
                mObjectAnimator = ObjectAnimator.ofFloat(top_bg,"alpha",0,1);
                mObjectAnimator.setDuration(500);
                mObjectAnimator.start();
            }

            @Override
            public void resetTitle() {
                mObjectAnimator1 = ObjectAnimator.ofFloat(top_bg,"alpha",1,0);
                mObjectAnimator1.setDuration(500);
                mObjectAnimator1.start();
            }
        });
    }

    /**
     * 隐藏颜色
     */
    private void hideColor(){
        mine_line_left.setBackgroundResource(R.drawable.line_black);
        mine_line_right.setBackgroundResource(R.drawable.line_black);
    }

    /**
     * 隐藏fragment
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction){
        if (mFragment_photo != null){
            fragmentTransaction.hide(mFragment_photo);
        }
        if (mFragment_foot != null){
            fragmentTransaction.hide(mFragment_foot);
        }
    }

    /**
     * 获得view高度
     * @param view
     * @return
     */
    private int getViewHeight(View view){
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return view.getMeasuredHeight();
    }

    /**
     * 请求个人信息
     */
    public void requestData(){
        P10308 p10308 = new P10308();
        p10308.req.sessionId = session.sessionId;
        p10308.req.searchUserId = session.userId;
        p10308.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
        p10308.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
        new MineDataTask().execute(getActivity().getApplicationContext(),p10308);
    }

    /**
     * 信息请求
     */
    class MineDataTask extends DefaultTask{
        @Override
        public void preExecute() {
            super.preExecute();
            mProgressDialogPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
        }
        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            mProgressDialogPop.dismiss();
            PromptUtils.showToast(getActivity(),"网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            mProgressDialogPop.dismiss();
            mBeforeTimeMillis = System.currentTimeMillis();
            final P10308 p10308 = (P10308) protocol;
            if (p10308.resp.transcode.equals("9999")){
                if (p10308.resp.msg.contains("用户未登录")) {
                    session.phone = "";
                    session.save();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    PromptUtils.showToast(getActivity(),p10308.resp.msg);
                }
            }else {
                Glide.with(getActivity())
                        .load(AIMIApplication.dealHeadImg(p10308.resp.headUrl))
                        .transform(new CompressTransform(AIMIApplication.getContext(),AIMIApplication.dealHeadImg(p10308.resp.headUrl)))
                        .centerCrop()
                        .into(mine_head);
                Log.e("iiiii", "onOk: "+AIMIApplication.dealHeadImg(p10308.resp.headUrl));
                Glide.with(getActivity()).load(AIMIApplication.dealHeadImg(p10308.resp.headUrl))
                        .transform(new BlurTransformation(getActivity(),AIMIApplication.dealHeadImg(p10308.resp.headUrl)))
                        .override(200,200)
                        .into(mine_topphoto);
                mine_name.setText(p10308.resp.userName);
                mine_fans.setText("粉丝 "+p10308.resp.fansNum);
                mine_attention.setText("关注 "+p10308.resp.followNum);
                mineWordsBeanList = p10308.resp.words;
                Log.e(TAG, "onOk: p10308.resp.words--------->"+p10308.resp.words.size());
                footMarkBeanList = p10308.resp.footmark;
                headUrl = p10308.resp.imgUrl;
                mFragment_photo.setPhotoList(mineWordsBeanList,p10308.resp.imgUrl);
                // 保存隐私状态
                if (p10308.resp.isSeefootmark == 1){
                    session.isSeeFootMark = true;
                }else {
                    session.isSeeFootMark = false;
                }
                session.save();
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

}
