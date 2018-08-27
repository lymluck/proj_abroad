package com.smartstudy.xxd.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/6/1
 * @describe 环形统计图
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class RingView extends View {

    private Context mContext;
    private Paint mPaint;
    /**
     * 画笔的宽
     */
    private int mPaintWidth = 0;
    /**
     * 上边距
     */
    private int topMargin = 30;
    /**
     * 左边距
     */
    private int leftMargin = 80;
    private Resources mRes;
    /**
     * 展示文字的大小
     */
    private int showRateSize = 10;
    /**
     * 圆心点X要与外圆半径相等
     */
    private int circleCenterX = 100;
    /**
     * 圆心点Y要与外圆半径相等
     */
    private int circleCenterY = 100;
    /**
     * 外圆的半径
     */
    private int ringOuterRidus = 100;
    /**
     * 内圆的半径
     */
    private int ringInnerRidus = 60;
    /**
     * 点所在圆的半径
     */
    private int ringPointRidus = 80;
    /**
     * 点的外延距离与点所在圆半径的长度比率
     */
    private float rate = 0.4f;
    /**
     * 点外延后  折的横线的长度
     */
    private float extendLineWidth = 20;
    /**
     * 外圆所在的矩形
     */
    private RectF rectF;
    /**
     * 点所在的矩形
     */
    private RectF rectFPoint;

    private List<Integer> colorList;
    private List<Float> rateList;
    private boolean isRing;
    private boolean isShowCenterPoint;
    private boolean isShowRate;

    public RingView(Context context) {
        super(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setShow(List<Integer> colorList, List<Float> rateList) {
        setShow(colorList, rateList, false);
    }

    public void setShow(List<Integer> colorList, List<Float> rateList, boolean isRing) {
        setShow(colorList, rateList, isRing, false);
    }

    public void setShow(List<Integer> colorList, List<Float> rateList, boolean isRing, boolean isShowRate) {
        setShow(colorList, rateList, isRing, isShowRate, false);
    }

    public void setShow(List<Integer> colorList, List<Float> rateList, boolean isRing, boolean isShowRate, boolean isShowCenterPoint) {
        this.colorList = colorList;
        this.rateList = rateList;
        this.isRing = isRing;
        this.isShowRate = isShowRate;
        this.isShowCenterPoint = isShowCenterPoint;
    }

    private void initView() {
        this.mRes = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int screenWidth = ScreenUtils.getScreenWidth();
        leftMargin = (DensityUtils.px2dip(screenWidth) - (2 * circleCenterX)) / 2;

        mPaint.setColor(getResources().getColor(R.color.bg_islet_menu));
        mPaint.setStrokeWidth(DensityUtils.dip2px(mPaintWidth));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        rectF = new RectF(DensityUtils.dip2px(mPaintWidth + leftMargin),
            DensityUtils.dip2px(mPaintWidth + topMargin),
            DensityUtils.dip2px(circleCenterX + ringOuterRidus + mPaintWidth * 2 + leftMargin),
            DensityUtils.dip2px(circleCenterY + ringOuterRidus + mPaintWidth * 2 + topMargin));

        rectFPoint = new RectF(DensityUtils.dip2px(mPaintWidth + leftMargin + (ringOuterRidus - ringPointRidus)),
            DensityUtils.dip2px(mPaintWidth + topMargin + (ringOuterRidus - ringPointRidus)),
            DensityUtils.dip2px(circleCenterX + ringPointRidus + mPaintWidth * 2 + leftMargin),
            DensityUtils.dip2px(circleCenterY + ringPointRidus + mPaintWidth * 2 + topMargin));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pointList.clear();
        if (colorList != null) {
            for (int i = 0; i < colorList.size(); i++) {
                mPaint.setColor(mRes.getColor(colorList.get(i)));
                mPaint.setStyle(Paint.Style.FILL);
                drawOuter(canvas, i);
            }
        }
        mPaint.setStyle(Paint.Style.FILL);
        if (isRing) {
            drawInner(canvas);
        }
        if (isShowCenterPoint) {
            drawCenterPoint(canvas);
        }

    }

    private void drawCenterPoint(Canvas canvas) {
        mPaint.setColor(mRes.getColor(R.color.bg_islet_menu));
        canvas.drawCircle(DensityUtils.dip2px(circleCenterX + mPaintWidth * 2 + leftMargin),
            DensityUtils.dip2px(circleCenterY + mPaintWidth * 2 + topMargin),
            DensityUtils.dip2px(1), mPaint);
    }

    private void drawInner(Canvas canvas) {
        mPaint.setColor(mRes.getColor(R.color.white));
        canvas.drawCircle(DensityUtils.dip2px(circleCenterX + mPaintWidth * 2 + leftMargin),
            DensityUtils.dip2px(circleCenterY + mPaintWidth * 2 + topMargin),
            DensityUtils.dip2px(ringInnerRidus), mPaint);
    }

    private float preRate;

    private void drawArcCenterPoint(Canvas canvas, int position) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mRes.getColor(R.color.transparent));
        mPaint.setStrokeWidth(DensityUtils.dip2px(1));
        canvas.drawArc(rectFPoint, preAngle, (endAngle) / 2, true, mPaint);
        dealPoint(rectFPoint, preAngle, (endAngle) / 2, pointArcCenterList);
        Point point = pointArcCenterList.get(position);
        mPaint.setColor(mRes.getColor(R.color.white));
        canvas.drawCircle(point.x, point.y, DensityUtils.dip2px(2), mPaint);

        if (preRate / 2 + rateList.get(position) / 2 < 5) {
            extendLineWidth += 20;
            rate -= 0.05f;
        } else {
            extendLineWidth = 20;
            rate = 0.4f;
        }

        // 外延画折线
        float lineXPoint1 = (point.x - DensityUtils.dip2px(leftMargin + ringOuterRidus)) * (1 + rate);
        float lineYPoint1 = (point.y - DensityUtils.dip2px(topMargin + ringOuterRidus)) * (1 + rate);

        float[] floats = new float[8];
        floats[0] = point.x;
        floats[1] = point.y;
        floats[2] = DensityUtils.dip2px(leftMargin + ringOuterRidus) + lineXPoint1;
        floats[3] = DensityUtils.dip2px(topMargin + ringOuterRidus) + lineYPoint1;
        floats[4] = DensityUtils.dip2px(leftMargin + ringOuterRidus) + lineXPoint1;
        floats[5] = DensityUtils.dip2px(topMargin + ringOuterRidus) + lineYPoint1;
        if (point.x >= DensityUtils.dip2px(leftMargin + ringOuterRidus)) {
            mPaint.setTextAlign(Paint.Align.LEFT);
            floats[6] = DensityUtils.dip2px(leftMargin + ringOuterRidus) + lineXPoint1
                + DensityUtils.dip2px(extendLineWidth);
        } else {
            mPaint.setTextAlign(Paint.Align.RIGHT);
            floats[6] = DensityUtils.dip2px(leftMargin + ringOuterRidus) + lineXPoint1
                - DensityUtils.dip2px(extendLineWidth);
        }
        floats[7] = DensityUtils.dip2px(topMargin + ringOuterRidus) + lineYPoint1;
        mPaint.setColor(mRes.getColor(colorList.get(position)));
        canvas.drawLines(floats, mPaint);
        mPaint.setTextSize(DensityUtils.dip2px(showRateSize));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawText(rateList.get(position) + "%", floats[6], floats[7]
            + DensityUtils.dip2px(showRateSize) / 3, mPaint);
        preRate = rateList.get(position);
    }

    List<Point> pointList = new ArrayList<>();
    List<Point> pointArcCenterList = new ArrayList<>();

    private void dealPoint(RectF rectF, float startAngle, float endAngle, List<Point> pointList) {
        Path orbit = new Path();
        //通过Path类画一个90度（180—270）的内切圆弧路径
        orbit.addArc(rectF, startAngle, endAngle);

        PathMeasure measure = new PathMeasure(orbit, false);

        float[] coords = new float[]{0f, 0f};
        //利用PathMeasure分别测量出各个点的坐标值coords
        int divisor = 1;
        measure.getPosTan(measure.getLength() / divisor, coords, null);
        float x = coords[0];
        float y = coords[1];
        Point point = new Point(Math.round(x), Math.round(y));
        pointList.add(point);
    }

    private void drawOuter(Canvas canvas, int position) {
        if (rateList != null) {
            endAngle = getAngle(rateList.get(position));
        }
        canvas.drawArc(rectF, preAngle, endAngle, true, mPaint);

        if (isShowRate) {
            drawArcCenterPoint(canvas, position);
        }

        preAngle = preAngle + endAngle;
    }

    private float preAngle = 0;
    private float endAngle = 0;

    /**
     * @param percent 百分比
     * @return
     */
    private float getAngle(float percent) {
        float a = -360f / 100f * percent;
        return a;
    }
}
