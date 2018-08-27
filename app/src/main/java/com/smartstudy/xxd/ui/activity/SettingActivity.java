package com.smartstudy.xxd.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.ui.activity.FeedBackActivity;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ImageCacheUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SettingActivityContract;
import com.smartstudy.xxd.mvp.presenter.SettingActivityPresenter;
import com.smartstudy.xxd.service.VersionUpdateService;
import com.smartstudy.xxd.ui.customView.PushSlideSwitchView;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SettingActivity extends UIActivity implements SettingActivityContract.View {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDestroy && application.isDownload()) {
            Intent it = new Intent(SettingActivity.this, VersionUpdateService.class);
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
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        if (ParameterUtils.CACHE_NULL.equals(ticket)) {
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
                    ImageCacheUtils.getInstance().clearImageMemoryCache(getApplicationContext());
                    ToastUtils.showToast(getApplicationContext(), "正在清理缓存...");
                    final Handler handler = new Handler();
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            //要做的事情
                            ImageCacheUtils.getInstance().clearImageDiskCache(getApplicationContext());
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
                ShareUtils.showShare(this, "http://xxd.smartstudy.com/", "选校帝", "轻松出国，简单留学", null, null);
                break;
            case R.id.llyt_check_vs:
                //获取服务器版本
                llytcheckvs.setEnabled(false);
                setP.getVersion();
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
        ToastUtils.showToast(this, message);
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
    public void updateable(final String downUrl, String des) {
        llytcheckvs.setEnabled(true);
        //提示当前有版本更新
        updateDialog = DialogCreator.createAppBasicDialog(this, getString(R.string.version_update), des,
                getString(R.string.update_vs_now), getString(R.string.not_update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.positive_btn:
                                application.setDownload(true);
                                application.setDownLoadUrl(downUrl);
                                //开始下载
                                Intent it = new Intent(SettingActivity.this, VersionUpdateService.class);
                                startService(it);
                                bindService(it, conn, Context.BIND_AUTO_CREATE);
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
        WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        updateDialog.getWindow().setAttributes(p);
        updateDialog.show();
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
                ToastUtils.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onSuccess(Object result) {
                //友盟账号统计
                MobclickAgent.onProfileSignOff();
                SPCacheUtils.put("user_name", ParameterUtils.CACHE_NULL);
                SPCacheUtils.put("user_pic", ParameterUtils.CACHE_NULL);
                SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
                SPCacheUtils.put("user", ParameterUtils.CACHE_NULL);
                SPCacheUtils.put("ss_user", ParameterUtils.CACHE_NULL);
                Utils.removeCookie(SettingActivity.this);
                Intent to_main = new Intent(SettingActivity.this, LoginActivity.class);
                to_main.putExtra("toMain", true);
                to_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(to_main);
            }
        });
    }
}
