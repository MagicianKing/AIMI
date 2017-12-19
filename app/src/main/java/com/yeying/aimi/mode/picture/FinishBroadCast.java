package com.yeying.aimi.mode.picture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tanchengkeji on 2017/9/27.
 */

public class FinishBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null){
            ((Activity)context).finish();
        }
    }
}
