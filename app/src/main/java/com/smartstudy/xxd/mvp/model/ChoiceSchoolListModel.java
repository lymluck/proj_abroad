package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolListModel {
    public static void getChoiceSchoolList(int cacheType, final String id, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return String.format(HttpUrlUtils.getUrl(HttpUrlUtils.URL_WATCH_SCHOOL), id);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("page", page + "");
                return params;
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
