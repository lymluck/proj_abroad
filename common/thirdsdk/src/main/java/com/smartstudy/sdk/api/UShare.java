package com.smartstudy.sdk.api;

import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * @author louis
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class UShare {

    public static void init(Context context, String appKey) {
        // 友盟分享，未安装直接跳转到下载页
        Config.isJumptoAppStore = true;
        UMShareAPI.init(context, appKey);

        // 各个平台的配置，建议放在全局Application或者程序入口
        PlatformConfig.setWeixin("wx84afe226d5061c83", "c05d4a58cf6640503cb05e6e98573ea8");
        PlatformConfig.setSinaWeibo("1219882590", "620d64cd64b56f92fa52d0641bac2513",
            "http://xxd.smartstudy.com/app/download.html");
        PlatformConfig.setQQZone("1106004379", "fvs802OXMV4ZPZCi");
    }

    public static void release(Context context) {
        UMShareAPI.get(context).release();
    }
}
