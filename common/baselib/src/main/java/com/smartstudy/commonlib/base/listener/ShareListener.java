package com.smartstudy.commonlib.base.listener;

import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author louis
 * @date on 2018/3/28
 * @describe 分享回调
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ShareListener implements UMShareListener {

    private String mLink;
    private String mType;

    public ShareListener(String link, String type) {
        this.mLink = link;
        this.mType = type;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        BaseModel.shareCount(mLink, mType);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
