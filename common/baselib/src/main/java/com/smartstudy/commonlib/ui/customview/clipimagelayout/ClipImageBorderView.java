package com.smartstudy.commonlib.ui.customview.clipimagelayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {
    private int mHorizontalPadding;
    private int mBorderWidth = 2;

    private Paint mPaint;
    private String clipStyle;


    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);
        int mVerticalPadding = (getHeight() - (getWidth() - 2 * mHorizontalPadding)) / 2;
        String style = getClipStyle();
        if (style.equals(ClipImageLayout.SQUARE)) {
            canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
        } else if (style.equals(ClipImageLayout.CIRCLE)) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
        }
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;

    }

    public String getClipStyle() {
        return clipStyle;
    }

    public void setClipStyle(String clipStyle) {
        this.clipStyle = clipStyle;
    }

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

}
