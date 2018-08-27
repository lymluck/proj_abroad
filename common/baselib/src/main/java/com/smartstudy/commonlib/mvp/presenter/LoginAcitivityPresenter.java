package com.smartstudy.commonlib.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.contract.LoginActivityContract;
import com.smartstudy.commonlib.mvp.model.LoginModel;
import com.smartstudy.commonlib.utils.IMUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.Utils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

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
        LoginModel.PhoneCode(phone, new BaseCallback<String>() {
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
                    LogUtils.d("info===" + person_data);
                    String ticket = data.getString("ticket");
                    String user_account = user_data.getString("phone");
                    String user_id = user_data.getString("id");
                    String imToken = user_data.getString("imToken");
                    int xxd_unread = user_data.getIntValue("notificationUnreadCount");
                    if (person_data.getJSONObject("targetSection") != null) {
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetCountry") != null) {
                            SPCacheUtils.put("target_countryInfo", person_data.getJSONObject("targetSection").getJSONObject("targetCountry").toString());
                        }
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetDegree") != null) {
                            SPCacheUtils.put("project_name", person_data.getJSONObject("targetSection").getJSONObject("targetDegree").getString("name"));
                        }
                        if (person_data.getJSONObject("targetSection").getJSONObject("admissionTime") != null) {
                            SPCacheUtils.put("aboard_time", person_data.getJSONObject("targetSection").getJSONObject("admissionTime").getString("name"));
                        }
                    }
                    String userName = user_data.getString("name");
                    String userPic = user_data.getString("avatar");
                    SPCacheUtils.put("user_name", userName);
                    SPCacheUtils.put("user_pic", Utils.getCacheUrl(userPic, 64, 64));
                    SPCacheUtils.put("user_account", user_account);
                    SPCacheUtils.put("ss_user", ss_user);
                    SPCacheUtils.put("ticket", ticket);
                    SPCacheUtils.put("user", user_data.getString("info"));
                    SPCacheUtils.put("xxd_unread", xxd_unread);
                    SPCacheUtils.put("user_id", user_id);
                    SPCacheUtils.put("imToken", imToken);
                    view.phoneCodeLoginSuccess(created, user_id);
                    //登录融云
                    loginRongIM(imToken, userName, userPic);
                }
            }
        });
    }

    private void loginRongIM(String imToken, final String userName, final String avatar) {
        if (!TextUtils.isEmpty(imToken)) {
            RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String userid) {
                    //登录成功后保存imUserId
                    SPCacheUtils.put("imUserId", userid);
                    //更新用户信息
                    if (RongIM.getInstance() != null) {
                        String cacheUrl = Utils.getCacheUrl(avatar, 64, 64);
                        UserInfo userInfo = new UserInfo(userid, userName, TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().refreshUserInfoCache(userInfo);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.d("RongIM===" + errorCode);
                }

                @Override
                public void onTokenIncorrect() {
                    IMUtils.reGetToken();
                }
            });
        }
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
