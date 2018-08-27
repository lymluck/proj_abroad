package com.smartstudy.commonlib.ui.customview.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AudioRecorder {

    private MediaRecorder recorder;
    private String fileName;
    private String fileFolder = Environment.getExternalStorageDirectory()
            .getPath() + "/xxd-im";
    private MediaPlayer mPlayer;

    private boolean isRecording = false;

    private boolean isPlaying = false;


    private static AudioRecorder instance;

    public static AudioRecorder getInstance() {
        if (null == instance) {
            synchronized (AudioRecorder.class) {
                if (null == instance) {
                    instance = new AudioRecorder();
                }
            }
        }
        return instance;
    }


    public void ready() {
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        fileName = getCurrentDate();
        recorder = new MediaRecorder();
        recorder.setOutputFile(fileFolder + "/" + fileName + ".amr");
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置MediaRecorder录制音频的编码为amr
    }

    // 以当前时间作为文件名
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public void start() {
        if (!isRecording) {
            try {
                recorder.prepare();
                recorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            isRecording = true;
        }

    }

    public void stop() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            isRecording = false;
        }

    }

    public void deleteOldFile() {
        File file = new File(fileFolder + "/" + fileName + ".amr");
        file.deleteOnExit();
    }

    public void playByUri(Context context, Uri uri) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }

        try {
            mPlayer.reset();
            mPlayer.setDataSource(context, uri);
            mPlayer.prepare();
            mPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            Log.e("kim", "prepare() failed");
        }

    }

    public double getAmplitude() {
        if (!isRecording) {
            return 0;
        }
        return recorder.getMaxAmplitude();
    }

    public String getFilePath() {
        return fileFolder + "/" + fileName + ".amr";
    }

    public void play(String path) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }

        try {
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            Log.e("kim", "prepare() failed");
        }
    }

    public void playStop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            isPlaying = false;
        }
    }

    public void playComplete(final PlayComplete playComplete) {
        if (mPlayer != null) {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    playComplete.playComplete();
                    isPlaying = false;
                }
            });
        }
    }


    public void setReset() {
        playStop();
        stop();
        instance = null;
    }

    public interface PlayComplete {
        void playComplete();
    }


    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}


