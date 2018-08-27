package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.Map;

/**
 * @author louis
 * @date on 2018/1/17
 * @describe IM相关的数据
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class IMClientModel {

    public static void reGetIMToken(BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_REGET_IMTOKEN);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
