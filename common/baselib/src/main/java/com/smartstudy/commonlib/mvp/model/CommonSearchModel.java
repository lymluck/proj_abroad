package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class CommonSearchModel {

    public static void getHighSchools(int cacheType, final String countryId, final String keyword, final int page,
                                      BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_HIGH_SCHOOL_LIST);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                if (countryId != null) {
                    map.put("countryId", countryId);
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getSchools(int cacheType, final String countryId, final String keyword, final int page,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOLS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                if (countryId != null) {
                    map.put("countryId", countryId);
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getRanks(int cacheType, final String categoryId,
                                final String keyword, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                if (categoryId != null) {
                    params.put("categoryId", categoryId);
                }
                params.put("keyword", keyword);
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }

    public static void getNews(int cacheType, final String keyword, final int page,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_NEWS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getCourses(int cacheType, final String keyword, final int page,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COURSE_LIST);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getSpeData(int cacheType, final String keyword, final int page,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SPECIAL_DATA);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getProgram(int cacheType, final String typeId, final String keyword, final int page,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_PROGRAM);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("typeId", typeId);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getRankType(int cacheType, final String keyword, final int page,
                                   BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_SEARCH);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getArtRank(int cacheType, final String keyword, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_ART_MAJOR);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("keyword", keyword);
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }
}
