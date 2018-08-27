package com.smartstudy.commonlib.base.okhttpcache.strategy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 先进行网络请求，如果网络请求失败则直接请求缓存数据
 * Created by louis on 17/3/22.
 */
public class NetworkCacheStrategy implements BaseRequestStrategy {
    /**
     * 请求策略
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Response response = null;
        CacheStrategy cacheStrategy = new CacheStrategy();
        NetworkStrategy networkStrategy = new NetworkStrategy();
        try {
            response = networkStrategy.request(chain);
            if (!response.isSuccessful()) {
                return cacheStrategy.request(chain);
            }
        } catch (IOException e) {
            try {
                response = cacheStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        } catch (Exception e) {
            try {
                response = cacheStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        }
        return response;
    }
}
