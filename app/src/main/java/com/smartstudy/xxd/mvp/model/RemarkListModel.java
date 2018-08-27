package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/5/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RemarkListModel {

    public static void getRemarkList(int cacheType, final String id, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_EXAMINEE), id);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("page", page + "");
                return params;
            }
        }, callback);
    }
}
