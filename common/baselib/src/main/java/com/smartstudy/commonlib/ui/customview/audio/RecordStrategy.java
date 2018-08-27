package com.smartstudy.commonlib.ui.customview.audio;

import android.content.Context;
import android.net.Uri;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface RecordStrategy {
    /**
     * 在这里进行录音准备工作，重置录音文件名等
     */
    public void ready();

    /**
     * 开始录音
     */
    public void start();

    /**
     * 录音结束
     */
    public void stop();

    /**
     * 录音失败时删除原来的旧文件
     */
    public void deleteOldFile();


    public void playByUri(Context context, Uri uri);

    /**
     * 获取录音音量的大小
     *
     * @return
     */
    public double getAmplitude();

    /**
     * 返回录音文件完整路径
     *
     * @return
     */
    public String getFilePath();


    /**
     * 录音播放
     */
    public void play(String path);

    /**
     * 播放停止
     */

    public void playStop();


    /**
     * 播放完成
     */
    public void playComplete(final AudioRecorder.PlayComplete playComplete);


    public void playReset();
}
