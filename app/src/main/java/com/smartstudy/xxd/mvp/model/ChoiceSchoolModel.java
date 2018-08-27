package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;
import android.util.Log;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolModel {
    public static void postComment(final String userId, final String toUserId, final String content, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_WATCH_COMMENT), userId);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                if (!TextUtils.isEmpty(toUserId)) {
                    params.put("toUserId", toUserId);
                }
                params.put("content", content);
                return params;
            }
        }, callback);
    }

    public static void getComment(final String userId, final String page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_WATCH_COMMENTS), userId);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("page", page);
                return params;
            }
        }, callback);
    }

    public static void addGood(final String userId, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_WATCH_LIKE), userId);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
