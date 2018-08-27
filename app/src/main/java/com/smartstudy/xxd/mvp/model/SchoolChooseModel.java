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

public class SchoolChooseModel {

    public static void getSchoolStat(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_CHOOSE_STAT, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getOtherStudentDetail(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_WATCH_STUDENT_DETAIL), id);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
