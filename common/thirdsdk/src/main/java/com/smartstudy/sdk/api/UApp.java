package com.smartstudy.sdk.api;

import android.content.Context;

import com.smartstudy.sdk.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * @author louis
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class UApp {

    public static void init(Context context, String appKey, String channel) {
        UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public static void debug() {
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setCatchUncaughtExceptions(false);
    }

    public static void logout() {
        MobclickAgent.onProfileSignOff();
    }

    public static void login(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }


    /**
     * @param context 上下文
     * @param eventId 事件Id，对应umeng自定义事件
     */
    public static void actionEvent(Context context, String eventId) {
        if (!Utils.isApkInDebug(context)) {
            MobclickAgent.onEvent(context, eventId);
        }
    }

    public static void pageSessionStart(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void pageSessionEnd(Context context) {
        MobclickAgent.onPause(context);
    }
}
