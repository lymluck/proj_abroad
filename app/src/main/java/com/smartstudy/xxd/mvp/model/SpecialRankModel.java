package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class SpecialRankModel {

    public static void getRankTypeList(int cacheType, final int flag, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                if (flag == ParameterUtils.GL_FLAG) {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_GLOBLE_LIST);
                } else {
                    return HttpUrlUtils.getUrl(HttpUrlUtils.URL_USA_LIST);
                }
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
