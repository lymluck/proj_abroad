package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.Map;

/**
 * @author louis
 * @date on 2018/4/10
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class CollectionModel {

    public static void isCollected(final String type, final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_IS_COLLECTED, type, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void collect(final String type, final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_IS_COLLECTED, type, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void disCollect(final String type, final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_IS_COLLECTED, type, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
