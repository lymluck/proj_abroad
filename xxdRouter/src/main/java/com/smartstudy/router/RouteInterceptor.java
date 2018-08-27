package com.smartstudy.router;

import android.content.Context;

/**
 * Interceptor before route.
 * <p>
 * Created by louis on 2016/12/20.
 */
public interface RouteInterceptor {
    /**
     * @param context      Context
     * @param routeRequest RouteRequest
     * @return True if you want to intercept this route, false otherwise.
     */
    boolean intercept(Context context, RouteRequest routeRequest);
}
