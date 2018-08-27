package com.smartstudy.router.chain;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.smartstudy.router.AptHub;
import com.smartstudy.router.MatcherRegistry;
import com.smartstudy.router.RealInterceptorChain;
import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteRequest;
import com.smartstudy.router.RouteResponse;
import com.smartstudy.router.RouteStatus;
import com.smartstudy.router.matcher.AbsImplicitMatcher;
import com.smartstudy.router.matcher.AbsMatcher;
import com.smartstudy.router.util.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by louis on 2018/6/15.
 */
public class IntentProcessor implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(RouteInterceptor.Chain chain) {
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        RouteRequest request = chain.getRequest();
        List<AbsMatcher> matcherList = MatcherRegistry.getMatcher();
        List<AbsImplicitMatcher> implicitMatcherList = MatcherRegistry.getImplicitMatcher();
        Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();

        Intent intent = null;
        if (AptHub.routeTable.isEmpty()) {
            for (AbsImplicitMatcher implicitMatcher : implicitMatcherList) {
                if (implicitMatcher.match(chain.getContext(), request.getUri(), null, request)) {
                    LogUtils.i(String.format("{uri=%s, matcher=%s}",
                        chain.getRequest().getUri(), implicitMatcher.getClass().getCanonicalName()));
                    realChain.setTargetClass(null);
                    Object result = implicitMatcher.generate(chain.getContext(), request.getUri(), null);
                    if (result instanceof Intent) {
                        intent = (Intent) result;
                        assembleIntent(intent, request);
                        realChain.setTargetObject(intent);
                    } else {
                        return RouteResponse.assemble(RouteStatus.FAILED, String.format(
                            "The matcher can't generate an intent for uri: %s",
                            request.getUri().toString()));
                    }
                    break;
                }
            }
        } else {
            MATCHER:
            for (AbsMatcher matcher : matcherList) {
                boolean isImplicit = matcher instanceof AbsImplicitMatcher;
                if (isImplicit) {
                    if (matcher.match(chain.getContext(), request.getUri(), null, request)) {
                        LogUtils.i(String.format("{uri=%s, matcher=%s}",
                            chain.getRequest().getUri(), matcher.getClass().getCanonicalName()));
                        realChain.setTargetClass(null);
                        Object result = matcher.generate(chain.getContext(), request.getUri(), null);
                        if (result instanceof Intent) {
                            intent = (Intent) result;
                            assembleIntent(intent, request);
                            realChain.setTargetObject(intent);
                        } else {
                            return RouteResponse.assemble(RouteStatus.FAILED, String.format(
                                "The matcher can't generate an intent for uri: %s",
                                request.getUri().toString()));
                        }
                        break;
                    }
                } else {
                    for (Map.Entry<String, Class<?>> entry : entries) {
                        if (matcher.match(chain.getContext(), request.getUri(), entry.getKey(), request)) {
                            LogUtils.i(String.format("{uri=%s, matcher=%s}",
                                chain.getRequest().getUri(), matcher.getClass().getCanonicalName()));
                            realChain.setTargetClass(entry.getValue());
                            Object result = matcher.generate(chain.getContext(), request.getUri(), entry.getValue());
                            if (result instanceof Intent) {
                                intent = (Intent) result;
                                assembleIntent(intent, request);
                                realChain.setTargetObject(intent);
                            } else {
                                return RouteResponse.assemble(RouteStatus.FAILED, String.format(
                                    "The matcher can't generate an intent for uri: %s",
                                    request.getUri().toString()));
                            }
                            break MATCHER;
                        }
                    }
                }

            }
        }

        if (intent == null) {
            return RouteResponse.assemble(RouteStatus.NOT_FOUND, String.format(
                "Can't find an activity that matches the given uri: %s",
                request.getUri().toString()));
        }
        return chain.process();
    }

    private void assembleIntent(Intent intent, RouteRequest request) {
        if (request.getExtras() != null && !request.getExtras().isEmpty()) {
            intent.putExtras(request.getExtras());
        }
        if (request.getFlags() != 0) {
            intent.addFlags(request.getFlags());
        }
        if (request.getData() != null) {
            intent.setData(request.getData());
        }
        if (request.getType() != null) {
            intent.setType(request.getType());
        }
        if (request.getAction() != null) {
            intent.setAction(request.getAction());
        }
    }
}
