package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class NewsListModel {

    public static void getNews(int cacheType, final List<String> tagIds, final int page,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_NEWS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (tagIds != null && tagIds.size() > 0) {
                    map.put("tagIds", tagIds.toString());
                } else {
                    map.put("tagIds", "");
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getSchoolNews(final String schoolId, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_NEWS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(schoolId)) {
                    map.put("schoolId", schoolId);
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getVisaNews(final String regionId, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_VISA_NEWS);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(regionId)) {
                    map.put("regionId", regionId);
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }
}
