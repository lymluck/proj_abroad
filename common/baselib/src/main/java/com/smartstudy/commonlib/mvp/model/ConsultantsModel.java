package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/12/21.
 */

public class ConsultantsModel {

    public static void getConsultantsList(int cacheType, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COUNSELLOR_LIST);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getConsultantInfo(final String userId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COUNSELLOR_INFO);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                try {
                    params.put("imUserId", URLEncoder.encode(userId, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return params;
            }
        }, callback);
    }
}
