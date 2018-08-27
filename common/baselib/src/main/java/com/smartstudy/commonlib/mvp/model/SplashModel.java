package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class SplashModel extends BaseModel {

    public static void getAdInfo(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.ONLY_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_LAUNCH_AD);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void trackAd(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.ONLY_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_TRACK_LAUNCH_AD, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
