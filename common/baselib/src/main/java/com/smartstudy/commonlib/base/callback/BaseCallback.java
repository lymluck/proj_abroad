package com.smartstudy.commonlib.base.callback;

/**
 * 网络请求基本回调
 * Created by louis on 2017/3/6.
 */
public interface BaseCallback<T> {

    void onErr(String errCode, String msg);

    void onSuccess(T result);

}
