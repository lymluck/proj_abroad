package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

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

public class SrtChooseSchoolModel {

    public static void getHasTest(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SCHOOL_TEST_COUNT);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void goChoose(final SmartChooseInfo info, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_SMART_CHOOSE);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap();
                params.put("countryId", info.getCountryId());
                params.put("degreeId", info.getProjId());
                if (!TextUtils.isEmpty(info.getRankTop())) {
                    params.put("localRankLimit", info.getRankTop());
                }
                if (info.getSchoolScore() > 0) {
                    params.put("gpa", info.getSchoolScore() + "");
                }
                if (info.getToeflScore() > 0) {
                    params.put("scoreToefl", info.getToeflScore() + "");
                }
                if (info.getIeltsScore() > 0) {
                    params.put("scoreIelts", info.getIeltsScore() + "");
                }
                if (info.getSatScore() > 0) {
                    params.put("scoreSat", info.getSatScore() + "");
                }
                if (info.getActScore() > 0) {
                    params.put("scoreAct", info.getActScore() + "");
                }
                if (info.getGreScore() > 0) {
                    params.put("scoreGre", info.getGreScore() + "");
                }
                if (info.getGmatScore() > 0) {
                    params.put("scoreGmat", info.getGmatScore() + "");
                }
                return params;
            }
        }, callback);
    }
}
