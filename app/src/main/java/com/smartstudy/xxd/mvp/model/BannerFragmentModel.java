package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class BannerFragmentModel {

    public static void getBanners(int cache_type, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cache_type, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ADS);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
