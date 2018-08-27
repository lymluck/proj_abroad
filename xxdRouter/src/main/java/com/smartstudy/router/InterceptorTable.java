package com.smartstudy.router;

import android.support.v4.util.ArrayMap;

/**
 * Interceptor table mapping.
 * <p>
 * Created by louis on 2017/6/30.
 */
public interface InterceptorTable {
    /**
     * Mapping between name and interceptor.
     *
     * @param map name -> interceptor.
     */
    void handle(ArrayMap<String, Class<? extends RouteInterceptor>> map);
}
