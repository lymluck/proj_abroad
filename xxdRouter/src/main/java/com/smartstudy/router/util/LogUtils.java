package com.smartstudy.router.util;

import android.util.Log;


/**
 * Created by admin on 2016/6/27.
 */
public class LogUtils {
    public static final String SEPARATOR = ",";
    private static boolean showDebug = false;
    private static String TAG = "router===";

    public static void showLog(boolean isDebug) {
        showDebug = isDebug;
    }


    public static void d(String message) {
        if (showDebug) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(TAG, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(String message) {
        if (showDebug) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(TAG, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String message) {
        if (showDebug) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(TAG, getLogInfo(stackTraceElement) + message);
        }
    }


    public static void e(String message) {
        if (showDebug) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(TAG, getLogInfo(stackTraceElement) + message);
        }
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
        logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
//        logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
//        logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
//        logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }

}
