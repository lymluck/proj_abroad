package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaCalculationDetailModel {
    public static void getGpaCalculationDetail(int cacheType, final String results, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_GPA_CALCULATION_DETAI);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("scores",results);
                return map;
            }
        }, callback);
    }
}
