package com.smartstudy.xxd.mvp.model;

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
 * Created by yqy on 2017/12/4.
 */

public class QaModel {

    public static void postQa(final String content, final String countryId, final String projectId, BaseCallback callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ADD_QUES);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("content", content);
                params.put("targetCountryId", countryId);
                params.put("targetDegreeId", projectId);
                return params;
            }
        }, callback);
    }

    public static void getQaOptions(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.CACHED_ELSE_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ADD_QUES_OPTS);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getQaDetail(int cacheType, final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_QUESTS_LINK, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    public static void postQuestion(final String id, final String answerId, final String question, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_POST_QUESTION, id, answerId));
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("content", question);
                return params;
            }
        }, callback);
    }


    public static void getTeacheInfo(int cacheType, final String targeId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_THEARCHER_INFO);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                try {
                    map.put("imUserId", URLEncoder.encode(targeId, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return map;
            }
        }, callback);
    }


    public static void updateRating(final String questionId, final String answerId, final String score, final String comment, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_UPDATE_RATING, questionId, answerId));
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("score", score);
                map.put("comment", comment);
                return map;
            }
        }, callback);
    }
}