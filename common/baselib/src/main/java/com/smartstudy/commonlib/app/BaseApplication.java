package com.smartstudy.commonlib.app;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.meiqia.core.callback.OnInitCallback;
import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.commonlib.base.manager.CrashHandler;
import com.smartstudy.commonlib.base.tools.AppMethodManager;
import com.smartstudy.commonlib.ui.provider.XxdConversationListProvider;
import com.smartstudy.commonlib.ui.provider.XxdTextMessageItemProvider;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Constructor;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.push.RongPushClient;


/**
 * Created by louis on 2017/2/22.
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    public static Context appContext;

    // 程序是否后台运行标志，用于标识推送消息是否需要在通知栏显示，默认程序是在后台运行的
    private boolean isBackground = true;
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
        //初始化的代码建议紧跟 super.onCreate(),并且所有进程都需要初始化，已达到所有进程都可以被 patch 的目的
        initTinkerPatch();
        appContext = this;
        if (getProcessName(this).equals(getPackageName())) {
            //初始化growingIO
            Configuration growingIOConfig = new Configuration();
            //umeng初始化
            String channel = WalleChannelReader.getChannel(this, "xxd");
            String appkey = "58bd410f04e2051199001a1b";
            MobclickAgent.UMAnalyticsConfig umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(this, appkey, channel, MobclickAgent.EScenarioType.E_UM_NORMAL);
            MobclickAgent.startWithConfigure(umAnalyticsConfig);
            if (Utils.isApkInDebug()) {
                /**严苛模式主要检测两大问题，一个是线程策略，即TreadPolicy，另一个是VM策略，即VmPolicy。*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
                }
                CrashHandler.getInstance().init(this);
                MobclickAgent.setDebugMode(true);
                JPushInterface.setDebugMode(true);
                Config.DEBUG = true;
                MobclickAgent.setCatchUncaughtExceptions(false);
                //打开调试Log;
                growingIOConfig.setDebugMode(true);
                Router.setDebuggable(true);
            }
            //禁止默认的页面统计方式
            MobclickAgent.openActivityDurationTrack(false);
            //友盟分享，未安装直接跳转到下载页
            Config.isJumptoAppStore = true;
            UMShareAPI.init(this, appkey);
            //初始化极光
            JPushInterface.init(this);

            //注册容云组件
            RongInit();

            //growingIO初始化
            growingIOConfig.useID()
                    .trackAllFragments()
                    .setChannel(channel);
            GrowingIO.startWithConfiguration(this, growingIOConfig);
            //路由初始化
            Router.initialize(this);

            try {
                //执行美洽初始化
                Constructor constructor = AppMethodManager.getClassInstance("com.meiqia.meiqiasdk.util.MQConfig");
                AppMethodManager.doClassMethod(constructor.newInstance(), "init", new Class[]{Context.class, String.class, OnInitCallback.class},
                        new Object[]{getApplicationContext(), ParameterUtils.MEIQIA_KEY, null});
            } catch (Exception e) {
                e.printStackTrace();
            }

            //各个平台的配置，建议放在全局Application或者程序入口
            PlatformConfig.setWeixin("wx84afe226d5061c83", "c05d4a58cf6640503cb05e6e98573ea8");
            PlatformConfig.setSinaWeibo("1219882590", "620d64cd64b56f92fa52d0641bac2513", "http://xxd.smartstudy.com/app/download.html");
            PlatformConfig.setQQZone("1106004379", "fvs802OXMV4ZPZCi");
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean isBackground) {
        this.isBackground = isBackground;
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
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
    private void initTinkerPatch() {
        // 我们可以从这里获得Tinker加载过程的信息
        ApplicationLike tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        // 初始化TinkerPatch SDK
        TinkerPatch.init(tinkerApplicationLike)
                .reflectPatchLibrary()
                .setPatchRollbackOnScreenOff(true)
                .setPatchRestartOnSrceenOff(true)
                .setFetchPatchIntervalByHours(3);
        // 获取当前的补丁版本
        LogUtils.d("Current patch version is " + TinkerPatch.with().getPatchVersion());

        // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
        // 不同的是，会通过handler的方式去轮询
        TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
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

    private void RongInit() {
        RongPushClient.registerHWPush(this);
        RongPushClient.registerMiPush(this, "2882303761517563762", "5441756335762");
//        try {
//            RongPushClient.registerFCM(this);
//        } catch (RongException e) {
//            e.printStackTrace();
//        }
        RongIM.init(this, "25wehl3u29wqw");
        RongIM.getInstance().registerConversationTemplate(new XxdConversationListProvider());
        RongIM.registerMessageTemplate(new XxdTextMessageItemProvider());
        AppManager.init(this);
    }
}
