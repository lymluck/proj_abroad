package com.smartstudy.commonlib.ui.customview.calendar.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.smartstudy.commonlib.ui.customview.calendar.entity.CalendarInfo;
import com.smartstudy.commonlib.ui.customview.calendar.theme.ADCircleDayTheme;
import com.smartstudy.commonlib.utils.DensityUtils;

import java.util.Calendar;

/**
 * @author luoyongming
 * @date on 2018/5/24
 * @describe 考试时间日历
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class CirclePointMonthView extends MonthView {

    private String exam;

    public CirclePointMonthView(Context context, AttributeSet attrs, int currYear, int currMonth
            , String exam) {
        super(context, attrs);
        super.setCurrYear(currYear);
        super.setCurrMonth(currMonth);
        super.setExam(exam);
        this.exam = exam;
    }

    @Override
    protected void drawBG(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar) {
        float startRecX = columnSize * column + 1;
        float startRecY = rowSize * row + 1;
        float endRecX = startRecX + columnSize - 2 * 1;
        float endRecY = startRecY + rowSize - 2 * 1;
        float cx = (startRecX + endRecX) / 2;
        float cy = (startRecY + endRecY) / 2;
        float radius = columnSize < (rowSize * 0.75) ? columnSize / 2 : (float) (rowSize * 0.75) / 2;
        CalendarInfo info = iscalendarInfo(year, month, day);
        paint.setColor(theme.colorSelectBG(info != null ? info.des : ""));
        setDisablePaintColor(calendar, year, month, day);
        if (info != null && info.select) {
            if (DEFAULT_FILTER.equals(exam) || MY_EXAM.equals(exam) || info.des.equals(exam)) {
                //绘制背景色圆形
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, radius, paint);
            }
        }
    }

    @Override
    protected void drawDecor(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar) {
        if (calendarInfos != null && calendarInfos.size() > 0) {
            CalendarInfo info = iscalendarInfo(year, month, day);
            String des = info != null ? info.des : "";
            if (TextUtils.isEmpty(des)) {
                return;
            } else {
                if (info.select) {
                    return;
                }
                if (!DEFAULT_FILTER.equals(exam) && !des.equals(exam)) {
                    return;
                }
            }
            paint.setColor(theme.colorDecor(des));
            setDisablePaintColor(calendar, year, month, day);
            paint.setStyle(Paint.Style.FILL);
            float circleX = (float) (columnSize * column + columnSize * 0.5);
            float circleY = (float) (rowSize * row + rowSize * 0.85);
            canvas.drawCircle(circleX, circleY, theme.sizeDecor(), paint);
        }
    }

    @Override
    protected void drawRest(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar) {
    }

    @Override
    protected void drawText(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar) {
        paint.setTextSize(theme.sizeDay());
        paint.setStyle(Paint.Style.STROKE);
        float startX = columnSize * column + (columnSize - paint.measureText(day + "")) / 2;
        // day文字在选框中位置修正
        int fixDay = DensityUtils.dip2px(7f);
        // des文字在选框中位置修正
        int fixDes = DensityUtils.dip2px(3.6f);
        float startY = rowSize * row + rowSize / 2 - (paint.ascent() + paint.descent()) / 2 - fixDay;
        CalendarInfo info = iscalendarInfo(year, month, day);
        String des = info != null ? info.des : "";
        if (info != null && info.select) {
            //日期为选中的日期
            if (!TextUtils.isEmpty(des)) {
                //desc不为空的时候
                if (DEFAULT_FILTER.equals(exam) || MY_EXAM.equals(exam) || des.equals(exam)) {
                    // 画日历
                    paint.setColor(theme.colorSelectDay());
                    canvas.drawText(day + "", startX, startY, paint);

                    // 画描述
                    paint.setColor(theme.colorSelectDesc());
                    paint.setTextSize(theme.sizeDesc());
                    int desX = (int) (columnSize * column + (columnSize - paint.measureText(des)) / 2);
                    int desY = (int) (rowSize * row + rowSize * 0.7 - (paint.ascent() + paint.descent()) / 2)
                            - fixDes;
                    canvas.drawText(des, desX, desY, paint);
                } else {
                    // 画日历
                    paint.setColor(theme.colorDay());
                    setDisablePaintColor(calendar, year, month, day);
                    canvas.drawText(day + "", startX, startY, paint);
                }
            }
        } else {
            // 画日历
            paint.setColor(theme.colorDay());
            setDisablePaintColor(calendar, year, month, day);
            canvas.drawText(day + "", startX, startY, paint);
            // 画描述
            if (!TextUtils.isEmpty(des)) {
                //没选中，但是desc不为空
                if (DEFAULT_FILTER.equals(exam) || des.equals(exam)) {
                    paint.setTextSize(theme.sizeDesc());
                    paint.setColor(theme.colorDesc(des));
                    setDisablePaintColor(calendar, year, month, day);
                    int desX = (int) (columnSize * column + Math.abs((columnSize - paint.measureText(des)) / 2));
                    int desY = (int) (rowSize * row + rowSize * 0.7 - (paint.ascent() + paint.descent()) / 2)
                            - fixDes;
                    canvas.drawText(des, desX, desY, paint);
                }
            }
        }
    }

    @Override
    protected void createTheme() {
        theme = new ADCircleDayTheme();
    }

    /**
     * 设置过期日历颜色
     *
     * @param calendar
     * @param year
     * @param month
     * @param day
     */
    private void setDisablePaintColor(Calendar calendar, int year, int month, int day) {
        int currYear = calendar.get(Calendar.YEAR);
        if (currYear > year) {
            paint.setColor(theme.colorDisableDay());
        } else if (currYear == year) {
            // 此处month没有+1，故currMonth非当前月份
            int currMonth = calendar.get(Calendar.MONTH);
            if (currMonth > month) {
                paint.setColor(theme.colorDisableDay());
            } else if (currMonth == month) {
                int currDay = calendar.get(Calendar.DAY_OF_MONTH);
                if (currDay > day) {
                    paint.setColor(theme.colorDisableDay());
                }
            }
        }
    }
}
