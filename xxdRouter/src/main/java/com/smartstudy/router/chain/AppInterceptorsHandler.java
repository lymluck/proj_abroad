package com.smartstudy.router.chain;

import android.support.annotation.NonNull;

import com.smartstudy.router.AptHub;
import com.smartstudy.router.RealInterceptorChain;
import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteRequest;
import com.smartstudy.router.RouteResponse;
import com.smartstudy.router.Router;
import com.smartstudy.router.util.LogUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collect app interceptors and insert into chain queue to process.
 * <br>
 * Created by louis on 2018/6/15.
 */
public class AppInterceptorsHandler implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(RouteInterceptor.Chain chain) {
        if (chain.getRequest().isSkipInterceptors()) {
            return chain.process();
        }

        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        RouteRequest request = chain.getRequest();

        // enqueue global interceptors
        if (!Router.getGlobalInterceptors().isEmpty()) {
            realChain.getInterceptors().addAll(Router.getGlobalInterceptors());
        }

        Set<String> finalInterceptors = new LinkedHashSet<>();
        // add annotated interceptors
        if (realChain.getTargetClass() != null) {
            String[] baseInterceptors = AptHub.targetInterceptorsTable.get(realChain.getTargetClass());
            if (baseInterceptors != null && baseInterceptors.length > 0) {
                Collections.addAll(finalInterceptors, baseInterceptors);
            }
        }

        // add/remove temp interceptors
        if (request.getTempInterceptors() != null) {
            for (Map.Entry<String, Boolean> entry : request.getTempInterceptors().entrySet()) {
                if (Boolean.TRUE.equals(entry.getValue())) {
                    finalInterceptors.add(entry.getKey());
                } else {
                    finalInterceptors.remove(entry.getKey());
                }
            }
        }

        if (!finalInterceptors.isEmpty()) {
            for (String name : finalInterceptors) {
                RouteInterceptor interceptor = AptHub.interceptorInstances.get(name);
                if (interceptor == null) {
                    Class<? extends RouteInterceptor> clz = AptHub.interceptorTable.get(name);
                    try {
                        interceptor = clz.newInstance();
                        AptHub.interceptorInstances.put(name, interceptor);
                    } catch (Exception e) {
                        LogUtils.e("Can't construct a interceptor instance for: " + name + "==" + e);
                    }
                }
                // enqueue
                if (interceptor != null) {
                    realChain.getInterceptors().add(interceptor);
                }
            }
        }

        return chain.process();
    }
}
