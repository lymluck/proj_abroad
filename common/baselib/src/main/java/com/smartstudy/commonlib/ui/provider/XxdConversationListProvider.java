package com.smartstudy.commonlib.ui.provider;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.utils.RongUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.Utils;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.PrivateConversationProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by yqy on 2017/12/29.
 */
@ConversationProviderTag(
        conversationType = "private",
        portraitPosition = 1
)
public class XxdConversationListProvider extends PrivateConversationProvider {

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View result = super.newView(context, viewGroup);
        XxdConversationListProvider.MyHolder holder = new XxdConversationListProvider.MyHolder();
        holder.tagTitle = result.findViewById(R.id.tag_title);
        holder.tagYear = result.findViewById(R.id.tag_year);
        result.setTag(R.id.my_holder, holder);
        return result;
    }

    @Override
    public void bindView(View view, int position, final UIConversation data) {
        super.bindView(view, position, data);
        //默认的会话列表回执不显示，样式不合适，需要的话自己定义
        ((ViewHolder) view.getTag()).readStatus.setVisibility(View.GONE);
        //自定义部分
        final XxdConversationListProvider.MyHolder holder = (XxdConversationListProvider.MyHolder) view.getTag(R.id.my_holder);
        if (data == null) {
            holder.tagTitle.setVisibility(View.GONE);
            holder.tagYear.setVisibility(View.GONE);
        } else {
            String extra = RongUtils.getMsgExtra(data.getMessageContent());
            if (!TextUtils.isEmpty(extra)) {
                final JSONObject object = JSON.parseObject(extra);
                if (!object.containsKey("title")) {
                    ConsultantsInfo info = JSON.parseObject(SPCacheUtils.get("Rong:" + data.getConversationTargetId(), "").toString(), ConsultantsInfo.class);
                    if (info != null) {
                        handleTag(holder, info.getTitle(), info.getYearsOfWorking());
                    } else {
                        getExtraFromCache(holder, data);
                    }
                } else {
                    handleTag(holder, object.getString("title"), object.getString("workyear"));
                }
            } else {
                getExtraFromCache(holder, data);
            }
        }
    }

    @Override
    public Spannable getSummary(UIConversation data) {
        return super.getSummary(data);
    }

    @Override
    public String getTitle(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (userInfo == null) {
            ConsultantsInfo info = JSON.parseObject(SPCacheUtils.get("Rong:" + userId, "").toString(), ConsultantsInfo.class);
            if (info != null) {
                return info.getName();
            } else {
                return userId;
            }
        } else {
            return userInfo.getName();
        }
    }

    @Override
    public Uri getPortraitUri(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (userInfo == null) {
            ConsultantsInfo info = JSON.parseObject(SPCacheUtils.get("Rong:" + userId, "").toString(), ConsultantsInfo.class);
            if (info != null) {
                String avatar = Utils.getCacheUrl(info.getAvatar(), 64, 64);
                return TextUtils.isEmpty(avatar) ? null : Uri.parse(avatar);
            } else {
                return null;
            }
        } else {
            return userInfo.getPortraitUri();
        }
    }

    private void handleTag(XxdConversationListProvider.MyHolder holder, String title, String year) {
        if (!TextUtils.isEmpty(title.trim())) {
            holder.tagTitle.setVisibility(View.VISIBLE);
            holder.tagTitle.setText(title);
        } else {
            holder.tagTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(year.trim())) {
            holder.tagYear.setVisibility(View.VISIBLE);
            holder.tagYear.setText("从业" + year + "年");
        } else {
            holder.tagYear.setVisibility(View.GONE);
        }
    }

    private void getExtraFromCache(final XxdConversationListProvider.MyHolder holder, UIConversation data) {
        RongIM.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, data.getConversationTargetId(), data.getLatestMessageId(), 100, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                for (Message message : messages) {
                    String extra = RongUtils.getMsgExtra(message.getContent());
                    JSONObject obj_msg = JSON.parseObject(extra);
                    if (obj_msg != null && obj_msg.containsKey("title")) {
                        handleTag(holder, obj_msg.getString("title"), obj_msg.getString("workyear"));
                        break;
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    protected class MyHolder {
        public TextView tagTitle;
        public TextView tagYear;

        protected MyHolder() {
        }
    }
}
