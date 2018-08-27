package com.smartstudy.xxd.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.ui.activity.FeedBackActivity;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.FileUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ImageCacheUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SettingActivityContract;
import com.smartstudy.xxd.mvp.presenter.SettingActivityPresenter;
import com.smartstudy.xxd.service.VersionUpdateService;
import com.smartstudy.xxd.ui.customview.PushSlideSwitchView;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.smartstudy.commonlib.utils.ParameterUtils.MEIQIA_UNREAD;
import static com.smartstudy.commonlib.utils.ParameterUtils.QA_UNREAD;
import static com.smartstudy.commonlib.utils.ParameterUtils.XXD_UNREAD;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

public class SettingActivity extends BaseActivity implements SettingActivityContract.View {

    private PushSlideSwitchView viewpushbtn;
    private TextView tvvs;
    private LinearLayout llytcheckvs;
    private LinearLayout llytappshare;
    private RelativeLayout rlytaboutus;
    private TextView tvcachesize;
    private LinearLayout llytclearcache;
    private LinearLayout btnexitlogin;
    private AppBasicDialog updateDialog;

    private VersionUpdateService.DownloadBinder binder;
    private boolean isBinded;
    private boolean isDestroy = true;
    private SettingActivityContract.Presenter setP;
    private String lastVersion;
    private String apkPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDestroy && BaseApplication.getInstance().isDownload()) {
            Intent it = new Intent(SettingActivity.this, VersionUpdateService.class);
            it.putExtra("version", lastVersion);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinded) {
            unbindService(conn);
        }
        if (binder != null && binder.isCanceled()) {
            Intent it = new Intent(this, VersionUpdateService.class);
            it.putExtra("version", lastVersion);
            stopService(it);
        }
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("设置");
        this.btnexitlogin = (LinearLayout) findViewById(R.id.btn_exit_login);
        this.llytclearcache = (LinearLayout) findViewById(R.id.llyt_clear_cache);
        this.tvcachesize = (TextView) findViewById(R.id.tv_cache_size);
        this.rlytaboutus = (RelativeLayout) findViewById(R.id.rlyt_about_us);
        this.llytappshare = (LinearLayout) findViewById(R.id.llyt_app_share);
        this.llytcheckvs = (LinearLayout) findViewById(R.id.llyt_check_vs);
        this.tvvs = (TextView) findViewById(R.id.tv_vs);
        this.viewpushbtn = (PushSlideSwitchView) findViewById(R.id.view_push_btn);
        if (JPushInterface.isPushStopped(getApplicationContext())) {
            viewpushbtn.setChecked(false);
        }
        llytclearcache.setEnabled(true);
        tvcachesize.setText(ImageCacheUtils.getInstance().getCacheSize(this));
        String ticket = (String) SPCacheUtils.get("ticket", "");
        if (TextUtils.isEmpty(ticket)) {
            btnexitlogin.setVisibility(GONE);
        } else {
            btnexitlogin.setVisibility(VISIBLE);
        }
        tvvs.setText(getString(R.string.current) + AppUtils.getVersionName());
        new SettingActivityPresenter(this);
    }

    @Override
    public void initEvent() {
        viewpushbtn.setOnChangeListener(new PushSlideSwitchView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChange(PushSlideSwitchView switchView, boolean isChecked) {
                if (isChecked) {
                    if (JPushInterface.isPushStopped(getApplicationContext())) {
                        JPushInterface.resumePush(getApplicationContext());
                    }
                } else {
                    JPushInterface.stopPush(getApplicationContext());
                }
            }
        });
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        btnexitlogin.setOnClickListener(this);
        rlytaboutus.setOnClickListener(this);
        llytcheckvs.setOnClickListener(this);
        llytappshare.setOnClickListener(this);
        llytclearcache.setOnClickListener(this);
        findViewById(R.id.llyt_opinion).setOnClickListener(this);

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.btn_exit_login:
                logout();
                break;
            case R.id.llyt_clear_cache:
                SPCacheUtils.remove("adImg");
                if (!"0.0Byte".equals(tvcachesize.getText())) {
                    llytclearcache.setEnabled(false);
                    // 清除图片内存缓存
                    ImageCacheUtils.getInstance().clearImageMemoryCache(getApplicationContext());
                    ToastUtils.showToast("正在清理缓存...");
                    final Handler handler = new Handler();
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            // 清除图片磁盘缓存
                            ImageCacheUtils.getInstance().clearImageDiskCache(getApplicationContext(), true);
                            tvcachesize.setText(ImageCacheUtils.getInstance().getCacheSize(getApplicationContext()));
                            if (!"0.0Byte".equals(tvcachesize.getText())) {
                                handler.postDelayed(this, 1000);
                            }
                        }
                    };
                    handler.post(task);
                }
                break;
            case R.id.llyt_opinion:
                startActivity(new Intent(SettingActivity.this, FeedBackActivity.class));
                break;
            case R.id.rlyt_about_us:
                Intent to_about_us = new Intent(SettingActivity.this, AboutXxdActivity.class);
                startActivity(to_about_us);
                break;
            case R.id.llyt_app_share:
                String url = "http://xxd.smartstudy.com/";
                UMShareUtils.showShare(this, url, "选校帝",
                    "轻松出国，简单留学", null, new ShareListener(url, "app_xxd"));
                break;
            case R.id.llyt_check_vs:
                //获取服务器版本
                llytcheckvs.setEnabled(false);
                String channel = WalleChannelReader.getChannel(this, "xxd");
                setP.getVersion(channel);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SettingActivityContract.Presenter presenter) {
        if (presenter != null) {
            this.setP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        llytcheckvs.setEnabled(true);
        ToastUtils.showToast(message);
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (VersionUpdateService.DownloadBinder) service;
            // 开始下载
            isBinded = true;
            binder.start(ParameterUtils.FLAG_UPDATE);
        }
    };

    @Override
    public void updateable(final String downUrl, String version, String des) {
        this.lastVersion = version;
        llytcheckvs.setEnabled(true);
        //提示当前有版本更新
        File apkfile_file = SDCardUtils.getFileDirPath("Xxd/file");
        String fileName = AppUtils.getAppName(this) + "_" + lastVersion + ".apk";
        final String filePath = apkfile_file.getAbsolutePath() + File.separator + fileName;
        final boolean exist = FileUtils.fileIsExists(filePath);
        String positiveTxt = getString(R.string.update_vs_now);
        String title = getString(R.string.version_update);
        if (exist) {
            apkPath = filePath;
            title = getString(R.string.version_install);
            positiveTxt = getString(R.string.install_now);
        }
        updateDialog = DialogCreator.createAppBasicDialog(this, title, des,
            positiveTxt, getString(R.string.not_update), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.positive_btn:
                            if (exist) {
                                handleInstallApk();
                            } else {
                                BaseApplication.getInstance().setDownload(true);
                                BaseApplication.getInstance().setDownLoadUrl(downUrl);
                                //开始下载
                                Intent it = new Intent(SettingActivity.this, VersionUpdateService.class);
                                it.putExtra("version", lastVersion);
                                startService(it);
                                bindService(it, conn, Context.BIND_AUTO_CREATE);
                            }
                            updateDialog.dismiss();
                            break;
                        case R.id.negative_btn:
                            updateDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            });
        ((TextView) updateDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER_VERTICAL);
        if (!isFinishing()) {
            updateDialog.show();
        }
    }

    //退出登录
    private void logout() {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_USER_LOGOUT);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, new BaseCallback() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onSuccess(Object result) {
                //友盟账号统计
                UApp.logout();
                // SensorsData
                SensorsUtils.trackLogout();
                SPCacheUtils.remove(USER_NAME);
                SPCacheUtils.remove("user_pic");
                SPCacheUtils.remove("ticket");
                SPCacheUtils.remove("user");
                SPCacheUtils.remove("ss_user");
                SPCacheUtils.remove("user_id");
                SPCacheUtils.remove("zhike_id");
                SPCacheUtils.remove(QA_UNREAD);
                SPCacheUtils.remove(XXD_UNREAD);
                SPCacheUtils.remove(MEIQIA_UNREAD);
                Utils.removeCookie(SettingActivity.this);
                Intent to_login = new Intent(SettingActivity.this, LoginActivity.class);
                to_login.putExtra("toMain", true);
                to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(to_login);
            }
        });
    }

    /**
     * 涉及到分享时必须调用到方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_MANAGE_APP_SOURCE:
                handleInstallApk();
                break;
            default:
                break;
        }
    }

    private void handleInstallApk() {
        if (!TextUtils.isEmpty(apkPath)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (hasInstallPermission) {
                    Utils.installApk(getApplicationContext(), apkPath);
                } else {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                        ParameterUtils.REQUEST_CODE_INSTALL);
                }
            } else {
                Utils.installApk(getApplicationContext(), apkPath);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_INSTALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.installApk(getApplicationContext(), apkPath);
                } else {
                    ToastUtils.showToast(getString(R.string.install_allow));
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, ParameterUtils.REQUEST_CODE_MANAGE_APP_SOURCE);
                }
                break;
            default:
                break;
        }
    }
}
