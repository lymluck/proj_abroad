package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class ExamDateModel {

    public static void getExamDate(int cacheType, final boolean plain, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_EXAM_DATE);
            }

            @Override
            public Map getParams() {
                Map param = new HashMap();
                param.put("plain", plain ? "true" : "false");
                return param;
            }
        }, callback);
    }

    public static void joinExam(final int id, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_JOIN_EXAM, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void delExam(final int id, BaseCallback<String> callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_JOIN_EXAM, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
