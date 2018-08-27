package com.smartstudy.commonlib.mvp.base;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class BaseModel {

    public static void getVersion(BaseCallback baseCallback) {
        RequestManager.getInstance().doGet(ParameterUtils.ONLY_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_APP_VERSION, AppUtils.getVersionName()));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, baseCallback);
    }

    public static void PhoneCode(final String phone, BaseCallback baseCallback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_PHONE_CODE);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                return params;
            }
        }, baseCallback);
    }
}
