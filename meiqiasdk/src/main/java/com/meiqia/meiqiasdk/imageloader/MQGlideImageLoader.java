package com.meiqia.meiqiasdk.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/28 下午6:51
 * 描述:
 */
public class MQGlideImageLoader extends MQImageLoader {

    @Override
    public void displayImage(Activity activity, final ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final MQDisplayImageListener listener) {
        final String finalPath = getPath(path);
        Glide.with(activity).asBitmap().load(finalPath)
            .apply(RequestOptions.placeholderOf(loadingResId).error(failResId).override(width, height))
            .listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                    if (listener != null) {
                        listener.onSuccess(imageView, finalPath);
                    }
                    return false;
                }
            }).into(imageView);
    }

    @Override
    public void downloadImage(Context context, String path, final MQDownloadImageListener listener) {
        final String finalPath = getPath(path);
        Glide.with(context.getApplicationContext()).asBitmap().load(finalPath).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onSuccess(finalPath, bitmap);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (listener != null) {
                    listener.onFailed(finalPath);
                }
            }
        });
    }

}