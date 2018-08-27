package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/10/10.
 */

public class ThematicCenterModel {
    public static void getThematicCenter(int cacheType, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_THEMATIC_CENTER);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }
}