package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/7/13.
 */

public class QrCodeModel {

    public static void verify(final String str, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_VERIFY_QRCODE);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("token", str);
                return map;
            }
        }, callback);
    }

    public static void confirmLogin(final String str, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_CONFIRM_QRCODE);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("token", str);
                return map;
            }
        }, callback);
    }

}
