package com.smartstudy.router.chain;

import android.support.annotation.NonNull;

import com.smartstudy.router.AptHub;
import com.smartstudy.router.MatcherRegistry;
import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteResponse;
import com.smartstudy.router.RouteStatus;
import com.smartstudy.router.matcher.AbsExplicitMatcher;

import java.util.List;

/**
 * Created by louis on 2018/6/15.
 */
public class FragmentValidator implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(RouteInterceptor.Chain chain) {
        // Fragment只能匹配显式Matcher
        List<AbsExplicitMatcher> matcherList = MatcherRegistry.getExplicitMatcher();
        if (matcherList.isEmpty()) {
            return RouteResponse.assemble(RouteStatus.FAILED, "The MatcherRegistry contains no explicit matcher.");
        }
        if (AptHub.routeTable.isEmpty()) {
            return RouteResponse.assemble(RouteStatus.FAILED, "The RouteTable is empty.");
        }
        return chain.process();
    }
}
