package com.smartstudy.xxd.ui.fragment;

import android.content.Context;

import com.smartstudy.commonlib.ui.adapter.XxdConversationAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.model.Event;
import io.rong.imkit.widget.adapter.MessageListAdapter;

/**
 * @author louis
 * @date on 2018/2/1
 * @describe 自定义聊天列表形式
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class XxdConversationFragment extends ConversationFragment {

    private List<Event.OnReceiveMessageEvent> myEvents = new ArrayList<>();
    private boolean isPause;

    @Override
    public MessageListAdapter onResolveAdapter(Context context) {
        return new XxdConversationAdapter(context);
    }

    @Override
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        if (isPause) {
            myEvents.add(event);
        } else {
            super.onEventMainThread(event);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        if (myEvents != null && myEvents.size() > 0) {
            for (Iterator<Event.OnReceiveMessageEvent> it = myEvents.iterator(); it.hasNext(); ) {
                Event.OnReceiveMessageEvent event = it.next();
                onEventMainThread(event);
                it.remove();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myEvents != null) {
            myEvents.clear();
            myEvents = null;
        }
    }
}

