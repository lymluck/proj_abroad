package com.smartstudy.commonlib.utils;

/**
 * 快速点击拦截
 * Created by louis on 17/4/17.
 */

public class NoFastClickUtils {

    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 300;//时间间隔

    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isFastClick;//是否允许点击
        if (currentTime - lastClickTime > spaceTime) {
            isFastClick = false;
        } else {
            isFastClick = true;
        }
        lastClickTime = currentTime;
        return isFastClick;
    }
}
