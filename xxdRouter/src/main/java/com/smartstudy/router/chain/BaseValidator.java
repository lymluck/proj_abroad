package com.smartstudy.router.chain;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteRequest;
import com.smartstudy.router.RouteResponse;
import com.smartstudy.router.RouteStatus;

/**
 * Created by louis on 2018/6/14.
 */
public class BaseValidator implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(RouteInterceptor.Chain chain) {
        RouteRequest request = chain.getRequest();
        if (request.getUri() == null) {
            return RouteResponse.assemble(RouteStatus.FAILED, "uri == null.");
        }

        Context context = null;
        if (chain.getSource() instanceof Context) {
            context = (Context) chain.getSource();
        } else if (chain.getSource() instanceof Fragment) {
            if (Build.VERSION.SDK_INT >= 23) {
                context = ((Fragment) chain.getSource()).getContext();
            } else {
                context = ((Fragment) chain.getSource()).getActivity();
            }
        } else if (chain.getSource() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) chain.getSource()).getContext();
        }
        if (context == null) {
            return RouteResponse.assemble(RouteStatus.FAILED, "Can't retrieve context from source.");
        }

        return chain.process();
    }
}
