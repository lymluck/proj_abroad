package com.smartstudy.commonlib.ui.customView.clipImageLayout;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout {

    public static final String CIRCLE = "circle";
    public static final String SQUARE = "square";
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private int mClipStyle;

    private int mHorizontalPadding = 30;

    public ClipImageLayout(Context context) {
        this(context, null);
    }

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mZoomImageView = new ClipZoomImageView(context);
        mZoomImageView.setClipStyle(this.getTag().toString());
        mClipImageView = new ClipImageBorderView(context);
        mClipImageView.setClipStyle(this.getTag().toString());

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);


        mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public Bitmap clip() {
        return mZoomImageView.clip();
    }

    public void setBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
    }

    public int getmClipStyle() {
        return mClipStyle;
    }

    public void setmClipStyle(int mClipStyle) {
        this.mClipStyle = mClipStyle;
    }

}
