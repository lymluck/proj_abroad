package com.smartstudy.commonlib.mvp.presenter;


import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.entity.ChatUserInfo;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.mvp.contract.MsgShareContract;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * @author louis
 * @date on 2018/2/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class MsgSharePresenter implements MsgShareContract.Presenter {
    private MsgShareContract.View view;

    public MsgSharePresenter(MsgShareContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getChatUsers() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<ChatUserInfo> datas = new ArrayList<>();
                ChatUserInfo info = null;
                for (Conversation conversation : conversations) {
                    info = new ChatUserInfo();
                    String userId = conversation.getTargetId();
                    info.setId(userId);
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
                    ConsultantsInfo consultantsInfo = JSON.parseObject(SPCacheUtils.get("Rong:" + userId, "").toString(), ConsultantsInfo.class);
                    if (TextUtils.isEmpty(conversation.getConversationTitle())) {
                        if (userInfo == null) {
                            if (consultantsInfo != null) {
                                info.setName(consultantsInfo.getName());
                            } else {
                                info.setName(userId);
                            }
                        } else {
                            info.setName(userInfo.getName());
                        }
                    } else {
                        info.setName(conversation.getConversationTitle());
                    }
                    if (TextUtils.isEmpty(conversation.getPortraitUrl().toString())) {
                        if (userInfo == null) {
                            if (consultantsInfo != null) {
                                String avatar = Utils.getCacheUrl(info.getAvatar(), 64, 64);
                                if (!TextUtils.isEmpty(avatar)) {
                                    info.setAvatar(avatar);
                                }
                            }
                        } else {
                            info.setAvatar(userInfo.getPortraitUri().toString());
                        }
                    } else {
                        info.setAvatar(conversation.getPortraitUrl().toString());
                    }
                    datas.add(info);
                    info = null;
                }
                view.showUsers(datas);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
