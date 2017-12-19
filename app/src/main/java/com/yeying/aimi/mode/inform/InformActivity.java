package com.yeying.aimi.mode.inform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;

/**
 * Created by tanchengkeji on 2017/9/20.
 */

public class InformActivity extends BaseActivity implements View.OnClickListener {

    private ImageView inform_back;
    private TextView like,message,inform_like_yes_line,inform_message_ok_line;
    private ViewPager vp;

    private Fragment_InformMessage fragment_informMessage=null;
    private Fragment_InformLike fragment_informLike=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        initViews();
        initEvent();
        initLogic();
    }

    private void initViews() {
        inform_back = (ImageView) findViewById(R.id.inform_back);
        inform_back.setOnClickListener(this);
        like = (TextView) findViewById(R.id.inform_like_yes);
        message = (TextView) findViewById(R.id.inform_message_ok);
        inform_message_ok_line = (TextView) findViewById(R.id.inform_message_ok_line);
        inform_like_yes_line= (TextView) findViewById(R.id.inform_like_yes_line);
        vp = (ViewPager) findViewById(R.id.inform_vp);
        vp.setOffscreenPageLimit(1);
    }

    private void initEvent() {
        like.setOnClickListener(this);
        message.setOnClickListener(this);
    }

    private void initLogic() {

        vp.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return 2;
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub

                switch (arg0) {
                    case 0:
                        if (fragment_informMessage==null){
                            fragment_informMessage=new Fragment_InformMessage();
                        }
                        return fragment_informMessage;
                    case 1:
                        if (fragment_informLike==null){
                            fragment_informLike=new Fragment_InformLike();
                        }
                        return fragment_informLike;

                    default:
                        return fragment_informLike;
                }

            }
        });
        vp.setOnPageChangeListener(listener);
        vp.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inform_back:
                finish();
                break;
            case R.id.inform_like_yes:
                hideLine();
                inform_like_yes_line.setBackgroundResource(R.drawable.round_yellow_bg);
                vp.setCurrentItem(1);
                break;
            case R.id.inform_message_ok:
                hideLine();
                inform_message_ok_line.setBackgroundResource(R.drawable.round_yellow_bg);
                vp.setCurrentItem(0);
                break;
        }
    }

    //viewpager 监听
    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 0:
                    hideLine();
                    inform_message_ok_line.setBackgroundResource(R.drawable.round_yellow_bg);
                    vp.setCurrentItem(0);

                    break;
                case 1:
                    hideLine();
                    inform_like_yes_line.setBackgroundResource(R.drawable.round_yellow_bg);
                    vp.setCurrentItem(1);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    private void hideLine(){
        inform_message_ok_line.setBackgroundResource(R.drawable.round_black_bg);
        inform_like_yes_line.setBackgroundResource(R.drawable.round_black_bg);
    }
}
