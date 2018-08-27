/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartstudy.qrcode.qrscan.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.smartstudy.qrcode.R;
import com.smartstudy.qrcode.qrscan.camera.CameraManager;
import com.smartstudy.qrcode.tools.QRCodeUtil;


public final class ScanBoxView extends View {

    private int mAnimTime;
    private int ScreenRate;
    private int cornerWidth;
    private int textSize;
    private int textMargin;
    private Paint paint;
    private final int maskColor;
    private float borderSize;
    private float mGridScanLineBottom;
    private int mMoveStepDistance;
    private Bitmap mGridScanLineBitmap;
    private Rect frame;
    private int mAnimDelayTime;

    public ScanBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ScreenRate = QRCodeUtil.dp2px(context, 17);
        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        mMoveStepDistance = QRCodeUtil.dp2px(context, 2f);
        borderSize = QRCodeUtil.sp2px(context, 0.5f);
        textSize = QRCodeUtil.sp2px(context, 14f);
        cornerWidth = QRCodeUtil.dp2px(context, 1.5f);
        textMargin = QRCodeUtil.dp2px(context, 23f);
        mGridScanLineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode_grid_scan_line);
        mAnimTime = 1000;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (frame == null) {
            frame = CameraManager.get().getFramingRect();
            if (frame != null) {
                mGridScanLineBottom = frame.top + 0.5f;

            }
        }
        if (frame != null) {
            mAnimDelayTime = (int) ((1.0f * mAnimTime * mMoveStepDistance) / frame.width());

            // 画遮罩层
            drawMask(canvas, frame);
            // 画边框线
            drawBorderLine(canvas, frame);
            // 画四个直角的线
            drawCornerLine(canvas, frame);
            // 画提示文本
            drawTipText(canvas, frame);
            // 画扫描线
            drawScanLine(canvas, frame);
            // 移动扫描线的位置
            moveScanLine(frame);
        }
    }

    /**
     * 画遮罩层
     *
     * @param canvas
     */
    private void drawMask(Canvas canvas, Rect frame) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
            paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
    }

    /**
     * 画边框线
     *
     * @param canvas
     */
    private void drawBorderLine(Canvas canvas, Rect frame) {
        if (borderSize > 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#b2ffffff"));
            paint.setStrokeWidth(borderSize);
            canvas.drawRect(frame, paint);
        }
    }

    /**
     * 画四个直角的线
     *
     * @param canvas
     */
    private void drawCornerLine(Canvas canvas, Rect frame) {
        // 扫描框边角颜色
        if (cornerWidth > 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#078CF1"));
            paint.setStrokeWidth(cornerWidth);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                frame.top + cornerWidth, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + cornerWidth,
                frame.top + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                frame.top + cornerWidth, paint);
            canvas.drawRect(frame.right - cornerWidth, frame.top, frame.right,
                frame.top + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - cornerWidth, frame.left
                + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left
                + cornerWidth, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom
                - cornerWidth, frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - cornerWidth, frame.bottom
                - ScreenRate, frame.right, frame.bottom, paint);
        }
    }

    /**
     * 画扫描线
     *
     * @param canvas
     */
    private void drawScanLine(Canvas canvas, Rect frame) {
        if (mGridScanLineBitmap != null) {
            RectF dstGridRectF = new RectF(frame.left, frame.top + 0.5f, frame.right, mGridScanLineBottom);
            Rect srcRect = new Rect(0, (int) (mGridScanLineBitmap.getHeight() - dstGridRectF.height()),
                mGridScanLineBitmap.getWidth(), mGridScanLineBitmap.getHeight());
            if (srcRect.top < 0) {
                srcRect.top = 0;
                dstGridRectF.top = dstGridRectF.bottom - srcRect.height();
            }
            canvas.drawBitmap(mGridScanLineBitmap, srcRect, dstGridRectF, paint);
        }
    }

    /**
     * 移动扫描线的位置
     */
    private void moveScanLine(Rect frame) {
        // 处理网格扫描图片的情况
        mGridScanLineBottom += mMoveStepDistance;
        if (mGridScanLineBottom > frame.bottom) {
            mGridScanLineBottom = frame.top + 0.5f;
        }
        postInvalidateDelayed(mAnimDelayTime, frame.left, frame.top, frame.right, frame.bottom);
    }

    /**
     * 画提示文本
     *
     * @param canvas
     */
    private void drawTipText(Canvas canvas, Rect frame) {
        canvas.save();
        String textStr = getResources().getString(R.string.scan_text);
        TextPaint mTipPaint = new TextPaint();
        mTipPaint.setAntiAlias(true);
        mTipPaint.setTextSize(textSize);
        mTipPaint.setColor(Color.WHITE);
        mTipPaint.setAlpha(0xB2);
        canvas.translate(0, frame.bottom + textMargin);
        StaticLayout mTipTextSl = new StaticLayout(textStr, mTipPaint, QRCodeUtil.getScreenResolution(getContext()).x, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, true);
        mTipTextSl.draw(canvas);
        canvas.restore();
    }
}
