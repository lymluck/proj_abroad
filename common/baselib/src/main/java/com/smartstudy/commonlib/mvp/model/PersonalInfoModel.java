package com.smartstudy.commonlib.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.callback.ReqProgressCallBack;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/8/1.
 */

public class PersonalInfoModel {

    public static void getInitOptions(BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_V2_PERSONAL_INIT_OPTS);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void editPersonalInfo(final PersonalParamsInfo info, BaseCallback callback) {
        RequestManager.getInstance().doPut(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_V2_PERSONAL);
            }

            @Override
            public Map getParams() {
                return params(info);
            }
        }, callback);
    }

    public static void getMyInfo(BaseCallback baseCallback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_V2_PERSONAL);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, baseCallback);
    }

    public static void upLoadAvatar(final String url, final Map param, ReqProgressCallBack callback) {
        RequestManager.getInstance().upLoadFile(new BaseRequestConfig() {

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public Map getParams() {
                return param;
            }
        }, callback);
    }

    public static void editMyInfo(final PersonalParamsInfo info, BaseCallback baseCallback) {
        RequestManager.getInstance().doPut(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_V2_PERSONAL);
            }

            @Override
            public Map getParams() {
                return params(info);
            }
        }, baseCallback);
    }

    private static Map params(PersonalParamsInfo personalParamsInfo) {
        Map params = new HashMap<>();
        if (personalParamsInfo.getName() != null) {
            params.put("name", personalParamsInfo.getName());
        }
        if (personalParamsInfo.getAvatar() != null) {
            params.put("avatar", personalParamsInfo.getAvatar());
        }
        params.put("avatarAlreadyCut", personalParamsInfo.isAvatarAlreadyCut() + "");
        if (personalParamsInfo.getGenderId() != null) {
            params.put("genderId", personalParamsInfo.getGenderId());
        }
        if (personalParamsInfo.getContact() != null) {
            params.put("contact", personalParamsInfo.getContact());
        }
        if (personalParamsInfo.getEmail() != null) {
            params.put("email", personalParamsInfo.getEmail());
        }
        if (personalParamsInfo.getCustomerRoleId() != null) {
            params.put("customerRoleId", personalParamsInfo.getCustomerRoleId());
        }
        if (personalParamsInfo.getAdmissionTime() != null) {
            params.put("admissionTime", personalParamsInfo.getAdmissionTime());
        }
        if (personalParamsInfo.getTargetCountry() != null) {
            params.put("targetCountry", personalParamsInfo.getTargetCountry());
        }
        if (personalParamsInfo.getTargetDegree() != null) {
            params.put("targetDegree", personalParamsInfo.getTargetDegree());
        }
        if (personalParamsInfo.getTargetSchoolRank() != null) {
            params.put("targetSchoolRank", personalParamsInfo.getTargetSchoolRank());
        }
        if (personalParamsInfo.getTargetMajorDirection() != null) {
            params.put("targetMajorDirection", personalParamsInfo.getTargetMajorDirection());
        }
        if (personalParamsInfo.getCurrentSchool() != null) {
            params.put("currentSchool", personalParamsInfo.getCurrentSchool());
        }
        if (personalParamsInfo.getScore() != null) {
            params.put("score", personalParamsInfo.getScore());
        }
        if (personalParamsInfo.getScoreLanguage() != null) {
            params.put("scoreLanguage", personalParamsInfo.getScoreLanguage());
        }
        if (personalParamsInfo.getScoreStandard() != null) {
            params.put("scoreStandard", personalParamsInfo.getScoreStandard());
        }
        if (personalParamsInfo.getActivityInternship() != null) {
            params.put("activityInternship", personalParamsInfo.getActivityInternship());
        }
        if (personalParamsInfo.getActivityResearch() != null) {
            params.put("activityResearch", personalParamsInfo.getActivityResearch());
        }
        if (personalParamsInfo.getActivityCommunity() != null) {
            params.put("activityCommunity", personalParamsInfo.getActivityCommunity());
        }
        if (personalParamsInfo.getActivitySocial() != null) {
            params.put("activitySocial", personalParamsInfo.getActivitySocial());
        }
        if (personalParamsInfo.getActivityExchange() != null) {
            params.put("activityExchange", personalParamsInfo.getActivityExchange());
        }
        return params;
    }
}
