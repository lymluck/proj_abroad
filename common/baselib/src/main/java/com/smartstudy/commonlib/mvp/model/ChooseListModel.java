package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.Map;

/**
 * Created by louis on 17/5/11.
 */

public class ChooseListModel {

    public static void getOptionsData(final String type, BaseCallback baseCallback) {
        RequestManager.getInstance().doGet(ParameterUtils.CACHED_ELSE_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                String url = null;
                if (ParameterUtils.TYPE_OPTIONS_SCHOOL.equals(type)) {
                    url = HttpUrlUtils.getUrl(HttpUrlUtils.URL_MATCHSHCOOL_OPTIONS);
                } else if (ParameterUtils.TYPE_OPTIONS_RATE.equals(type)) {
                    url = HttpUrlUtils.getUrl(HttpUrlUtils.URL_RATE_OPTIONS);
                }
                return url;
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, baseCallback);
    }
}
