package com.smartstudy.commonlib.base.okhttpcache.strategy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public interface BaseRequestStrategy {
    /**
     * 请求策略
     *
     * @param chain
     * @return
     */
    Response request(Interceptor.Chain chain) throws IOException;
}
