package com.smartstudy.router.chain;

import android.support.annotation.NonNull;

import com.smartstudy.router.MatcherRegistry;
import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteResponse;
import com.smartstudy.router.RouteStatus;
import com.smartstudy.router.matcher.AbsMatcher;

import java.util.List;

/**
 * Created by louis on 2018/6/15.
 */
public class IntentValidator implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(RouteInterceptor.Chain chain) {
        List<AbsMatcher> matcherList = MatcherRegistry.getMatcher();
        if (matcherList.isEmpty()) {
            return RouteResponse.assemble(RouteStatus.FAILED, "The MatcherRegistry contains no matcher.");
        }
        return chain.process();
    }
}
