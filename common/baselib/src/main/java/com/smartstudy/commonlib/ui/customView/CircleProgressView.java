package com.smartstudy.commonlib.ui.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by louis on 17/4/25.
 */

public class CircleProgressView extends View {

    private int mMaxProgress = 100;

    private double mProgress = 30;

    private final int mCircleLineStrokeWidth = 10;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;
    private String mTxtHint;
    private int progressColor = Color.rgb(0xf8, 0x60, 0x30);
    private int hintColor = Color.rgb(0x99, 0x99, 0x99);

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Style.STROKE);
        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
        mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角x
        mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(progressColor);
        String text = "";
        if (mProgress > 0) {
            text = mProgress + "%";
        } else {
            //异常情况
            mProgress = 0;
            text = "N/A";
        }
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        // 绘制进度文案显示
        mPaint.setStrokeWidth(mTxtStrokeWidth);
        int textHeight = height / 4;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2, mPaint);

        if (!TextUtils.isEmpty(mTxtHint)) {
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            text = mTxtHint;
            textHeight = height / 8;
            mPaint.setColor(hintColor);
            mPaint.setTextSize(textHeight);
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, 2 * height / 3 + textHeight / 2, mPaint);
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(double progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(double progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }


    public String getmTxtHint() {
        return mTxtHint;
    }

    public void setmTxtHint(String mTxtHint) {
        this.mTxtHint = mTxtHint;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }
}
