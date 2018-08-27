package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/8/18.
 */

public class VisaModel {

    public static void getVisas(final String regionId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_VISA);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(regionId)) {
                    map.put("regionId", regionId);
                }
                return map;
            }
        }, callback);
    }

    public static void getVisaQas(final String regionId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_VISA_QA);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(regionId)) {
                    map.put("regionId", regionId);
                }
                return map;
            }
        }, callback);
    }

    public static void getVisaAddr(final String regionId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_VISA_ADDR);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(regionId)) {
                    map.put("countryId", regionId);
                }
                return map;
            }
        }, callback);
    }
}
