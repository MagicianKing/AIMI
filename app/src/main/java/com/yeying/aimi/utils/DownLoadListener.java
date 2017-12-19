package com.yeying.aimi.utils;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/11/8 上午10:52
 */

public interface DownLoadListener {
    void onDownLoadListener(int i);
    void onDownLoadListenerFinish(String s);
    void onDownLoadListenerCancel(String s);
}
