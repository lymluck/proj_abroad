package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class MyChooseSchoolModel {

    public static void getMySchool(int cacheType, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MYSCHOOL);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void editMySchool(final String school_Id, final String match_id, final String source,
                                    BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MYSCHOOL_EDIT);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("schoolId", school_Id);
                params.put("matchTypeId", match_id);
                params.put("source", source);
                return params;
            }
        }, callback);
    }

    public static void editMySchool(final String data, final String source, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_MYSCHOOL_EDIT);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", data);
                params.put("source", source);
                return params;
            }
        }, callback);
    }

    public static void deleteMySchool(final String school_Id, BaseCallback<String> callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_MYSCHOOL_DEL, school_Id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void setPrivacy(final boolean visible, BaseCallback<String> callback) {
        RequestManager.getInstance().doPut(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_WACTH_VISIBILITY);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap<>();
                params.put("visible", visible+"");
                return params;
            }
        }, callback);
    }
}
