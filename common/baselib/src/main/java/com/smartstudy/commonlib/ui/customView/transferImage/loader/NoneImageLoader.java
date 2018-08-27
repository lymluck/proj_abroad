package com.smartstudy.commonlib.ui.customView.transferImage.loader;

import android.widget.ImageView;

/**
 * 图片加载器空实现，防止 NullPointer
 * Created by louis on 2017/5/3 0003.
 * <p>
 */
public class NoneImageLoader implements ImageLoader {

    @Override
    public void showSourceImage(String srcUrl, ImageView imageView, SourceCallback sourceCallback) {
    }

    @Override
    public void loadThumbnailAsync(String thumbUrl,
                                   ImageView imageView, ThumbnailCallback callback) {
    }

    @Override
    public boolean isLoaded(String url) {
        return false;
    }

    @Override
    public void clearCache() {
    }

}
