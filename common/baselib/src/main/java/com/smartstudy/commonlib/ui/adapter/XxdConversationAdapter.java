package com.smartstudy.commonlib.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RecallNotificationMessage;

/**
 * @author louis
 * @date on 2018/2/1
 * @describe 自定义聊天列表适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class XxdConversationAdapter extends MessageListAdapter {
    public XxdConversationAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindView(View v, int position, UIMessage data) {
        super.bindView(v, position, data);
        if (data != null) {
            //逻辑前提是配置中开启了使用阅读回执功能
            MessageListAdapter.ViewHolder holder = (MessageListAdapter.ViewHolder) v.getTag();
            Class clazz = data.getContent().getClass();
            if (RecallNotificationMessage.class.isAssignableFrom(clazz)
                    || InformationNotificationMessage.class.isAssignableFrom(clazz)) {
                holder.readReceipt.setVisibility(View.GONE);
            } else {
                if (data.getMessageDirection() == Message.MessageDirection.SEND) {
                    if (data.getSentStatus() == Message.SentStatus.SENT) {
                        holder.readReceipt.setVisibility(View.VISIBLE);
                        holder.readReceipt.setText("未读");
                        holder.readReceipt.setTextColor(Color.parseColor("#078CF1"));
                    } else if (data.getSentStatus() == Message.SentStatus.READ) {
                        holder.readReceipt.setVisibility(View.VISIBLE);
                        holder.readReceipt.setText("已读");
                        holder.readReceipt.setTextColor(Color.parseColor("#b2949BA1"));
                    } else {
                        holder.readReceipt.setVisibility(View.GONE);
                    }
                } else {
                    holder.readReceipt.setVisibility(View.GONE);
                }
            }
        }
    }
}
