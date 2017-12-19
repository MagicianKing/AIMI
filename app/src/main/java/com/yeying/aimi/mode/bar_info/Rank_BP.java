package com.yeying.aimi.mode.bar_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.GameTopBean;
import com.yeying.aimi.bean.TopUserBean;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P640000;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/20.
 */

public class Rank_BP extends BaseActivity implements View.OnClickListener {

    public static final String RANK_BAR_ID = "rank_bar_id";

    private ImageView mLeftViewTitle;
    private RelativeLayout mLeftTitle;
    private TextView mCenterViewTitle;
    private ImageView mRightImgTitle;
    private TextView mRightTvTitle;
    private RelativeLayout mRightTitle;
    private TextView mTwoMoneyRank;
    private TextView mTwoNameRank;
    private TextView mOneMoneyRank;
    private TextView mOneNameRank;
    private TextView mThreeMoneyRank;
    private TextView mThreeNameRank;
    private RecyclerView mListRank;
    private RoundImageView mTwoHeadRank;
    private RoundImageView mOneHeadRank;
    private RoundImageView mThreeHeadRank;
    private RankAdapter mRankAdapter;
    private SessionCache mSessionCache;
    private RequestManager mWith;
    private String barId;
    private PopupWindow mPopupWindow;
    private List<TopUserBean> mTopUserBeanList = new ArrayList<>();//后七名的list集合
    private String rank_oneId;
    private String rank_twoId;
    private String rank_threeId;
    private TextView rank_tv_top;
    private TextView rank_tv_center;
    private TextView rank_tv_bottom;

    private RelativeLayout rank_one;
    private RelativeLayout rank_two;
    private RelativeLayout rank_three;

    private LinearLayout rank_background;

    private LinearLayout rank_linear_top;

    public static final String RANK_FLAG = "rank_flag";
    public static final String RANK_BAPING = "rank_baping";
    public static final String RANK_GAME = "rank_game";
    public static final String RANK_TEMP_INFO = "rank_temp_info";
    private String rank_flag = "";
    private GameTopBean mGameTopBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_bp);
        mSessionCache = SessionCache.getInstance(this);
        mWith = Glide.with(this);
        initIntent();
        initView();
    }

    private void initIntent() {
        barId = getIntent().getStringExtra(RANK_BAR_ID);
        rank_flag = getIntent().getStringExtra(RANK_FLAG);
        mGameTopBean = (GameTopBean) getIntent().getSerializableExtra(RANK_TEMP_INFO);
    }

    private void initView() {
        rank_background = (LinearLayout) findViewById(R.id.rank_background);
        rank_linear_top = (LinearLayout) findViewById(R.id.rank_linear_top);
        rank_one = (RelativeLayout) findViewById(R.id.rank_one);
        rank_two = (RelativeLayout) findViewById(R.id.rank_two);
        rank_three = (RelativeLayout) findViewById(R.id.rank_three);
        rank_tv_top = (TextView) findViewById(R.id.rank_tv_top);
        rank_tv_center = (TextView) findViewById(R.id.rank_tv_center);
        rank_tv_bottom = (TextView) findViewById(R.id.rank_tv_bottom);
        mLeftViewTitle = (ImageView) findViewById(R.id.title_left_view);
        mLeftViewTitle.setVisibility(View.VISIBLE);
        mLeftTitle = (RelativeLayout) findViewById(R.id.title_left);
        mLeftTitle.setOnClickListener(this);
        mCenterViewTitle = (TextView) findViewById(R.id.title_center_view);
        mCenterViewTitle.setVisibility(View.VISIBLE);
        mRightImgTitle = (ImageView) findViewById(R.id.title_right_img);
        mRightTvTitle = (TextView) findViewById(R.id.title_right_tv);
        mRightTitle = (RelativeLayout) findViewById(R.id.title_right);
        mTwoMoneyRank = (TextView) findViewById(R.id.rank_two_money);
        mTwoNameRank = (TextView) findViewById(R.id.rank_two_name);
        mOneMoneyRank = (TextView) findViewById(R.id.rank_one_money);
        mOneNameRank = (TextView) findViewById(R.id.rank_one_name);
        mThreeMoneyRank = (TextView) findViewById(R.id.rank_three_money);
        mThreeNameRank = (TextView) findViewById(R.id.rank_three_name);
        mListRank = (RecyclerView) findViewById(R.id.rank_list);
        mTwoHeadRank = (RoundImageView) findViewById(R.id.rank_two_head);
        mOneHeadRank = (RoundImageView) findViewById(R.id.rank_one_head);
        mThreeHeadRank = (RoundImageView) findViewById(R.id.rank_three_head);
        mListRank.setLayoutManager(new LinearLayoutManager(this));
        mOneHeadRank.setOnClickListener(this);
        mTwoHeadRank.setOnClickListener(this);
        mThreeHeadRank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.rank_one_head:
                toOther(rank_oneId);
                break;
            case R.id.rank_two_head:
                toOther(rank_twoId);
                break;
            case R.id.rank_three_head:
                toOther(rank_threeId);
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (rank_flag.equals(RANK_BAPING)){//展示霸屏的排行榜
            mCenterViewTitle.setText("霸屏榜");
            requestData();
        }else if (rank_flag.equals(RANK_GAME)){//展示游戏的排行榜
            rank_background.setBackgroundResource(R.drawable.games_bg);
            mCenterViewTitle.setText("游戏排行榜");
            dealGameTextColor(mGameTopBean.getRanking() , mGameTopBean.getMoney());
            dealGameRankList(mGameTopBean.getTopUserList());
        }
    }

    class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankAdapter_VH> {

        @Override
        public RankAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(Rank_BP.this).inflate(R.layout.rank_itemview , parent , false);
            return new RankAdapter_VH(itemView);
        }

        @Override
        public void onBindViewHolder(RankAdapter_VH holder, final int position) {
            holder.rank_it_parent.getBackground().setAlpha(50);
            mWith.load(AIMIApplication.dealHeadImg(mTopUserBeanList.get(position).getHeadImg()))
                    .placeholder(R.drawable.default_icon)
                    .transform(new CompressTransform(Rank_BP.this , mTopUserBeanList.get(position).getHeadImg()))
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.rank_it_head);
            Log.e(TAG, "onBindViewHolder: "+AIMIApplication.dealHeadImg(mTopUserBeanList.get(position).getHeadImg()));
            holder.rank_it_rank.setText(mTopUserBeanList.get(position).getRank()+"");
            holder.rank_it_name.setText(mTopUserBeanList.get(position).getNickName());
            holder.rank_it_money.setText(mTopUserBeanList.get(position).getMoney()+"");
            holder.rank_it_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toOther(mTopUserBeanList.get(position).getUserId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTopUserBeanList == null ? 0 : mTopUserBeanList.size();
        }

        class RankAdapter_VH extends RecyclerView.ViewHolder{
            private RelativeLayout rank_it_parent;
            private TextView rank_it_rank , rank_it_name , rank_it_money;
            private RoundImageView rank_it_head;

            public RankAdapter_VH(View itemView) {
                super(itemView);
                rank_it_parent = (RelativeLayout) itemView.findViewById(R.id.rank_it_parent);
                rank_it_rank = (TextView) itemView.findViewById(R.id.rank_it_rank);
                rank_it_name = (TextView) itemView.findViewById(R.id.rank_it_name);
                rank_it_money = (TextView) itemView.findViewById(R.id.rank_it_money);
                rank_it_head = (RoundImageView) itemView.findViewById(R.id.rank_it_head);
            }
        }
    }

    /**
     * 进入他人主页
     * @param userId
     */
    private void toOther(String userId){
        if (userId.equals(mSessionCache.userId)){//自己的
            MineHomepage.toOtherHomePage(Rank_BP.this ,
                    userId ,
                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX) ,
                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY) , true);
        }else {//别人的
            OtherHomepage.toOtherHomePage(Rank_BP.this ,
                    userId ,
                    TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX) ,
                    TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY) , false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWith = null;
    }

    /**
     * 请求霸屏榜数据
     */
    private void requestData(){
        P640000 p640000 = new P640000();
        p640000.req.sessionId = mSessionCache.sessionId;
        p640000.req.barId = barId;
        new RankTask().execute(getApplicationContext() , p640000);
    }

    /**
     * 霸屏排行榜查询
     */
    private class RankTask extends DefaultTask{
        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(Rank_BP.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            PromptUtils.showToast(Rank_BP.this , "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            P640000 p640000 = (P640000) protocol;
            if (p640000.resp.transcode.equals("9999")){
                PromptUtils.showToast(Rank_BP.this , p640000.resp.msg);
            }else {
                mTopUserBeanList.clear();
                List<TopUserBean> topUserList = p640000.resp.topUserList;
                if (topUserList.size() > 3){//当size大于3的时候才会循环取出后边的数据放到集合中，更新适配器
                    for (int i = 3 ; i < topUserList.size() ; i++){
                        TopUserBean topUserBean = topUserList.get(i);
                        topUserBean.setRank(i+1);
                        mTopUserBeanList.add(topUserBean);
                    }
                    initData(topUserList);
                }else {
                    //initData(topUserList);
                    for (int i = 0 ; i < topUserList.size() ; i++){
                        TopUserBean topUserBean = topUserList.get(i);
                        topUserBean.setRank(i+1);
                        mTopUserBeanList.add(topUserBean);
                    }
                    initLessData();
                }
            }
        }
    }

    /**
     * 设置霸屏排行榜数据 数据量小于3
     */
    private void initLessData(){
        rank_linear_top.setVisibility(View.GONE);
        mRankAdapter = new RankAdapter();
        mListRank.setAdapter(mRankAdapter);
    }

    /**
     * 设置霸屏排行榜数据 数据量大于3
     * @param list
     */
    private void initData(List<TopUserBean> list) {
        rank_oneId = list.get(0).getUserId();
        setData(list , mOneHeadRank , mOneNameRank , mOneMoneyRank , 0);

        rank_twoId = list.get(1).getUserId();
        setData(list , mTwoHeadRank , mTwoNameRank , mTwoMoneyRank , 1);

        rank_threeId = list.get(2).getUserId();
        setData(list , mThreeHeadRank , mThreeNameRank , mThreeMoneyRank , 2);

        mRankAdapter = new RankAdapter();
        mListRank.setAdapter(mRankAdapter);
    }

    /**
     * 设置前三名数据
     * @param list
     * @param imageView
     * @param name
     * @param money
     * @param position
     */
    private void setData(List<TopUserBean> list , RoundImageView imageView , TextView name , TextView money , int position){
        mWith.load(AIMIApplication.dealHeadImg(list.get(position).getHeadImg())).placeholder(R.drawable.default_icon).transform(new CompressTransform(this , AIMIApplication.dealHeadImg(list.get(position).getHeadImg()))).centerCrop().into(imageView);
        name.setText(list.get(position).getNickName()+"");
        money.setText(list.get(position).getMoney()+"");
    }

    public static void toRank(Context context , String barId , String rank_flag){
        Intent intent = new Intent(context , Rank_BP.class);
        intent.putExtra(RANK_BAR_ID , barId);
        intent.putExtra(RANK_FLAG , rank_flag);
        context.startActivity(intent);
    }

    public static void toRank(Context context , String barId , String rank_flag , GameTopBean gameTopBean){
        Intent intent = new Intent(context , Rank_BP.class);
        intent.putExtra(RANK_BAR_ID , barId);
        intent.putExtra(RANK_FLAG , rank_flag);
        intent.putExtra(RANK_TEMP_INFO ,gameTopBean);
        context.startActivity(intent);
    }

    /**
     * 处理游戏排行榜上面文字的颜色
     * @param rankNum
     */
    private void dealGameTextColor(int rankNum , int money){
        rank_tv_top.setVisibility(View.VISIBLE);
        rank_tv_center.setVisibility(View.VISIBLE);
        rank_tv_bottom.setVisibility(View.VISIBLE);
        if (rankNum == 1){//第一名
            rank_tv_top.setTextColor(getResources().getColor(R.color.yellow_light));
            rank_tv_center.setTextColor(getResources().getColor(R.color.yellow_light));
            rank_tv_bottom.setTextColor(getResources().getColor(R.color.yellow_light));
            rank_tv_top.setText("恭喜您");
            rank_tv_center.setText("获得第"+rankNum+"名！");
            rank_tv_bottom.setText("赢得"+money+"猫币  已放入您的钱包");
        }else if (rankNum == 2){//第二名
            rank_tv_top.setTextColor(getResources().getColor(R.color.gender_man));
            rank_tv_center.setTextColor(getResources().getColor(R.color.gender_man));
            rank_tv_bottom.setTextColor(getResources().getColor(R.color.gender_man));
            rank_tv_top.setText("恭喜您");
            rank_tv_center.setText("获得第"+rankNum+"名！");
            rank_tv_bottom.setText("赢得"+money+"猫币  已放入您的钱包");
        }else if (rankNum == 3){//第三名
            rank_tv_top.setTextColor(getResources().getColor(R.color.rank_three));
            rank_tv_center.setTextColor(getResources().getColor(R.color.rank_three));
            rank_tv_bottom.setTextColor(getResources().getColor(R.color.rank_three));
            rank_tv_top.setText("恭喜您");
            rank_tv_center.setText("获得第"+rankNum+"名！");
            rank_tv_bottom.setText("赢得"+money+"猫币  已放入您的钱包");
        }else {//其他名次
            rank_tv_top.setTextColor(getResources().getColor(R.color.white));
            rank_tv_center.setTextColor(getResources().getColor(R.color.white));
            rank_tv_bottom.setTextColor(getResources().getColor(R.color.white));
            rank_tv_top.setText("获得第"+rankNum+"名");
            rank_tv_center.setText("今天也是元气满满的一天");
            rank_tv_bottom.setText("继续加油~");
        }
    }

    /**
     * 处理游戏排行榜
     * @param list
     */
    private void dealGameRankList(List<TopUserBean> list){
        mTopUserBeanList.clear();
        if (list.size() > 3){
            for (int i = 3 ; i < list.size() ; i++){
                TopUserBean topUserBean = list.get(i);
                topUserBean.setRank(i+1);
                mTopUserBeanList.add(topUserBean);
            }
            initData(list);
        }else {
            for (int i = 0 ; i < list.size() ; i++){
                TopUserBean topUserBean = list.get(i);
                topUserBean.setRank(i+1);
                mTopUserBeanList.add(topUserBean);
            }
            initLessData();
        }
    }
}
