package com.smartstudy.xxd.receiver;

import android.content.Context;

import com.smartstudy.commonlib.utils.LogUtils;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @author louis
 * @date on 2018/1/30
 * @describe Rong消息通知接收器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class RongNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }
}
