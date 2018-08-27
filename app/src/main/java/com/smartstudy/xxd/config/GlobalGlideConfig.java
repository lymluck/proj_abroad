package com.smartstudy.xxd.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.smartstudy.commonlib.base.config.https.OkHttpUrlLoader;
import com.smartstudy.commonlib.utils.SDCardUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe glide4配置
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
@GlideModule
public final class GlobalGlideConfig extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        File cache_file = SDCardUtils.getFileDirPath("Xxd" + File.separator + "cache");
        if (cache_file != null) {
            //100M
            builder.setDiskCache(new DiskLruCacheFactory(cache_file.getAbsolutePath(), 100 * 1024 * 1024));
        }
//        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
