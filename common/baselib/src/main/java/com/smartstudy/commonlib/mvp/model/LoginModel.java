package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class LoginModel extends BaseModel {
    public static void phoneCodeLogin(final String phone,
                                      final String code, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_CODE_LOGIN);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                params.put("phone", phone);
                params.put("captcha", code);
                params.put("from", "app");
                params.put("sourceAction", "安卓用户注册");
                params.put("infoVersion", "v2");
                return params;
            }
        }, callback);
    }
}
