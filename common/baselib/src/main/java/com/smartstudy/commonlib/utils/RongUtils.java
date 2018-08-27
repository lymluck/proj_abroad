package com.smartstudy.commonlib.utils;

import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author louis
 * @date on 2018/1/29
 * @describe RongIM相关类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class RongUtils {

    public static String getMsgExtra(MessageContent messageContent) {
        String extra = null;
        if (TextMessage.class.isAssignableFrom(messageContent.getClass())) {
            extra = ((TextMessage) messageContent).getExtra();
        }
        if (ImageMessage.class.isAssignableFrom(messageContent.getClass())) {
            extra = ((ImageMessage) messageContent).getExtra();
        }
        if (LocationMessage.class.isAssignableFrom(messageContent.getClass())) {
            extra = ((LocationMessage) messageContent).getExtra();
        }
        if (FileMessage.class.isAssignableFrom(messageContent.getClass())) {
            extra = ((FileMessage) messageContent).getExtra();
        }
        if (VoiceMessage.class.isAssignableFrom(messageContent.getClass())) {
            extra = ((VoiceMessage) messageContent).getExtra();
        }
        return extra;
    }
}
