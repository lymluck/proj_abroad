package com.smartstudy.commonlib.utils;

import android.app.Activity;

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
        if (url.contains("_from")) {
            url = url.replaceAll("(_from=[^&]*)", "_from=wap");
        }
        final String finalUrl = url;
        ShareAction action = new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(finalUrl);
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
