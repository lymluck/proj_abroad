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

public class MajorModel {

    public static void getMajorInfo(final String id,
                                    BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_MAJOR_INFO, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getMajorProgramRankList(int cacheType, final String categoryId, final int page,
                                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MAJOR_PROGRAM_RANK);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("categoryId", categoryId);
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }

    public static void getProgramInfo(final String id,
                                      BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_MAJOR_PROGRAM_INFO, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getProgramList(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MAJOR_PROGRAM_LIST);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
