package com.smartstudy.commonlib;


import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.meiqia.core.MQManager;
import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.commonlib.base.callback.ForegroundCallbacks;
import com.smartstudy.commonlib.base.handler.CrashHandler;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Configuration;
import com.smartstudy.router.Router;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.api.UShare;

import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by louis on 2017/2/22.
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    public static Context appContext;

    private boolean isDownload = false;
    private String downLoadUrl;

    public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class) {
                if (null == instance) {
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // activity生命周期，onCreate之后
        ForegroundCallbacks.init(this);
        appContext = this;
        if (getProcessName(this).equals(getPackageName())) {
            // umeng初始化
            String channel = WalleChannelReader.getChannel(this, "xxd");
            String appkey = "58bd410f04e2051199001a1b";
            UApp.init(this, appkey, channel);
            if (Utils.isApkInDebug()) {
                // jpush debug
                JPushInterface.setDebugMode(true);
                // umeng debug
                UApp.debug();
                // 初始化 Sensors SDK
                SensorsUtils.initDebugMode(this, channel);
                // 禁止极光捕获crash
                JPushInterface.stopCrashHandler(this);
            } else {
                // 初始化 Sensors SDK
                SensorsUtils.initReleaseMode(this, channel);
                // 捕获闪退日志
                CrashHandler.getInstance().init(this);
            }
            // 初始化分享
            UShare.init(this, appkey);

            // 初始化极光
            JPushInterface.init(this);
            //路由初始化
            Router.initialize(new Configuration.Builder()
                .setDebuggable(BuildConfig.DEBUG)
                .registerModules("baselib", "app")
                .build());
            // 执行美洽初始化
            MQManager.init(this, ParameterUtils.MEIQIA_KEY, null);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    /**
     * 获取进程名。
     * 由于app是一个多进程应用，因此每个进程被os创建时，
     * onCreate()方法均会被执行一次，
     * 进行辨别初始化，针对特定进程进行相应初始化工作，
     * 此方法可以提高一半启动时间。
     *
     * @param context 上下文环境对象
     * @return 获取此进程的进程名
     */
    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return "";
        }

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == android.os.Process.myPid()
                && !TextUtils.isEmpty(runningAppProcess.processName)) {
                return runningAppProcess.processName;
            }
        }
        return "";
    }
}
