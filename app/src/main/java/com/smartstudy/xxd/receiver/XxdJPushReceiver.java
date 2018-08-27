package com.smartstudy.xxd.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.meiqia.core.MQManager;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.ReloginActivity;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.router.RouteCallback;
import com.smartstudy.router.RouteResult;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.ui.activity.MainActivity;
import com.smartstudy.xxd.ui.activity.MsgCenterActivity;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class XxdJPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        JSONObject objExtra = null;
        try {
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (extra != null) {
                objExtra = new JSONObject(extra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            final String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            MQManager mqManager = MQManager.getInstance(context);
            mqManager.registerDeviceToken(regId, null);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            receiveMsg(context, type, objExtra);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            receiveNotify(objExtra);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            notifyOpened(context, objExtra);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void receiveMsg(Context context, String type, JSONObject objExtra) {
        switch (type) {
            case "session_kickout":
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + "退出");
                RongIM.getInstance().logout();
                context.startActivity(new Intent(context, ReloginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case "counsellor_block":
                String imUserId = objExtra.optString("imUserId");
                String blockIds = (String) SPCacheUtils.get("blockIds", "");
                if (TextUtils.isEmpty(blockIds)) {
                    blockIds = imUserId;
                } else {
                    if (!blockIds.contains(imUserId)) {
                        blockIds += "," + imUserId;
                    }
                }
                SPCacheUtils.put("blockIds", blockIds);
                break;
            case "counsellor_unblock":
                String imUserIdUn = objExtra.optString("imUserId");
                String blockIdsUn = (String) SPCacheUtils.get("blockIds", "");
                String newIds = "";
                if (!TextUtils.isEmpty(blockIdsUn)) {
                    String[] ids = blockIdsUn.split(",");
                    for (String id : ids) {
                        if (!imUserIdUn.equals(id)) {
                            newIds += "," + id;
                        }
                    }
                }
                if (TextUtils.isEmpty(newIds)) {
                    SPCacheUtils.remove("blockIds");
                } else {
                    SPCacheUtils.put("blockIds", newIds.substring(1));
                }
                break;
            default:
                break;
        }
    }

    //接收到通知
    private void receiveNotify(JSONObject objExtra) {
        //判断消息来源
        if (objExtra != null) {
            String link = objExtra.optString("link");
            String type = link.substring(link.lastIndexOf("/") + 1, link.length());
            if (!TextUtils.isEmpty(link)) {
                switch (type) {
                    case "customer_service":
                        int meiqiaUnRead = (int) SPCacheUtils.get("meiqia_unread", 0);
                        ++meiqiaUnRead;
                        SPCacheUtils.put("meiqia_unread", meiqiaUnRead);
                        EventBus.getDefault().post(new Event.MsgMeiQiaEvent(meiqiaUnRead));
                        break;
                    default:
                        int xxdUnRead = (int) SPCacheUtils.get("xxd_unread", 0);
                        ++xxdUnRead;
                        SPCacheUtils.put("xxd_unread", xxdUnRead);
                        EventBus.getDefault().post(new Event.MsgXxdEvent(xxdUnRead));
                        break;
                }
            } else {
                if ("xxd".equals(objExtra.optString("from"))) {
                    int xxdUnRead = (int) SPCacheUtils.get("xxd_unread", 0);
                    ++xxdUnRead;
                    SPCacheUtils.put("xxd_unread", xxdUnRead);
                    EventBus.getDefault().post(new Event.MsgXxdEvent(xxdUnRead));
                } else if ("meiqia".equals(objExtra.optString("from"))) {
                    int meiqiaUnRead = (int) SPCacheUtils.get("meiqia_unread", 0);
                    ++meiqiaUnRead;
                    SPCacheUtils.put("meiqia_unread", meiqiaUnRead);
                    EventBus.getDefault().post(new Event.MsgMeiQiaEvent(meiqiaUnRead));
                }
            }
        }
    }

    //用户点击了通知
    private void notifyOpened(final Context context, JSONObject objExtra) {
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
            //判断消息来源
            if (objExtra != null) {
                String link = objExtra.optString("link");
                if (!TextUtils.isEmpty(link)) {
                    if (link.startsWith("http") || link.startsWith("https")) {
                        //调用webview打开网址
                        Router.build("showWebView").with("web_url", link).with("url_action", "get")
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).go(context);
                    } else {
                        if (link.contains("course?") || link.contains("school?")) {
                            //构造一个StringBuilder对象
                            StringBuilder sb = new StringBuilder(link);
                            //在指定的位置1，插入指定的字符串
                            sb.insert(link.indexOf("?"), "/detail");
                            link = sb.toString();
                        }
                        Router.build(link).callback(new RouteCallback() {
                            @Override
                            public void callback(RouteResult state, Uri uri, String message) {
                                String url = uri.toString();
                                if (url.contains("?")) {
                                    url = url.substring(0, url.indexOf("?"));
                                }
                                switch (url) {
                                    case "xuanxiaodi://customer_service":
                                        //打开美洽
                                        SPCacheUtils.put("meiqia_unread", 0);
                                        JPushInterface.clearAllNotifications(context);
                                        Intent toMeiQia = new MQIntentBuilder(context).build();
                                        toMeiQia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(toMeiQia);
                                        break;
                                    case "xuanxiaodi://question":
                                        Intent toQa = new Intent(context, ShowWebViewActivity.class);
                                        toQa.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), uri.getQueryParameter("id")));
                                        toQa.putExtra("url_action", "get");
                                        toQa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(toQa);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).go(context);
                    }
                } else {
                    Intent toWhere = new Intent(context, MainActivity.class);
                    if ("meiqia".equals(objExtra.optString("from"))) {
                        toWhere = new MQIntentBuilder(context).build();
                    } else if ("xxd".equals(objExtra.optString("from"))) {
                        toWhere = new Intent(context, MsgCenterActivity.class);
                        SPCacheUtils.put("xxd_unread", 0);
                    }
                    toWhere.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(toWhere);
                }
            }
        } else {
            Intent toLogin = new Intent(context, LoginActivity.class);
            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toLogin.putExtra("toMain", true);
            context.startActivity(toLogin);
        }
    }

}
