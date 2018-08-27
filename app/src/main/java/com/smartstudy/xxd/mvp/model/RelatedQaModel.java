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

public class RelatedQaModel {

    public static void getQuestions(final String ques_id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_RELATED_QUES, ques_id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void qaHelpful(final String ques_id, final String result, final boolean helpful, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_RELATED_QUES_HELPFUL, ques_id));
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("result", result);
                params.put("helpful", helpful + "");
                return params;
            }
        }, callback);
    }
}
