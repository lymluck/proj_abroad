package com.smartstudy.commonlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * 获取设备信息
 * Created by louis on 2017/3/4.
 */
public class DeviceUtils {

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static String getSystemName(Context context) {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static int getSystemVersion(Context context) {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统唯一标识
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceModel(Context context) {
        return Build.DEVICE + ":" + Build.MODEL;
    }


    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getNetworkType(); // Requires READ_PHONE_STATE
    }


    /**
     * 返回当前网络连接类型
     *
     * @param context 上下文
     * @return
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connManager) {
            return ParameterUtils.NETWORN_NONE;
        }
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return ParameterUtils.NETWORN_NONE;
        }
        // Wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            State state = wifiInfo.getState();
            if (null != state) {
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return ParameterUtils.NETWORN_WIFI;
                }
            }
        }
        // 网络
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state) {
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return ParameterUtils.NETWORN_2G;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return ParameterUtils.NETWORN_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return ParameterUtils.NETWORN_4G;
                        default://有机型返回16,17
                            //中国移动 联通 电信 三种3G制式
                            if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName) || "WCDMA".equalsIgnoreCase(strSubTypeName) || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                                return ParameterUtils.NETWORN_3G;
                            } else {
                                return ParameterUtils.NETWORN_MOBILE;
                            }
                    }
                }
            }
        }
        return ParameterUtils.NETWORN_NONE;
    }
}
