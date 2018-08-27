package com.smartstudy.commonlib;


import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.meiqia.core.callback.OnInitCallback;
import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.commonlib.base.manager.CrashHandler;
import com.smartstudy.commonlib.base.tools.AppMethodManager;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


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
        appContext = this;
        //初始化的代码建议紧跟 super.onCreate(),并且所有进程都需要初始化，已达到所有进程都可以被 patch 的目的
        initTinkerPatch();
        //初始化growingIO
        Configuration growingIOConfig = new Configuration();
        if (Utils.isApkInDebug()) {
            /**严苛模式主要检测两大问题，一个是线程策略，即TreadPolicy，另一个是VM策略，即VmPolicy。*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            }
            CrashHandler.getInstance().init(this);
            MobclickAgent.setDebugMode(true);
            JPushInterface.setDebugMode(true);
            UMGameAgent.setDebugMode(true);
            Config.DEBUG = true;
            MobclickAgent.setCatchUncaughtExceptions(false);
            growingIOConfig.setDebugMode(true);
            Router.setDebuggable(true);
        }
        //umeng初始化
        String channel = WalleChannelReader.getChannel(this, "xxd");
        String appkey = "58bd410f04e2051199001a1b";
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, appkey, channel);
        MobclickAgent.startWithConfigure(config);
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //友盟分享，未安装直接跳转到下载页
        Config.isJumptoAppStore = true;
        UMShareAPI.init(this, appkey);
        //初始化极光
        JPushInterface.init(this);
        //growingIO初始化
        growingIOConfig.useID()
                .trackAllFragments()
                .setChannel(channel);
        GrowingIO.startWithConfiguration(this, growingIOConfig); //打开调试Log;
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


//    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
//        @Override
//        public boolean getUseDeveloperSupport() {
//            return BuildConfig.DEBUG;
//        }
//
//        @Override
//        protected List<ReactPackage> getPackages() {
//            return Arrays.<ReactPackage>asList(
//                    new MainReactPackage(), new ReactQuPackage()
//            );
//        }
//    };

//    @Override
//    public ReactNativeHost getReactNativeHost() {
//        return mReactNativeHost;
//    }

}