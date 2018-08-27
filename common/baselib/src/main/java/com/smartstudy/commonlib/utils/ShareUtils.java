package com.smartstudy.commonlib.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.smartstudy.commonlib.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


/**
 * Created by louis on 17/4/5.
 */

public class ShareUtils {

    public static void showShare(final Activity context, String url, final String title, final String text,
                                 final String img_url, final UMShareListener listener) {
        if (url != null && url.contains("_from")) {
            url = url.replaceAll("(_from=[^&]*)", "_from=wap");
        }
        final String finalUrl = url;
        ShareAction action = shareAction(context)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        doClick(context, finalUrl, title, text, img_url, listener, share_media);
                    }
                });
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        config.setCancelButtonVisibility(false);
        config.setTitleVisibility(false);
        config.setShareboardBackgroundColor(context.getResources().getColor(R.color.white));
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        action.open(config);
    }

    private static ShareAction shareAction(Activity context) {
        return new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
    }

    private static void doClick(final Activity context, String url, final String title, final String text,
                                final String img_url, final UMShareListener listener, SHARE_MEDIA share_media) {
        if (TextUtils.isEmpty(url)) {
            if (!TextUtils.isEmpty(img_url)) {
                UMImage umImage = new UMImage(context, img_url);
                umImage.setTitle(title);
                new ShareAction(context).withMedia(umImage)
                        .setPlatform(share_media)
                        .setCallback(listener)
                        .share();
            } else {
                new ShareAction(context).withText(text)
                        .setPlatform(share_media)
                        .setCallback(listener)
                        .share();
            }
        } else {
            UMWeb web = new UMWeb(url);
            web.setTitle(title);
            web.setDescription(text);
            if (img_url == null) {
                web.setThumb(new UMImage(context, R.drawable.ic_logo_square));
            } else {
                web.setThumb(new UMImage(context, img_url));
            }
            new ShareAction(context).withMedia(web)
                    .setPlatform(share_media)
                    .setCallback(listener)
                    .share();
        }
    }

    public static void showWebShare(final Activity context, String url, final String title, final String text,
                                    final String img_url, final UMShareListener listener) {
        if (url != null && url.contains("_from")) {
            url = url.replaceAll("(_from=[^&]*)", "_from=wap");
        }
        final String finalUrl = url;
        ShareAction action = shareAction(context)
                .addButton("umeng_socialize_text_copy_key", "copy_link", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .addButton("umeng_socialize_text_browser_key", "open_browser", "umeng_socialize_browser", "umeng_socialize_browser")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mKeyword.equals("copy_link")) {
                            AppUtils.copyText(finalUrl);
                            ToastUtils.showToast(context, "已复制到剪贴板");
                        } else if (snsPlatform.mKeyword.equals("open_browser")) {
                            Utils.openWithWeb(context, finalUrl);
                        }
                        doClick(context, finalUrl, title, text, img_url, listener, share_media);
                    }
                });
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        config.setCancelButtonVisibility(false);
        config.setTitleVisibility(false);
        config.setShareboardBackgroundColor(context.getResources().getColor(R.color.white));
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        action.open(config);
    }
}
