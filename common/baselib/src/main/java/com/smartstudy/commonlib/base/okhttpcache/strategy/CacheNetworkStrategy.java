package com.smartstudy.commonlib.base.okhttpcache.strategy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 读取缓存，如果缓存不存在则读取网络
 * Created by louis on 17/3/22.
 */
public class CacheNetworkStrategy implements BaseRequestStrategy {
    /**
     * 请求策略 ,异常直接在里面捕获，不直接抛出
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) {
        Response response = null;
        CacheStrategy cacheStrategy = new CacheStrategy();
        NetworkStrategy networkStrategy = new NetworkStrategy();
        try {
            response = cacheStrategy.request(chain);
            if (!response.isSuccessful()) {
                return networkStrategy.request(chain);
            }
        } catch (IOException e) {
            try {
                response = networkStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        } catch (Exception e) {
            try {
                response = networkStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        }
        return response;
    }
}
