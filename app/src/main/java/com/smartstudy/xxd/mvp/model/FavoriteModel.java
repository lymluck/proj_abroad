package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.Map;

/**
 * @author louis
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class FavoriteModel {

    public static void favorite(final String type, final String Id, BaseCallback callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_LIKE, type, Id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void disFavorite(final String type, final String Id, BaseCallback callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_LIKE, type, Id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
