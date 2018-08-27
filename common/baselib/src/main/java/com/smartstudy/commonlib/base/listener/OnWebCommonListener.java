package com.smartstudy.commonlib.base.listener;

import android.os.Bundle;

import java.util.HashMap;

/**
 * webview回调
 * Created by louis on 17/7/12.
 */

public interface OnWebCommonListener extends OnWebBaseListener {
    void goMQ(HashMap<String, String> clientInfo);

    void goCommandSchool(String id, String name);

    void toAddQa();

    void toTestRate(Bundle bundle);

    void handleSchool(boolean isTrue);

    void handleTitle(String title);

    void toShowShare(boolean showShare, String url, String title, String text, String img_url);

    void onProgress(int newProgress);
}
