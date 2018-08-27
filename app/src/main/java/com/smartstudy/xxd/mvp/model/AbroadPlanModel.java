package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class AbroadPlanModel {

    public static void getPlan(int cacheType,
                               BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ABROAD_PLAN);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("currentGradeId", "GRA_SHS_1");
                map.put("targetDegreeId", "DEGREE_1");
                return map;
            }
        }, callback);
    }
}
