package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/6/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HotSchoolRankModel {
    public static void getHotSchoolRank(int cacheType, final String country_id, final String from, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOLS);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("countryId", country_id);
                params.put("page", page + "");
                params.put("order", from);
                params.put("withOrderRank", "true");
                params.put("simple", "true");
                return params;
            }
        }, callback);
    }
}
