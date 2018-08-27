package com.smartstudy.sdk.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.smartstudy.sdk.R;
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

public class UMShareUtils {

    /**
     * 纯分享面板
     *
     * @param context
     * @param url
     * @param title
     * @param text
     * @param img_url
     */
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
        config.setShareboardBackgroundColor(context.getResources().getColor(android.R.color.white));
        config.setTitleVisibility(false);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        action.open(config);
    }

    /**
     * 基本的分享面板
     *
     * @param context
     * @return
     */
    private static ShareAction shareAction(Activity context) {
        return new ShareAction(context).setDisplayList(
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
    }

    /**
     * 分享面板点击事件
     *
     * @param context
     * @param url
     * @param title
     * @param text
     * @param img_url
     * @param listener
     * @param share_media
     */
    private static void doClick(final Activity context, String url, final String title, final String text,
                                final String img_url, UMShareListener listener, SHARE_MEDIA share_media) {
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
            if (TextUtils.isEmpty(img_url)) {
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

    /**
     * 包含菜单的分享面板
     *
     * @param context
     * @param url
     * @param title
     * @param text
     * @param img_url
     */
    public static void showWebShare(final Activity context, String url, final String title, final String text,
                                    final String img_url, final UMShareListener listener) {
        if (url != null && url.contains("_from")) {
            url = url.replaceAll("(_from=[^&]*)", "_from=wap");
        }
        final String finalUrl = url;
        ShareAction action = shareAction(context)
            .addButton(context.getString(R.string.umeng_socialize_text_copy_key), "copy_link", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
            .addButton(context.getString(R.string.umeng_socialize_text_browser_key), "open_browser", "umeng_socialize_browser", "umeng_socialize_browser")
            .setShareboardclickCallback(new ShareBoardlistener() {
                @Override
                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                    if (snsPlatform.mKeyword.equals("copy_link")) {
                        Utils.copyText(context, finalUrl);
                        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    } else if (snsPlatform.mKeyword.equals("open_browser")) {
                        Utils.openWithWeb(context, finalUrl);
                    }
                    doClick(context, finalUrl, title, text, img_url, listener, share_media);
                }
            });
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        config.setCancelButtonVisibility(false);
        config.setShareboardBackgroundColor(context.getResources().getColor(android.R.color.white));
        config.setTitleVisibility(false);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        action.open(config);
    }

    public static void showWeixinFriend(final Activity contex, Bitmap bitmap, String title, UMShareListener listener, SHARE_MEDIA share_media) {
        if (bitmap != null) {
            UMImage umImage = new UMImage(contex, bitmap);
            umImage.setTitle(title);
            new ShareAction(contex).withMedia(umImage)
                .setPlatform(share_media)
                .setCallback(listener)
                .share();
        }
    }
}
