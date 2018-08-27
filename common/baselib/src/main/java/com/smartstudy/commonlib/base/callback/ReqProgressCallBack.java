package com.smartstudy.commonlib.base.callback;

/**
 * 文件上传或下载进度回调
 * Created by louis on 2017/3/6.
 */

public interface ReqProgressCallBack<T> extends BaseCallback<T> {
    /**
     * 响应进度更新
     */
    void onProgress(long total, long current);
}
