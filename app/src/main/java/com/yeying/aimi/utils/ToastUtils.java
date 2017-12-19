package com.yeying.aimi.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast = null;
    private static ToastUtils mInstance = null;

    public static ToastUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ToastUtils();
        }
        return mInstance;
    }

    public void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

}
