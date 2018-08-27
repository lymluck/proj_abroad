package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class SpecialListModel {

    public static void getProgram(final String typeId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.CACHED_ELSE_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_PROGRAM);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("hierarchy", "true");
                params.put("typeId", typeId);
                return params;
            }
        }, callback);
    }

    public static void getSpecialData(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.CACHED_ELSE_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SPECIAL_DATA);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("hierarchy", "true");
                return params;
            }
        }, callback);
    }

    public static void getCommandSchool(int cacheType, final String id, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COMMAND_SCHOOL, id));
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }

    public static void getHotMajor(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_HOT_MAJOR);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
