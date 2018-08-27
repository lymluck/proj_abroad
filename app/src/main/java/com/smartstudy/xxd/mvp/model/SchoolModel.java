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
 * Created by louis on 17/4/6.
 */

public class SchoolModel {

    public static void getSchools(final String country_id, final String rankRange, final String egRange, final String feeRange, final int page,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOLS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(country_id)) {
                    map.put("countryId", country_id);
                }
                if (!TextUtils.isEmpty(rankRange)) {
                    map.put("localRank", rankRange);
                }
                if (!TextUtils.isEmpty(feeRange)) {
                    map.put("feeTotal", feeRange);
                }
                if (!TextUtils.isEmpty(egRange)) {
                    String type = TextUtils.split(egRange, ":")[0];
                    String value = TextUtils.split(egRange, ":")[1];
                    if ("toefl".equals(type)) {
                        map.put("scoreToefl", value);
                    } else if ("ielts".equals(type)) {
                        map.put("scoreIelts", value);
                    }
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getSchools(final String ids,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOLS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("ids", ids);
                return map;
            }
        }, callback);
    }

    public static void getCollegeIntro(final String id,
                                       BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.CACHED_ELSE_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOL_INFO);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("id", id);
                return map;
            }
        }, callback);
    }

    public static void getCollegeInfo(final String id,
                                      BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOL_DETAIL);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("id", id);
                return map;
            }
        }, callback);
    }

    public static void getHighSchoolInfo(final String schoolId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_HIGHSCHOOL_DETAIL);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("id", schoolId);
                return map;
            }
        }, callback);
    }

    public static void postErr(final String schoolId, final String section, final String content,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_APP_SCHOOL_ERR);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("schoolId", schoolId);
                map.put("section", section);
                map.put("content", content);
                return map;
            }
        }, callback);
    }
}
