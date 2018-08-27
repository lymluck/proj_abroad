package com.smartstudy.router.template;

import com.smartstudy.router.RouteInterceptor;

import java.util.Map;

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
    void handle(Map<String, Class<? extends RouteInterceptor>> map);
}
