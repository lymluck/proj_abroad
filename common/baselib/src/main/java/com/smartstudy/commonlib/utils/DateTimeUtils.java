package com.smartstudy.commonlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.smartstudy.commonlib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    // 获取系统当前时间
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static int getNowYear() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        return year;
    }

    // 获取系统当前时间
    public static String getTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    // 获取系统当前时间 yyyy-MM-dd
    public static String getTimeOnlyMd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault());
        return format.format(new Date(time));
    }

    // 获取系统当前时间 yyyy-MM-dd
    public static String getTimeOnlyMdhm() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    // 获取系统昨天时间 yyyy-MM-dd
    @SuppressLint("SimpleDateFormat")
    public static String getYestedayTimeOnlyMd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static String dateStrFormat(String dateTime, SimpleDateFormat sdf_in, SimpleDateFormat sdf_out) {
        String str_date = "";
        try {
            if (!"".equals(dateTime)) {
                Date date = sdf_in.parse(dateTime);
                str_date = sdf_out.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str_date;
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(new Date(time));
    }

    public static boolean isOverNowTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date nowDate = sdf.parse(getTime());
            Date date_time = sdf.parse(time);
            return date_time.getTime() < nowDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDetailTime(Context mContext, String time_str, boolean simple) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = simpleDateFormat.parse(getTimeOnlyMd());
        Date date2 = simpleDateFormat.parse(time_str);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(format2.parse(time_str));
        String date3 = simpleDateFormat.format(format2.parse(time_str));
        int hour = Integer.parseInt(date.substring(0, 2));
        if (date1.getDate() - date2.getDate() == 0) {
            if (simple) {
                return date;
            } else {
                if (hour < 6)
                    return mContext.getString(R.string.before_dawn) + date;
                else if (hour < 12)
                    return mContext.getString(R.string.morning) + date;
                else if (hour < 18)
                    return mContext.getString(R.string.afternoon) + date;
                else return mContext.getString(R.string.night) + date;
            }
        } else if (date1.getDate() - date2.getDate() == 1) {
            if (simple) {
                return mContext.getString(R.string.yesterday);
            } else {
                if (hour < 6)
                    return mContext.getString(R.string.yesterday) + " " + mContext.getString(R.string.before_dawn) + date;
                else if (hour < 12)
                    return mContext.getString(R.string.yesterday) + " " + mContext.getString(R.string.morning) + date;
                else if (hour < 18)
                    return mContext.getString(R.string.yesterday) + " " + mContext.getString(R.string.afternoon) + date;
                else
                    return mContext.getString(R.string.yesterday) + " " + mContext.getString(R.string.night) + date;
            }
        } else if (date1.getYear() == date2.getYear()) {
            if (simple) {
                return date2.getMonth() + 1 + mContext.getString(R.string.month) + date2.getDate() + mContext.getString(R.string.day);
            } else {
                if (hour < 6)
                    return date2.getMonth() + 1 + mContext.getString(R.string.month) + date2.getDate() + mContext.getString(R.string.day) + " " + mContext.getString(R.string.before_dawn) + date;
                else if (hour < 12)
                    return date2.getMonth() + 1 + mContext.getString(R.string.month) + date2.getDate() + mContext.getString(R.string.day) + " " + mContext.getString(R.string.morning) + date;
                else if (hour < 18)
                    return date2.getMonth() + 1 + mContext.getString(R.string.month) + date2.getDate() + mContext.getString(R.string.day) + " " + mContext.getString(R.string.afternoon) + date;
                else
                    return date2.getMonth() + 1 + mContext.getString(R.string.month) + date2.getDate() + mContext.getString(R.string.day) + " " + mContext.getString(R.string.night) + date;
            }

        } else {
            if (simple) {
                return date3;
            } else {
                if (hour < 6) {
                    return date3 + " " + mContext.getString(R.string.before_dawn) + date;
                } else if (hour < 12) {
                    return date3 + " " + mContext.getString(R.string.morning) + date;
                } else if (hour < 18) {

                    return date3 + " " + mContext.getString(R.string.afternoon) + date;
                } else {
                    return date3 + " " + mContext.getString(R.string.night) + date;
                }
            }
        }
    }

    public static String secToTime(long time) {
        String timeStr;
        long hour;
        long minute;
        long second;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
