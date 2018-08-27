package com.smartstudy.commonlib.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * @author louis
 * @date on 2018/1/16
 * @describe 统计分析工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class StatisticUtils {

    /**
     * @param context 上下文
     * @param eventId 事件Id，对应umeng自定义事件
     */
    public static void actionEvent(Context context, String eventId) {
        if (!Utils.isApkInDebug()) {
            MobclickAgent.onEvent(context, eventId);
        }
    }
}
