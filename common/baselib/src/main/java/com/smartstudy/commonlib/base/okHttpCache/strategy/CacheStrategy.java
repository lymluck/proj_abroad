package com.smartstudy.commonlib.base.okHttpCache.strategy;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 仅仅请求缓存策略
 * Created by louis on 17/3/22.
 */
public class CacheStrategy implements BaseRequestStrategy {

    private static final float MAX_STALE = 60 * 60 * 24 * 7f;//过期时间为7天
    private float mMaxStale;//缓存过期时间

    public CacheStrategy() {
        mMaxStale = MAX_STALE;
    }

    public CacheStrategy(float maxStale) {
        this.mMaxStale = maxStale;
    }

    /**
     * 请求策略
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();//没有网络，直接读取缓存
        Response response = chain.proceed(request);
        response.newBuilder()// only-if-cached完全使用缓存，如果命中失败，则返回503错误
                .header("Cache-Control", "public, only-if-cached, max-stale=" + mMaxStale)
                .removeHeader("Pragma")
                .build();
        return response;
    }
}
