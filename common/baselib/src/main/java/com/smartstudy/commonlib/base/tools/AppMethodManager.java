package com.smartstudy.commonlib.base.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author louis
 * @desc 全局方法通过类名和方法名进行调用，代码混淆时要注意需调用的方法
 * @time created at 17/4/6 下午9:05
 * @company www.smartstudy.com
 */
public class AppMethodManager {

    /**
     * 通过类名和方法名调用改方法
     *
     * @param obj        方法调用对象
     * @param methodName 方法名
     * @param clz        参数类型类别，与参数一一对应
     * @param args       参数列表
     * @throws Exception
     */
    public static void doClassMethod(Object obj, String methodName, Class[] clz, Object[] args) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName, clz);
        method.invoke(obj, args);
    }

    public static void doClassMethod(Object obj, String methodName) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName);
        method.invoke(obj);
    }

    public static Constructor getClassInstance(String className, Class<?>... args) throws Exception {
        Class cls = Class.forName(className);
        Constructor constructor = cls.getConstructor(args);
        return constructor;
    }
}
