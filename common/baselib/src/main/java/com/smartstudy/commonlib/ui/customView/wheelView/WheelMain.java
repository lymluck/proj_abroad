package com.smartstudy.commonlib.ui.customView.wheelView;

import android.text.TextUtils;
import android.view.View;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.adapter.NumericWheelAdapter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView hour_start;
    private WheelView hour_end;
    private WheelView minute_start;
    private WheelView minute_end;
    private static int START_YEAR = 2017, END_YEAR = 2017;
    private List<String> list_big;
    private List<String> list_little;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getSTART_YEAR() {
        return START_YEAR;
    }

    public static void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public static int getEND_YEAR() {
        return END_YEAR;
    }

    public static void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public WheelMain(View view) {
        super();
        this.view = view;
        setView(view);
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(String flag, String initValue) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        if ("order_date".equals(flag)) {
            // 年
            wv_year = (WheelView) view.findViewById(R.id.year);
            wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
            wv_year.setCyclic(false);// 可循环滚动
            wv_year.setLabel("年");// 添加文字
            wv_year.setEnabled(false);
            wv_year.setFocusable(false);
            wv_year.addChangingListener(wheelListener_year);

            // 月
            wv_month = (WheelView) view.findViewById(R.id.month);
            wv_month.setAdapter(new NumericWheelAdapter(month + 1, 12));
            wv_month.setLabel("月");
            wv_month.addChangingListener(wheelListener_month);

            // 日
            wv_day = (WheelView) view.findViewById(R.id.day);
            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_day.setLabel("日");
            if (!TextUtils.isEmpty(initValue)) {
                wv_year.setCurrentItem(year - Integer.parseInt(initValue.split("-")[0], 10));// 初始化时显示的数据
                wv_month.setCurrentItem(Integer.parseInt(initValue.split("-")[1], 10) - month - 1);
                wv_day.setCurrentItem(Integer.parseInt(initValue.split("-")[2], 10) - 1);
            } else {
                wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
                wv_month.setCurrentItem(month);
                wv_day.setCurrentItem(day);
            }
        } else if ("order_time".equals(flag)) {
            hour_start = (WheelView) view.findViewById(R.id.hour_start);
            hour_start.setAdapter(new NumericWheelAdapter(0, 23));
            hour_start.setCyclic(true);
            hour_start.setLabel("时");

            minute_start = (WheelView) view.findViewById(R.id.minute_start);
            minute_start.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
            minute_start.setCyclic(true);
            minute_start.setLabel("分");

            hour_end = (WheelView) view.findViewById(R.id.hour_end);
            hour_end.setAdapter(new NumericWheelAdapter(0, 23));
            hour_end.setCyclic(true);
            hour_end.setLabel("时");

            minute_end = (WheelView) view.findViewById(R.id.minute_end);
            minute_end.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
            minute_end.setCyclic(true);
            minute_end.setLabel("分");
            if (!TextUtils.isEmpty(initValue)) {
                hour_start.setCurrentItem(Integer.parseInt(initValue.split("-")[0].split(":")[0], 10));
                minute_start.setCurrentItem(Integer.parseInt(initValue.split("-")[0].split(":")[1], 10));
                hour_end.setCurrentItem(Integer.parseInt(initValue.split("-")[1].split(":")[0], 10));
                minute_end.setCurrentItem(Integer.parseInt(initValue.split("-")[1].split(":")[1], 10));
            } else {
                hour_start.setCurrentItem(0);
                minute_start.setCurrentItem(0);
                hour_end.setCurrentItem(23);
                minute_end.setCurrentItem(59);
            }
        }
    }

    // 添加"年"监听
    OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year_num = newValue + START_YEAR;
            int curr_index = wv_day.getCurrentIndex();
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                if (curr_index > 30) {
                    wv_day.setCurrentItem(0);
                }
            } else if (list_little.contains(String.valueOf(wv_month
                    .getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                if (curr_index > 29) {
                    wv_day.setCurrentItem(0);
                }
            } else {
                if ((year_num % 4 == 0 && year_num % 100 != 0)
                        || year_num % 400 == 0) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    if (curr_index > 28) {
                        wv_day.setCurrentItem(0);
                    }
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    if (curr_index > 27) {
                        wv_day.setCurrentItem(0);
                    }
                }
            }
        }
    };
    // 添加"月"监听
    OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int month_num = newValue + Calendar.getInstance().get(Calendar.MONTH) + 1;
            int curr_index = wv_day.getCurrentIndex();
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                if (curr_index > 30) {
                    wv_day.setCurrentItem(0);
                }
            } else if (list_little.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                if (curr_index > 29) {
                    wv_day.setCurrentItem(0);
                }
            } else {
                if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                        .getCurrentItem() + START_YEAR) % 100 != 0)
                        || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    if (curr_index > 28) {
                        wv_day.setCurrentItem(0);
                    }
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    if (curr_index > 27) {
                        wv_day.setCurrentItem(0);
                    }
                }
            }
        }
    };

    public String getDateTime() {
        StringBuffer sb = new StringBuffer();
        sb.append(wv_year.getCurrentItem() + START_YEAR).append("-")
                .append(String.format("%02d", (wv_month.getCurrentItem() + 1))).append("-")
                .append(String.format("%02d", (wv_day.getCurrentItem() + 1))).append(" ");
//                .append(String.format("%02d", wv_hours.getCurrentItem())).append(":")
//                .append(String.format("%02d", wv_mins.getCurrentItem()));
        return sb.toString();
    }

    public String getDate() {
        StringBuffer sb = new StringBuffer();
        sb.append(wv_year.getCurrentItem() + START_YEAR).append("-")
                .append(String.format("%02d", (wv_month.getCurrentItem() + Calendar.getInstance().get(Calendar.MONTH) + 1))).append("-")
                .append(String.format("%02d", (wv_day.getCurrentItem() + 1)));
        return sb.toString();
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%02d", hour_start.getCurrentItem())).append(":")
                .append(String.format("%02d", minute_start.getCurrentItem())).append("-")
                .append(String.format("%02d", hour_end.getCurrentItem())).append(":")
                .append(String.format("%02d", minute_end.getCurrentItem()));
        return sb.toString();
    }
}
