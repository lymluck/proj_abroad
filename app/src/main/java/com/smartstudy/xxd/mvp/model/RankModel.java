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

public class RankModel {

    public static void getRank(int cacheType, final String categoryId, final int page,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS);
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

    public static void getRankHot(final boolean top3, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RANKS_HOT);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                if (top3) {
                    params.put("top3", top3 + "");
                }
                return params;
            }
        }, callback);
    }
}
