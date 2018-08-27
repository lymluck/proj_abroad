package com.smartstudy.router;

import android.net.Uri;

import com.smartstudy.router.matcher.AbsMatcher;
import com.smartstudy.router.template.RouteTable;
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

    private static final List<RouteInterceptor> sGlobalInterceptors = new ArrayList<>();


    public static void initialize(Configuration configuration) {
        LogUtils.showLog(configuration.debuggable);
        AptHub.registerModules(configuration.modules);
    }

    public static IRouter build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    public static IRouter build(Uri uri) {
        return RealRouter.getInstance().build(uri);
    }

    public static IRouter build(RouteRequest request) {
        return RealRouter.getInstance().build(request);
    }

    /**
     * Custom route table.
     */
    public static void handleRouteTable(RouteTable routeTable) {
        if (routeTable != null) {
            routeTable.handle(AptHub.routeTable);
        }
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
