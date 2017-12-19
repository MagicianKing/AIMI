package com.yeying.aimi.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by king on 2017/8/16.
 */

public abstract class WeakHandler extends Handler {
    WeakReference<Object> mObjectWeakReference;


    public WeakHandler(Object o) {
        mObjectWeakReference = new WeakReference<>(o);

    }
    public Object getObjct(){
      return  mObjectWeakReference.get();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


    }
}
