package com.smartstudy.xxd.application;


import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.base.callback.ForegroundCallbacks;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;


/**
 * @author luoyongming
 * @date on 2017/4/12
 * @describe application类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class XxdApplication extends BaseApplication {

    private int patchStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        final SophixManager instance = SophixManager.getInstance();
        instance.setPatchLoadStatusStub(new PatchLoadStatusListener() {
            @Override
            public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                patchStatus = code;
            }
        });
        ForegroundCallbacks.get().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                // 启动到前台时检测是否有新补丁
                instance.queryAndLoadNewPatch();
            }

            @Override
            public void onBecameBackground() {
                // 应用处于后台，如果补丁存在应用结束掉，重启
                if (patchStatus == PatchStatus.CODE_LOAD_RELAUNCH) {
                    // 应用处于后台时结束程序
                    if (ForegroundCallbacks.get().isBackground()) {
                        instance.killProcessSafely();
                    }
                }
            }
        });
        // 检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
