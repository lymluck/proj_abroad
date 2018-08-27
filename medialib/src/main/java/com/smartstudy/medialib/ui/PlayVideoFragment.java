package com.smartstudy.medialib.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.smartstudy.commonlib.entity.VideoijkInfo;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.MediaUtils;
import com.smartstudy.medialib.R;
import com.smartstudy.medialib.ijkplayer.listener.OnShowThumbnailListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;

import java.util.List;


public class PlayVideoFragment extends UIFragment {

    private PlayerView player;

    private PowerManager.WakeLock wakeLock;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.layout_play_video, null);
    }

    @Override
    protected void initView(final View rootView) {
        final Bundle data = getArguments();
        List<VideoijkInfo> urls = data.getParcelableArrayList("");
        /**常亮*/
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
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
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }
}
