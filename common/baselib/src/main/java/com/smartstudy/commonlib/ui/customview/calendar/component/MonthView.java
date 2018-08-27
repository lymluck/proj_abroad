package com.smartstudy.commonlib.ui.customview.calendar.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.customview.calendar.entity.CalendarInfo;
import com.smartstudy.commonlib.ui.customview.calendar.theme.IDayTheme;
import com.smartstudy.commonlib.ui.customview.calendar.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public abstract class MonthView extends View {

    public static String DEFAULT_FILTER = "筛选全部";
    public static String MY_EXAM = "我的计划";

    private Context mContext;
    protected int NUM_COLUMNS = 7;
    protected int NUM_ROWS = 6;
    protected Paint paint;
    protected IDayTheme theme;
    private IDateClick dateClick;
    protected int[][] daysString;
    protected float columnSize, rowSize, baseRowSize;
    protected float density;
    protected int selDay, selMonth;
    protected int currYear, currMonth, currDay;
    private int downX = 0, downY = 0;
    protected List<CalendarInfo> calendarInfos = new ArrayList<>();
    private String exam;
    private Calendar calendar;

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        density = getResources().getDisplayMetrics().density;
        calendar = Calendar.getInstance();
        currDay = calendar.get(Calendar.DATE);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        createTheme();
        baseRowSize = rowSize = theme == null ? 70 : theme.dateHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (300 * density);
        }
        NUM_ROWS = getMonthRowNumber(currYear, currMonth);
        //本来是想根据每月的行数，动态改变控件高度，现在为了使滑动的左右两边效果相同，不适用getMonthRowNumber();
        int heightSize = (int) (NUM_ROWS * baseRowSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制当前月份
        drawDate(canvas, currYear, currMonth);
    }

    private void drawDate(Canvas canvas, int year, int month) {
        NUM_ROWS = getMonthRowNumber(year, month);
        columnSize = getWidth() * 1.0F / NUM_COLUMNS;
        rowSize = getHeight() * 1.0F / NUM_ROWS;
        daysString = new int[6][7];
        int mMonthDays = DateUtils.getMonthDays(year, month);
        int weekNumber = DateUtils.getFirstDayWeek(year, month);
        int column, row;
        for (int day = 0; day < mMonthDays; day++) {
            column = (day + weekNumber - 1) % 7;
            row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            drawBG(canvas, column, row, year, month, daysString[row][column], calendar);
            drawDecor(canvas, column, row, year, month, daysString[row][column], calendar);
            drawRest(canvas, column, row, year, month, daysString[row][column], calendar);
            drawText(canvas, column, row, year, month, daysString[row][column], calendar);
        }
    }

    protected abstract void drawBG(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar);

    protected abstract void drawDecor(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar);

    protected abstract void drawRest(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar);

    protected abstract void drawText(Canvas canvas, int column, int row, int year, int month, int day, Calendar calendar);

    /**
     * 实例化Theme
     */
    protected abstract void createTheme();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {
                    //点击事件
                    int x = (upX + downX) / 2;
                    int y = (upY + downY) / 2;
                    int row = (int) (y / rowSize);
                    int column = (int) (x / columnSize);
                    selDay = daysString[row][column];
                    selMonth = currMonth + 1;
                    CalendarInfo info = iscalendarInfo(currYear, currMonth, selDay);
                    String des = info != null ? info.des : "";
                    if (!TextUtils.isEmpty(des)) {
                        if (DEFAULT_FILTER.equals(exam)
                                || (MY_EXAM.equals(exam) && info.select)
                                || des.equals(exam)) {
                            if (!isDateDisable(calendar, currYear, currMonth, selDay)) {
                                performClick();
                                doClickAction(info, String.format(mContext.getString(R.string.year_month_day),
                                        currYear, selMonth, selDay, DateUtils.getWeekName(column)), null);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    protected int getMonthRowNumber(int year, int month) {
        int monthDays = DateUtils.getMonthDays(year, month);
        int weekNumber = DateUtils.getFirstDayWeek(year, month);
        return (monthDays + weekNumber - 1) % 7 == 0 ? (monthDays + weekNumber - 1) / 7 : (monthDays + weekNumber - 1) / 7 + 1;
    }

    public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
        this.calendarInfos = calendarInfos;
        invalidate();
    }

    /**
     * 判断是否为事务天数,通过获取desc来辨别
     *
     * @param day
     * @return
     */
    protected CalendarInfo iscalendarInfo(int year, int month, int day) {
        if (calendarInfos == null || calendarInfos.size() == 0) {
            return null;
        }
        for (CalendarInfo calendarInfo : calendarInfos) {
            if (calendarInfo.day == day && calendarInfo.month == month + 1 && calendarInfo.year == year) {
                return calendarInfo;
            }
        }
        return null;
    }

    /**
     * 执行点击事件
     *
     * @param info
     * @param dateStr
     * @param history
     */
    private void doClickAction(CalendarInfo info, String dateStr, List<String> history) {
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            dateClick.onClickOnDate(info, dateStr, history);
        }
    }

    public void setDateClick(IDateClick dateClick) {
        this.dateClick = dateClick;
    }

    public interface IDateClick {
        void onClickOnDate(CalendarInfo info, String dateStr, List<String> history);
    }

    /**
     * 设置样式
     *
     * @param theme
     */
    public void setTheme(IDayTheme theme) {
        this.theme = theme;
        invalidate();
    }


    public int getCurrYear() {
        return currYear;
    }

    public void setCurrYear(int currYear) {
        this.currYear = currYear;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public int getCurrMonth() {
        return currMonth;
    }

    public void setCurrMonth(int currMonth) {
        this.currMonth = currMonth;
    }

    public int getSelDay() {
        return selDay;
    }

    /**
     * 判断日期是否过期，过期不可点击
     *
     * @param calendar
     * @param year
     * @param month
     * @param day
     */
    private boolean isDateDisable(Calendar calendar, int year, int month, int day) {
        int currYear = calendar.get(Calendar.YEAR);
        if (currYear > year) {
            return true;
        } else if (currYear == year) {
            // 此处month没有+1，故currMonth非当前月份，当前月份是currMonth+1
            int currMonth = calendar.get(Calendar.MONTH);
            if (currMonth > month) {
                return true;
            } else if (currMonth == month) {
                int currDay = calendar.get(Calendar.DAY_OF_MONTH);
                if (currDay > day) {
                    return true;
                }
            }
        }
        return false;
    }
}
