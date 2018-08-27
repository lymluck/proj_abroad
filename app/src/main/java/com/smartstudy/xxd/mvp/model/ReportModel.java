package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/12/27.
 */

public class ReportModel {
    public static void getReportReason(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COUNSELLOR_REASONS);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    public static void submitReport(final String targetId, final String reasonId, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COUNSELLOR_SUBMIT_REASONS);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("targetId", targetId);
                params.put("reasonId", reasonId);
                return params;
            }
        }, callback);
    }


}
