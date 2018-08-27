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
 * Created by louis on 17/4/6.
 */

public class HomeHotModel {

    public static void getUsCollegeHomeHot(int cache_type, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cache_type, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_US_COLLEGE_HOT);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("appVersion", AppUtils.getVersionName());
                return params;
            }
        }, callback);
    }

    public static void getUsHighSchoolHomeHot(int cache_type, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cache_type, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_US_HIGH_SCHOOL_HOT);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("appVersion", AppUtils.getVersionName());
                return params;
            }
        }, callback);
    }

    public static void deleteCourse(final String productId, BaseCallback<String> callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {

            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_DELETE_RECOMMOND_COURSE), productId);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    /**
     * 首页打卡事件
     *
     * @param id
     * @param callback
     */
    public static void checkin(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {

            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.EXAM_SCHEDULE_CHECKIN), id);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}
