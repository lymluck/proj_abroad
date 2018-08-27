package com.smartstudy.medialib.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smartstudy.commonlib.entity.VideoijkInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.MediaUtils;
import com.smartstudy.medialib.R;
import com.smartstudy.medialib.ijkplayer.listener.OnShowThumbnailListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;

import java.util.List;


public class PlayVideoFragment extends BaseFragment {

    private PlayerView player;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_play_video;
    }

    @Override
    protected void initView() {
        final Bundle data = getArguments();
        List<VideoijkInfo> urls = data.getParcelableArrayList("");
        /**常亮*/
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                player = new PlayerView(mActivity, mActivity.getWindow().getDecorView())
                    .showThumbnail(new OnShowThumbnailListener() {
                        @Override
                        public void onShowThumbnail(ImageView ivThumbnail) {
                            DisplayImageUtils.formatImgUrl(mActivity, data.getString("courseCover"), ivThumbnail);
                        }
                    })
                    .setScaleType(PlayStateParams.fillparent)
                    .forbidScroll(true)
                    .hideRotation(true)
                    .setForbidDoulbeUp(true)
                    .hideSteam(true)
                    .hideControlPanl(true)
                    .hideCenterPlayer(true);
            }
        });
    }


    public void ConfigAndStart(List<VideoijkInfo> list, String title) {
        player.forbidScroll(false)
            .hideControlPanl(false)
            .hideSteam(false)
            .hideBottomBar(false)
            .setTitle(title)
            .setPlaySource(list)
            .startPlay();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }
}
