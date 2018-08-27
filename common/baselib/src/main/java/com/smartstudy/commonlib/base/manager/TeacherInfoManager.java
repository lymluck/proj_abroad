package com.smartstudy.commonlib.base.manager;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.mvp.model.ConsultantsModel;
import com.smartstudy.commonlib.utils.Utils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author louis
 * @date on 2018/1/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class TeacherInfoManager {

    private static TeacherInfoManager instance;

    public static TeacherInfoManager getInstance() {
        if (null == instance) {
            synchronized (TeacherInfoManager.class) {
                if (null == instance) {
                    instance = new TeacherInfoManager();
                }
            }
        }
        return instance;
    }

    public void getTeacherInfo(String userId, final TextView textView) {
        ConsultantsModel.getConsultantInfo(userId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
            }

            @Override
            public void onSuccess(String result) {
                ConsultantsInfo data = JSON.parseObject(result, ConsultantsInfo.class);
                if (data != null) {
                    if (textView != null) {
                        String company = data.getOrganization().getName();
                        if (!TextUtils.isEmpty(company)) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(company);
                        }
                    }
                    String cacheUrl = Utils.getCacheUrl(data.getAvatar(), 64, 64);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(data.getImUserId(), data.getName(), TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl)));
                    data = null;
                }
            }
        });
    }
}
