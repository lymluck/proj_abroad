package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/12/8.
 */

public class StudyGetPlanningModel {
    public static void postStudyGetPlanning(final String currentGradeId, final String targetDegreeId, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ABROAD_PLAN);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("currentGradeId", currentGradeId);
                map.put("targetDegreeId", targetDegreeId);
                return map;
            }
        }, callback);
    }
}
