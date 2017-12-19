package com.yeying.aimi.utils;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeying.aimi.R;

/**
 * Created by tanchengkeji on 2017/9/12.
 */

public class PromptUtils {

    public static void showToast(Activity mActivity, String content) {
        showToast(mActivity, content, Toast.LENGTH_SHORT);
    }

    public static void showToast(Activity mActivity, int id) {
        if (mActivity == null) {
            return;
        }
        String content = mActivity.getResources().getString(id);
        if (content == null) {
            return;
        }
        showToast(mActivity, content, Toast.LENGTH_SHORT);
    }

    /**
     * toast
     *
     * @param mActivity
     *
     */
    private static void showToast(Activity mActivity, String content, int duration) {
        if (Utils.isFastDoubleClick() || mActivity == null) {
            return;
        }
        Toast toast = Toast.makeText(mActivity, content, duration);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.define_toast, null);
        TextView toastTv = (TextView) view.findViewById(R.id.toast_tv);
        toastTv.setText(content);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -200);
        toast.show();
    }

    //loding框
    public static PopupWindow getProgressDialogPop(Activity mActivity) {
        View view=mActivity.getLayoutInflater().inflate(R.layout.net_dialog,null,false);
        ImageView im_loading= (ImageView) view.findViewById(R.id.im_loading);
        RelativeLayout dialog_progress_re = (RelativeLayout) view.findViewById(R.id.dialog_progress_re);
        im_loading.setImageResource(R.drawable.loading_anim);
        AnimationDrawable animation = (AnimationDrawable) im_loading.getDrawable();
        animation.start();
        final PopupWindow popupWindow=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(false);
        /*dialog_progress_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });*/
        return popupWindow;
    }

}
