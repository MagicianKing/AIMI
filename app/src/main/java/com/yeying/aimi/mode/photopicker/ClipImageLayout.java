package com.yeying.aimi.mode.photopicker;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout {
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private int bitW;
    private int bitH;
    private int mHorizontalPadding = 0;// 框左右的边距，这里左右边距为0，为�?��屏幕宽度的正方形�?

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context, false);
        mClipImageView = new ClipImageBorderView(context, false);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    public void setImageDrawable(Drawable drawable) {
        mZoomImageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
        bitH = bitmap.getHeight();
        bitW = bitmap.getWidth();
        Log.e("真实宽2", bitmap.getWidth() + "");
        Log.e("真实高2", bitmap.getHeight() + "");
    }

    /**
     * 对外公布设置边距的方�?单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     *
     * @param kspBitmap
     * @param kspheight
     * @param kspwidth
     * @return
     */
    public Bitmap clip(Bitmap kspBitmap, int kspwidth, int kspheight) {
        return mZoomImageView.clip(kspBitmap, kspwidth, kspheight);
    }


}
