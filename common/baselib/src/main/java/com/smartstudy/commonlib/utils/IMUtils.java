package com.smartstudy.commonlib.utils;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.model.IMClientModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author louis
 * @date on 2018/1/17
 * @describe IM相关的工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class IMUtils {

    public static RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //获取token,成功后重新connect
                reGetToken();
            }

            @Override
            public void onSuccess(String s) {
                SPCacheUtils.put("imUserId", s);
            }

            @Override
            public void onError(final RongIMClient.ErrorCode e) {
                LogUtils.d("im--err=======" + e.getMessage());
            }
        };
        return connectCallback;
    }

    public static void reGetToken() {
        IMClientModel.reGetIMToken(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
            }

            @Override
            public void onSuccess(String result) {
                RongIM.connect(JSON.parseObject(result).getString("imToken"), new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        LogUtils.d("im--err=======" + "reToken still Incorrect");
                    }

                    @Override
                    public void onSuccess(String s) {
                        SPCacheUtils.put("imUserId", s);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        LogUtils.d("im--err=======" + e.getMessage());
                    }
                });
            }
        });
    }
}
