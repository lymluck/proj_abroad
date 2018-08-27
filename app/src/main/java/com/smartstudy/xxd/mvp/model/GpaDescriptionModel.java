package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.Map;

/**
 * Created by yqy on 2017/10/26.
 */

public class GpaDescriptionModel {
    public static void getGpaDescription(int cacheType, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_GPA_DESCRIPTION);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
