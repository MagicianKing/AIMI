package com.yeying.aimi.mode.photopicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yeying.aimi.R;


public class ClipImageLayoutJiancai extends RelativeLayout {
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private int bitW;
    private int bitH;
    private int mHorizontalPadding = 0;// 框左右的边距，这里左右边距为0，为�?��屏幕宽度的正方形�?

    public ClipImageLayoutJiancai(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean isjiancai = true;
        mZoomImageView = new ClipZoomImageView(context, true);
        mClipImageView = new ClipImageBorderView(context, true);
        //mZoomImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        ViewGroup.LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);

        mZoomImageView.setBackgroundResource(R.color.black);
    }

    public void setImageDrawable(Drawable drawable) {
        mZoomImageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
        bitH = bitmap.getHeight();
        bitW = bitmap.getWidth();
        //Log.e("真实宽2", bitmap.getWidth() + "");
        //Log.e("真实高2", bitmap.getHeight() + "");
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
