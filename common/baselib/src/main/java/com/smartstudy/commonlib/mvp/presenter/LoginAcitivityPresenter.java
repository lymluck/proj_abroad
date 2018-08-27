package com.smartstudy.commonlib.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.contract.LoginActivityContract;
import com.smartstudy.commonlib.mvp.model.LoginModel;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;

import static com.smartstudy.commonlib.utils.ParameterUtils.XXD_UNREAD;

/**
 * Created by louis on 2017/3/4.
 */

public class LoginAcitivityPresenter implements LoginActivityContract.Presenter {
    private LoginActivityContract.View view;

    public LoginAcitivityPresenter(LoginActivityContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getPhoneCode(String phone) {
        LoginModel.phoneCode(phone, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.getPhoneCodeSuccess();
            }
        });
    }

    @Override
    public void phoneCodeLogin(final String phone, final String code) {
        LoginModel.phoneCodeLogin(phone, code, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    boolean created = data.getBoolean("created");
                    String ss_user = data.getString("ssUser");
                    JSONObject user_data = data.getJSONObject("user");
                    JSONObject person_data = user_data.getJSONObject("info");
                    String ticket = data.getString("ticket");
                    String user_account = user_data.getString("phone");
                    String user_id = user_data.getString("id");
                    String zhikeId = user_data.getString("userId");
                    int xxd_unread = user_data.getIntValue("notificationUnreadCount");
                    if (person_data.getJSONObject("targetSection") != null) {
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetCountry") != null) {
                            SPCacheUtils.put("target_countryInfo", person_data.getJSONObject("targetSection").getJSONObject("targetCountry").toString());
                        }
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetMajorDirection") != null) {
                            SPCacheUtils.put("target_major", person_data.getJSONObject("targetSection").getJSONObject("targetMajorDirection").toString());
                        }
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetDegree") != null) {
                            SPCacheUtils.put("project_name", person_data.getJSONObject("targetSection").getJSONObject("targetDegree").getString("name"));
                        }
                    }
                    SPCacheUtils.put("user_name", user_data.getString("name"));
                    SPCacheUtils.put("user_pic", user_data.getString("avatar"));
                    SPCacheUtils.put("user_account", user_account);
                    SPCacheUtils.put("ss_user", ss_user);
                    SPCacheUtils.put("ticket", ticket);
                    SPCacheUtils.put("user", user_data.getString("info"));
                    SPCacheUtils.put(XXD_UNREAD, xxd_unread);
                    SPCacheUtils.put("user_id", user_id);
                    SPCacheUtils.put("zhike_id", zhikeId);
                    view.phoneCodeLoginSuccess(created, user_id);
                    // sensorData
                    SensorsUtils.trackLogin(zhikeId);
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
