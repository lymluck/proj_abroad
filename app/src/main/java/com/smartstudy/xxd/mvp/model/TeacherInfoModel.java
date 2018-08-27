package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherInfoModel {
    public static void addGood(final String imUserId, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ADD_GOOD);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("imUserId", imUserId);
                return map;
            }
        }, callback);
    }
}
