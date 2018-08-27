package com.smartstudy.medialib.ui;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.medialib.videocache.HttpProxyCacheServer;

import java.io.File;

/**
 * @author louis
 * @date on 2018/8/15
 * @describe ijkplayer播放器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MediaApplication extends BaseApplication {

    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        if (null == proxy) {
            proxy = newProxy();
        }
        return proxy;
    }

    private static HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(BaseApplication.appContext)
            .cacheDirectory(SDCardUtils.getFileDirPath("Xxd" + File.separator + "cache"))
            .build();
    }
}
