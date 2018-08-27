package com.smartstudy.commonlib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.smartstudy.commonlib.BaseApplication;

/**
 * 跟App相关的辅助类
 * Created by louis on 2017/3/4.
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = BaseApplication.appContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApplication.appContext.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号]
     *
     * @return 当前应用的版本名称
     */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = BaseApplication.appContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApplication.appContext.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取应用程序图标]
     *
     * @return 当前应用的图标
     */
    public static Drawable getAppIcon() {
        try {
            PackageManager packageManager = BaseApplication.appContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApplication.appContext.getPackageName(), 0);
            return packageInfo.applicationInfo.loadIcon(packageManager);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
