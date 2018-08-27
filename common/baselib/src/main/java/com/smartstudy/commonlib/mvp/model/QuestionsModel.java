package com.smartstudy.commonlib.mvp.model;

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

public class QuestionsModel {

    public static void getQuestions(int cacheType, final boolean answered, final int page,
                                    BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_QUESTS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("answered", answered + "");
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getQuestions(int cacheType, final String keyword, final boolean answered, final int page,
                                    BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_QUESTS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("answered", answered + "");
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getMyQuestions(int cacheType, final int page,
                                      BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MYQA);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getSchoolQa(final String schoolId, final int page,
                                   BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_QUESTS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                map.put("schoolId", schoolId);
                return map;
            }
        }, callback);
    }
}
