package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/9/4.
 */

public class QaCardModel {

    public static void getQaCardInfo(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_QA_CARD_USAGE);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("cardId", id);
                return params;
            }
        }, callback);
    }

    public static void useCard(final String cardId, final String appointmentDate, final String appointmentStartTime,
                               final String appointmentEndTime, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_QA_CARD_USE);
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("cardId", cardId);
                params.put("appointmentDate", appointmentDate);
                params.put("appointmentStartTime", appointmentStartTime);
                params.put("appointmentEndTime", appointmentEndTime);
                return params;
            }
        }, callback);
    }
}
