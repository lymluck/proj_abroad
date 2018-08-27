package com.smartstudy.router;

import android.content.Context;
import android.net.Uri;

import com.smartstudy.router.matcher.AbsMatcher;
import com.smartstudy.router.matcher.MatcherRegistry;
import com.smartstudy.router.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry class.
 * <p>
 * Created by louis on 2016/12/20.
 */
public class Router {
    /**
     * You can get the raw uri in target page by call <code>intent.getStringExtra(Router.RAW_URI)</code>.
     */
    public static final String RAW_URI = "raw_uri";

    private static List<RouteInterceptor> sGlobalInterceptors = new ArrayList<>();

    private static boolean sDebuggable = false;

    /**
     * Initialize router.
     *
     * @param context placeholder for a future usage.
     */
    public static void initialize(Context context) {
        initialize(context, false);
    }

    /**
     * Initialize router.
     *
     * @param context    placeholder for a future usage.
     * @param debuggable {@link #setDebuggable(boolean)}.
     */
    public static void initialize(Context context, boolean debuggable) {
        if (debuggable) {
            setDebuggable(true);
        }
        AptHub.initDefault();
    }

    public static boolean isDebuggable() {
        return sDebuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        sDebuggable = debuggable;
        LogUtils.showLog(debuggable);
    }

    public static IRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    public static IRouter build(Uri uri) {
        return RealRouter.getInstance().build(uri);
    }

    /**
     * Use {@link #handleRouteTable(RouteTable)} instead.
     * <p>
     * This method will be <strong>removed</strong> in a future release.
     */
    @Deprecated
    public static void addRouteTable(RouteTable routeTable) {
        handleRouteTable(routeTable);
    }

    /**
     * Custom route table.
     */
    public static void handleRouteTable(RouteTable routeTable) {
        RealRouter.getInstance().handleRouteTable(routeTable);
    }

    /**
     * Custom interceptor table.
     */
    public static void handleInterceptorTable(InterceptorTable interceptorTable) {
        RealRouter.getInstance().handleInterceptorTable(interceptorTable);
    }

    /**
     * Custom targets' interceptors.
     */
    public static void handleTargetInterceptors(TargetInterceptors targetInterceptors) {
        RealRouter.getInstance().handleTargetInterceptors(targetInterceptors);
    }

    /**
     * Global interceptor.
     */
    public static void addGlobalInterceptor(RouteInterceptor routeInterceptor) {
        sGlobalInterceptors.add(routeInterceptor);
    }

    public static List<RouteInterceptor> getGlobalInterceptors() {
        return sGlobalInterceptors;
    }

    /**
     * Register your own matcher.
     *
     * @see com.smartstudy.router.matcher.AbsExplicitMatcher
     * @see com.smartstudy.router.matcher.AbsImplicitMatcher
     */
    public static void registerMatcher(AbsMatcher matcher) {
        MatcherRegistry.register(matcher);
    }

    public static void clearMatcher() {
        MatcherRegistry.clear();
    }
}
