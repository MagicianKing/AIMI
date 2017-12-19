package com.yeying.aimi.fragment;


import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by tanchengkeji on 2017/9/14.
 */

public class BaseFragment extends Fragment {
    public  final String VIEW_NAME = this.getClass().getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(VIEW_NAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(VIEW_NAME);
    }
}
