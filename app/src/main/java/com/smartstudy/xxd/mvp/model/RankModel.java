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

public class RankModel {

    public static void getRank(int cacheType, final String country_id, final String categoryId, final int page,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                if (!TextUtils.isEmpty(country_id)) {
                    params.put("countryId", country_id);
                }
                params.put("categoryId", categoryId);
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }

    public static void getRankType(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_TYPE);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getHotRank(final String flag, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                if ("world".equals(flag)) {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_WORLD_MAJOR);
                } else if ("us".equals(flag)) {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_US_MAJOR);
                } else if ("yjs".equals(flag)) {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_YJS_MAJOR);
                } else if ("art".equals(flag)) {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_ART_MAJOR);
                }
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_WORLD_MAJOR);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getArtRank(final String majorId, final String country_id, final String degreeId,
                                  final int page, final BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_ART);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                if (!TextUtils.isEmpty(country_id)) {
                    params.put("countryId", country_id);
                }
                if (!TextUtils.isEmpty(degreeId)) {
                    params.put("degreeId", degreeId);
                }
                params.put("majorId", majorId);
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }
}
