package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/5/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyRemarkPlanModel {
    public static void getMyRemarksPlan(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_EXAM_SCHEDULE_MINE);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
