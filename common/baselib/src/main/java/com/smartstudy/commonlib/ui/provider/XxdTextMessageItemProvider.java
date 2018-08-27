package com.smartstudy.commonlib.ui.provider;

import android.graphics.Color;
import android.view.View;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AutoLinkTextView;
import io.rong.imkit.widget.provider.TextMessageItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * @author louis
 * @date on 2018/1/31
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
@ProviderTag(
        messageContent = TextMessage.class,
        showReadState = true
)
public class XxdTextMessageItemProvider extends TextMessageItemProvider {

    @Override
    public void bindView(View v, int position, TextMessage content, UIMessage data) {
        super.bindView(v, position, content, data);
        AutoLinkTextView textView = (AutoLinkTextView) v;
        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.parseColor("#26343F"));
        }
    }
}
