package com.smartstudy.router;

import android.support.v4.util.ArrayMap;

import com.smartstudy.router.util.LogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Hub for 'apt' classes.
 * <p>
 * Created by louis on 2017/3/13.
 */
class AptHub {
    private static final String PACKAGE_NAME = "com.smartstudy.router";
    private static final String DOT = ".";
    private static final String ROUTER_BUILD_INFO = "RouterBuildInfo";
    private static final String ALL_MODULES = "ALL_MODULES";
    private static final String ROUTE_TABLE = "RouteTable";
    private static final String INTERCEPTOR_TABLE = "InterceptorTable";
    private static final String TARGET_INTERCEPTORS = "TargetInterceptors";

    // Uri -> Activity/Fragment
    static ArrayMap<String, Class<?>> routeTable = new ArrayMap<>();
    // Activity/Fragment -> interceptorTable' name
    static ArrayMap<Class<?>, String[]> targetInterceptors = new ArrayMap<>();
    // interceptor's name -> interceptor
    static ArrayMap<String, Class<? extends RouteInterceptor>> interceptorTable = new ArrayMap<>();

    /**
     * This method offers an ability to register extra route info for developers,
     * such as you compile an aar as dependencies rather than a project module.
     *
     * @param modules extra modules' name
     */
    public static void registerModules(String... modules) {
        init(modules);
    }

    static void initDefault() {
        String[] modules;
        try {
            /* RouterBuildInfo */
            Class<?> buildInfo = Class.forName(PACKAGE_NAME + DOT + ROUTER_BUILD_INFO);
            Field allModules = buildInfo.getField(ALL_MODULES);
            String modules_name = (String) allModules.get(buildInfo);
            modules = modules_name.split(",");
        } catch (ClassNotFoundException e) {
            LogUtils.e("Initialization failed, have you forgotten to apply plugin: " +
                    "'com.chenenyu.router' in application module?");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        init(modules);
    }

    private synchronized static void init(String... modules) {
        if (modules == null || modules.length == 0) {
            LogUtils.w("empty modules.");
        } else {
            // validate module name first.
            validateModuleName(modules);

            /* RouteTable */
            String routeTableName;
            for (String module : modules) {
                try {
                    routeTableName = PACKAGE_NAME + DOT + capitalize(module) + ROUTE_TABLE;
                    Class<?> routeTableClz = Class.forName(routeTableName);
                    Constructor constructor = routeTableClz.getConstructor();
                    RouteTable instance = (RouteTable) constructor.newInstance();
                    instance.handle(routeTable);
                } catch (ClassNotFoundException e) {
                    LogUtils.i(String.format("There is no RouteTable in module: %s.", module));
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                }
            }
            LogUtils.i("RouteTable:" + routeTable.toString());

            /* TargetInterceptors */
            String targetInterceptorsName;
            for (String moduleName : modules) {
                try {
                    targetInterceptorsName = PACKAGE_NAME + DOT + capitalize(moduleName) + TARGET_INTERCEPTORS;
                    Class<?> clz = Class.forName(targetInterceptorsName);
                    Constructor constructor = clz.getConstructor();
                    TargetInterceptors instance = (TargetInterceptors) constructor.newInstance();
                    instance.handle(targetInterceptors);
                } catch (ClassNotFoundException e) {
                    LogUtils.i(String.format("There is no TargetInterceptors in module: %s.", moduleName));
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                }
            }
            if (!targetInterceptors.isEmpty()) {
                LogUtils.i("TargetInterceptors:" + targetInterceptors.toString());
            }

            /* InterceptorTable */
            String interceptorName;
            for (String moduleName : modules) {
                try {
                    interceptorName = PACKAGE_NAME + DOT + capitalize(moduleName) + INTERCEPTOR_TABLE;
                    Class<?> clz = Class.forName(interceptorName);
                    Constructor constructor = clz.getConstructor();
                    InterceptorTable instance = (InterceptorTable) constructor.newInstance();
                    instance.handle(interceptorTable);
                } catch (ClassNotFoundException e) {
                    LogUtils.i(String.format("There is no InterceptorTable in module: %s.", moduleName));
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                }
            }
            if (!interceptorTable.isEmpty()) {
                LogUtils.i("InterceptorTable:" + interceptorTable.toString());
            }
        }
    }

    private static String capitalize(CharSequence self) {
        return self.length() == 0 ? "" :
                "" + Character.toUpperCase(self.charAt(0)) + self.subSequence(1, self.length());
    }

    private static void validateModuleName(String... modules) {
        for (int i = 0; i < modules.length; i++) {
            modules[i] = modules[i].replace('.', '_').replace('-', '_');
        }
    }
}
