package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.SmartChooseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class SrtTestRateModel {

    public static void getHasTest(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RATE_TEST_COUNT);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void goTest(final SmartChooseInfo info, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RATE_TEST);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap();
                params.put("schoolId", info.getSchoolId());
                params.put("degreeId", info.getProjId());
                if (info.getGradeId() != null) {
                    params.put("gradeId", info.getGradeId());
                }
                if (info.getSchoolScore() > 0) {
                    params.put("gpa", info.getSchoolScore() + "");
                }
                if (info.getToeflScore() > 0) {
                    params.put("toefl", info.getToeflScore() + "");
                }
                if (info.getIeltsScore() > 0) {
                    params.put("ielts", info.getIeltsScore() + "");
                }

                if (info.getSatScore() > 0) {
                    params.put("sat", info.getSatScore() + "");
                }
                if (info.getActScore() > 0) {
                    params.put("act", info.getActScore() + "");
                }
                if (info.getGreScore() > 0) {
                    params.put("gre", info.getGreScore() + "");
                }
                if (info.getGmatScore() > 0) {
                    params.put("gmat", info.getGmatScore() + "");
                }
                return params;
            }
        }, callback);
    }

    public static void getResult(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_RATE_TEST);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap();
                params.put("id", id);
                return params;
            }
        }, callback);
    }
}
