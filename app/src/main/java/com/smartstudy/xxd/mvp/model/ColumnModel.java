package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author louis
 * @date on 2018/7/17
 * @describe 专栏
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ColumnModel {

    public static void getColumnList(final int page, BaseCallback callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COLUMN_LIST);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getColumn(final String id, BaseCallback callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLUMN, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
