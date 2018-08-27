package com.smartstudy.commonlib.utils;

import android.content.Context;
import android.webkit.WebView;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.smartstudy.commonlib.entity.ChannelInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/5/3
 * @describe SensorsData
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class SensorsUtils {

    public static void initDebugMode(Context context, String channel) {
        SensorsDataAPI.sharedInstance(context, "http://sea.smartstudy.com/sa?project=default",
            SensorsDataAPI.DebugMode.DEBUG_OFF);
        //开启调试日志（ true 表示开启调试日志）
        SensorsDataAPI.sharedInstance().enableLog(false);
        initConfig(context, channel);
    }

    public static void initReleaseMode(Context context, String channel) {
        // 初始化
        SensorsDataAPI.sharedInstance(context, "http://sea.smartstudy.com/sa?project=production",
            SensorsDataAPI.DebugMode.DEBUG_OFF);
        initConfig(context, channel);
    }

    private static void initConfig(Context context, String channel) {
        // 设置匿名ID
        SensorsDataAPI.sharedInstance().identify(DeviceUtils.getUniquePsuedoID());
        try {
            ChannelInfo channelInfo = ChannelUtils.getChannelObject(channel);
            // 初始化SDK后，获取应用名称设置为公共属性
            JSONObject obj_super = new JSONObject();
            obj_super.put("platform", "AndroidApp");
            obj_super.put("product", AppUtils.getAppName(context) + "-APP");
            obj_super.put("pid", channelInfo.getId());
            obj_super.put("$latest_utm_source", channelInfo.getName());
            obj_super.put("$latest_utm_medium", channelInfo.getMedium());
            obj_super.put("$latest_utm_term", channelInfo.getTerm());
            obj_super.put("$latest_utm_content", channelInfo.getContent());
            obj_super.put("$latest_utm_campaign", channelInfo.getCampaign());
            SensorsDataAPI.sharedInstance().registerSuperProperties(obj_super);

            // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
            List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
            // $AppStart
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
            // $AppEnd
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
            // $AppViewScreen
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
            // $AppClick
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
            SensorsDataAPI.sharedInstance().enableAutoTrack(eventTypeList);

            // 初始化我们SDK后 调用这段代码，用于记录安装事件、渠道追踪。
            JSONObject installation = new JSONObject();
            // 这里的 DownloadChannel 负责记录下载商店的渠道。
            installation.put("DownloadChannel", channel);
            // 这里安装事件取名为 AppInstall。
            // 注意 由于要追踪不同渠道链接中投放的推广渠道，所以 Manifest 中不能按照“方案一”神策meta-data方式定制渠道信息，代码中也不能传入 $utm_ 开头的渠道字段！！！
            SensorsDataAPI.sharedInstance(context).trackInstallation("AppInstall", installation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // rn
        SensorsDataAPI.sharedInstance().enableReactNativeAutoTrack();
        // crash
        SensorsDataAPI.sharedInstance().trackAppCrash();
    }

    public static void trackLogin(String userId) {
        SensorsDataAPI.sharedInstance().login(userId);
    }

    public static void trackLogout() {
        SensorsDataAPI.sharedInstance().logout();
    }

    public static void trackWebView(WebView webView) {
        SensorsDataAPI.sharedInstance().showUpWebView(webView, true);
    }

    public static void trackCustomeEvent(String event, JSONObject object) {
        SensorsDataAPI.sharedInstance().track(event, object);
    }

    public static void trackCustomeEvent(String event) {
        SensorsDataAPI.sharedInstance().track(event);
    }

    public static void tackAppView(String name) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("$screen_name", name);
            SensorsDataAPI.sharedInstance().trackViewScreen(null, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
