package com.smartstudy.xxd.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.mvp.contract.ChatDetailContract;
import com.smartstudy.commonlib.mvp.model.ConsultantsModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by yqy on 2017/12/26.
 */

public class ChatDetailPresenter implements ChatDetailContract.Presenter {

    private ChatDetailContract.View view;

    public ChatDetailPresenter(ChatDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void getTeacherInfo(final String userId) {
        ConsultantsModel.getConsultantInfo(userId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                ConsultantsInfo data = JSON.parseObject(result, ConsultantsInfo.class);
                if (data != null) {
                    String cacheUrl = Utils.getCacheUrl(data.getAvatar(), 64, 64);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(data.getImUserId(), data.getName(), TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl)));
                    view.getTeacherInfoSuccess(data);
                    data = null;
                }
            }
        });
    }
}
