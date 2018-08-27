package com.smartstudy.xxd.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.base.callback.ReqProgressCallBack;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.OnProgressListener;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.activity.MainActivity;
import com.smartstudy.xxd.utils.AppContants;

import java.io.File;

public class VersionUpdateService extends Service {

    private static final int NOTIFY_ID = 0;
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    private DownloadBinder binder;
    private BaseApplication app;
    private boolean serviceIsDestroy = false;
    private String apk_path;
    private RemoteViews mContentView;
    private WeakHandler mHandler;
    private String lastVersion;
    /**
     * 更新进度的回调接口
     */
    private static OnProgressListener onProgressListener;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            lastVersion = intent.getStringExtra("version");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 假如被销毁了，无论如何都默认取消了。
        app.setDownload(false);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_PROGRESS:
                        // 更新进度
                        if (progress < 100) {
                            //进度发生变化通知调用方
                            mContentView.setTextViewText(R.id.tv_progress, progress + "%");
                            mContentView.setProgressBar(R.id.progressbar, 100, progress, false);
                        } else {
                            // 下载完毕后变换通知形式
                            mNotification.build().flags = Notification.FLAG_AUTO_CANCEL;
                            mNotification.setContent(null);
                            Intent intent = new Intent(VersionUpdateService.this, MainActivity.class);
                            // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                            PendingIntent contentIntent = PendingIntent.getActivity(VersionUpdateService.this,
                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mNotification.setContentTitle("下载完成");
                            mNotification.setContentText("文件已下载完毕");
                            mNotification.setContentIntent(contentIntent);
                            serviceIsDestroy = true;
                            app.setDownload(false);
                            stopSelf();// 停掉服务自身
                        }
                        mNotificationManager.notify(NOTIFY_ID, mNotification.build());
                        break;
                    case ParameterUtils.MSG_WHAT_ERR:
                        mNotificationManager.cancel(NOTIFY_ID);
                        ToastUtils.showToast((String) msg.obj);
                        break;
                    case ParameterUtils.MSG_WHAT_FINISH:
                        app.setDownload(false);
                        // 下载完毕
                        // 取消通知
                        mNotificationManager.cancel(NOTIFY_ID);
                        handleInstallApk();
                        Utils.installApk(VersionUpdateService.this, apk_path);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(AppContants.XXD_NOTIFY_CHANNEL_ID,
                AppContants.XXD_NOTIFY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //是否在桌面icon右上角展示小红点
            channel.enableLights(true);
            //是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(channel);
        }
        app = BaseApplication.getInstance();
    }

    public class DownloadBinder extends Binder {
        public void start(int flag) {
            progress = 0;
            if (flag == ParameterUtils.FLAG_UPDATE) {
                setUpNotification();
            }
            startDownload(flag);
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void setOnProgressListener(OnProgressListener onProgressListener) {
            VersionUpdateService.onProgressListener = onProgressListener;
        }
    }

    private void startDownload(int flag) {
        canceled = false;
        downloadApk(flag);
    }

    /**
     * 通知
     */
    NotificationCompat.Builder mNotification;


    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();
        mNotification = new NotificationCompat.Builder(VersionUpdateService.this,
            AppContants.XXD_NOTIFY_CHANNEL_ID);
        mNotification.setSmallIcon(icon);
        mNotification.setTicker(tickerText);
        mNotification.setWhen(when);
        // 放置在"正在运行"栏目中
        mNotification.build().flags = Notification.FLAG_ONGOING_EVENT;
        mContentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
        mContentView.setTextViewText(R.id.name, "下载中，请稍候...");
        // 指定个性化视图
        mNotification.setContent(mContentView);

        Intent intent = new Intent(this, MainActivity.class);

        // 指定内容意图
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotification.build());
    }

    public void downloadApk(final int flag) {
        File apkfile_file = SDCardUtils.getFileDirPath("Xxd/file");
        String fileName = AppUtils.getAppName(this) + "_" + lastVersion + ".apk";
        RequestManager.getInstance().downLoadFile(app.getDownLoadUrl(),
            apkfile_file.getAbsolutePath() + File.separator + fileName,
            new ReqProgressCallBack<File>() {

                @Override
                public void onProgress(long total, long current) {
                    int nowProgress = (int) (((float) current / total) * 100);
                    app.setDownload(true);
                    if (flag == ParameterUtils.FLAG_UPDATE) {
                        if (progress < 100 && progress < nowProgress) {
                            progress = nowProgress;
                            mHandler.sendEmptyMessage(ParameterUtils.MSG_WHAT_PROGRESS);
                        }
                    } else if (flag == ParameterUtils.FLAG_UPDATE_NOW) {
                        if (onProgressListener != null) {
                            onProgressListener.onProgress(nowProgress);
                        }
                    }
                }

                @Override
                public void onErr(String errCode, String msg) {
                    Message message = Message.obtain();
                    message.obj = msg;
                    message.what = ParameterUtils.MSG_WHAT_ERR;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onSuccess(File result) {
                    apk_path = result.getAbsolutePath();
                    if (flag == ParameterUtils.FLAG_UPDATE_NOW) {
                        if (onProgressListener != null) {
                            onProgressListener.onFinish(apk_path);
                        }
                    } else if (flag == ParameterUtils.FLAG_UPDATE) {
                        mHandler.sendEmptyMessage(ParameterUtils.MSG_WHAT_FINISH);
                    }
                    // 下载完了，cancelled也要设置
                    canceled = true;
                }
            });
    }

    private void handleInstallApk() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermission) {
                Utils.installApk(getApplicationContext(), apk_path);
            } else {
                ToastUtils.showToast(getString(R.string.install_allow));
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivity(intent);
            }
        } else {
            Utils.installApk(getApplicationContext(), apk_path);
        }
    }
}
