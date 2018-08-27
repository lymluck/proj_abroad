package com.smartstudy.permissions;

import android.Manifest;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author louis
 * @date on 2018/2/5
 * @describe 由于Android8.0的限制 最好的做法是申请权限的时候一组一组的申请
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class Permission {
    public static final String[] CALENDAR;
    public static final String[] CAMERA;
    public static final String[] CONTACTS;
    public static final String[] LOCATION;
    public static final String[] MICROPHONE;
    public static final String[] PHONE;
    public static final String[] SENSORS;
    public static final String[] SMS;
    public static final String[] STORAGE;

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
        } else {
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_CALL_LOG,
//                    Manifest.permission.WRITE_CALL_LOG,
//                    Manifest.permission.USE_SIP,
//                    Manifest.permission.PROCESS_OUTGOING_CALLS
            };

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    public static String getPermissionContent(List<String> perms) {
        String content = "";
        for (String perm : perms) {
            if (Arrays.asList(CALENDAR).contains(perm)) {
                if (!content.contains("日历")) {
                    content += "，" + "日历";
                }
            }
            if (Arrays.asList(CAMERA).contains(perm)) {
                if (!content.contains("相机")) {
                    content += "，" + "相机";
                }
            }
            if (Arrays.asList(CONTACTS).contains(perm)) {
                if (!content.contains("联系人")) {
                    content += "，" + "联系人";
                }
            }
            if (Arrays.asList(LOCATION).contains(perm)) {
                if (!content.contains("位置")) {
                    content += "，" + "位置";
                }
            }
            if (Arrays.asList(MICROPHONE).contains(perm)) {
                if (!content.contains("录音")) {
                    content += "，" + "录音";
                }
            }
            if (Arrays.asList(PHONE).contains(perm)) {
                if (!content.contains("电话")) {
                    content += "，" + "电话";
                }
            }
            if (Arrays.asList(SENSORS).contains(perm)) {
                if (!content.contains("传感器")) {
                    content += "，" + "传感器";
                }
            }
            if (Arrays.asList(SMS).contains(perm)) {
                if (!content.contains("短信")) {
                    content += "，" + "短信";
                }
            }
            if (Arrays.asList(STORAGE).contains(perm)) {
                if (!content.contains("存储空间")) {
                    content += "，" + "存储空间";
                }
            }
        }
        if (!TextUtils.isEmpty(content)) {
            content = "请在设置－应用－选校帝－权限管理中开启" + content.substring(1) + "权限，以正常使用选校帝功能服务";
        }
        return content;
    }
}
