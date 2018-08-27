package com.smartstudy.xxd.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.DialogClickListener;
import com.smartstudy.commonlib.base.listener.OnProgressListener;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.FileUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.StatusBarUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.MainActivityContract;
import com.smartstudy.xxd.mvp.presenter.MainActivityPresenter;
import com.smartstudy.xxd.service.VersionUpdateService;
import com.smartstudy.xxd.ui.fragment.BannerFragment;
import com.smartstudy.xxd.ui.fragment.CourseListFragment;
import com.smartstudy.xxd.ui.fragment.FragmentFactory;
import com.smartstudy.xxd.ui.fragment.MeFragment;
import com.smartstudy.xxd.ui.fragment.QaFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import static com.smartstudy.commonlib.utils.ParameterUtils.MEIQIA_UNREAD;
import static com.smartstudy.commonlib.utils.ParameterUtils.QA_UNREAD;

/**
 * @author luoyongming
 * @date on 2017/2/22
 * @describe 高中院校详情页
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
@Route("MainActivity")
public class MainActivity extends BaseActivity implements MainActivityContract.View {
    private TextView tvTabSchool;
    private TextView tvTabCourse;
    private TextView tvTabNews;
    private TextView tvTabQa;
    private TextView tvTabMe;
    private AppBasicDialog updateDialog;
    private ProgressBar progressbar;
    private TextView tvProgress;
    private TextView tabMeRed;
    private TextView tabQaRed;

    private FragmentManager mfragmentManager;
    //美国本科首页
    private BannerFragment bannerFragment;
    private MeFragment meFragment;
    private QaFragment qaFragment;
    private int updateType;
    private VersionUpdateService.DownloadBinder binder;
    private boolean isBinded;
    private boolean isDestroy = true;
    private String apkPath;
    private MainActivityContract.Presenter mainP;
    private WeakHandler myHandler;
    private Bundle state;
    private BaseFragment lastFragment;
    private String lastVersion;
    private ServiceConnection conn;
    private FragmentFactory fragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        state = savedInstanceState;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViewAndData() {
        initServiceConn();
        LinearLayout llTab = findViewById(R.id.llyt_tab);
        tvTabSchool = findViewById(R.id.tv_tab_school);
        tvTabCourse = findViewById(R.id.tv_tab_course);
        tvTabNews = findViewById(R.id.tv_tab_news);
        tvTabQa = findViewById(R.id.tv_tab_qa);
        tvTabMe = findViewById(R.id.tv_tab_me);
        tabMeRed = findViewById(R.id.tab_me_red);
        tabQaRed = findViewById(R.id.tab_qa_red);
        fragmentFactory = new FragmentFactory();
        mfragmentManager = getSupportFragmentManager();
        new MainActivityPresenter(this);
        mainP.isGetMyPlan();
        if (getIntent().hasExtra("flag")) {
            addActivity(this);
            //判断是否是从其它入口过来的
            int flag = getIntent().getIntExtra("flag", 0);
            if (flag == ParameterUtils.FRAGMENT_TAB_TWO) {
                llTab.setVisibility(View.GONE);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_TWO);
            } else if (flag == ParameterUtils.FRAGMENT_TAB_THREE) {
                llTab.setVisibility(View.GONE);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_THREE);
            } else if (flag == ParameterUtils.FRAGMENT_TAB_THOUR) {
                llTab.setVisibility(View.GONE);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_THOUR);
            }
        } else {
            if (state == null) {
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_ONE);
            }
        }
        releaseDialog();
        if (!BaseApplication.getInstance().isDownload()) {
            String channel = WalleChannelReader.getChannel(this, "xxd");
            mainP.getVersion(channel);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //继续下载apk
        if (isDestroy && BaseApplication.getInstance().isDownload()) {
            Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
            it.putExtra("version", lastVersion);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
        // 我的未读消息处理
        int meiqiaUnread = (int) SPCacheUtils.get(MEIQIA_UNREAD, 0);
        if (meiqiaUnread > 0) {
            tabMeRed.setVisibility(View.VISIBLE);
        } else {
            tabMeRed.setVisibility(View.GONE);
        }
        // 问答未读消息处理
        int qaUnread = (int) SPCacheUtils.get(QA_UNREAD, 0);
        showQaTabRed(qaUnread);
        if (qaFragment != null) {
            qaFragment.showMyQaRed(qaUnread);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDestroy = false;
        EventBus.getDefault().unregister(this);
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(MainActivity.class.getSimpleName());
        if (isBinded) {
            unbindService(conn);
        }
        if (binder != null && binder.isCanceled()) {
            Intent it = new Intent(this, VersionUpdateService.class);
            it.putExtra("version", lastVersion);
            stopService(it);
            binder = null;
        }
        if (myHandler != null) {
            myHandler = null;
        }
        if (mainP != null) {
            mainP = null;
        }
        if (state != null) {
            state = null;
        }
        releaseDialog();
    }

    private void releaseDialog() {
        if (updateDialog != null) {
            updateDialog.dismiss();
            updateDialog = null;
        }
    }

    public void showQaTabRed(int qaUnread) {
        if (qaUnread > 0) {
            showQaTabRed();
        } else {
            tabQaRed.setVisibility(View.GONE);
        }
    }

    public void showQaTabRed() {
        tabQaRed.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgMeiQiaEvent msgMeiQiaEvent) {
        //美洽未读消息处理
        if (msgMeiQiaEvent.getUnRead() > 0) {
            tabMeRed.setVisibility(View.VISIBLE);
        } else {
            tabMeRed.setVisibility(View.GONE);
        }
        if (meFragment != null) {
            meFragment.handleMeiQiaRed(msgMeiQiaEvent.getUnRead());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgXxdEvent msgXxdEvent) {
        //极光未读消息处理
        if (bannerFragment != null && bannerFragment.isAdded()) {
            bannerFragment.handleMsgRed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgQaEvent msgQaEvent) {
        // 问答tab未读消息处理
        showQaTabRed(msgQaEvent.getUnRead());
        // 我的问答消息红点展示
        if (qaFragment != null && qaFragment.isAdded()) {
            qaFragment.showMyQaRed(msgQaEvent.getUnRead());
        }
    }

    @Override
    public void initEvent() {
        myHandler = new WeakHandler(new Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.FLAG_UPDATE:
                        progressbar.setProgress(msg.arg1);
                        tvProgress.setText(msg.arg1 + "%");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        tvTabSchool.setOnClickListener(this);
        tvTabCourse.setOnClickListener(this);
        tvTabNews.setOnClickListener(this);
        findViewById(R.id.rlyt_qa).setOnClickListener(this);
        findViewById(R.id.rlyt_me).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        canFastClick = true;
        super.onClick(v);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_school:
                if (bannerFragment != null && !bannerFragment.isDarkMode()) {
                    StatusBarUtils.setDarkMode(this);
                }
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_ONE);
                break;
            case R.id.tv_tab_course:
                StatusBarUtils.setLightMode(this);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_TWO);
                break;
            case R.id.tv_tab_news:
                StatusBarUtils.setLightMode(this);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_THREE);
                break;
            case R.id.rlyt_qa:
                StatusBarUtils.setLightMode(this);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_THOUR);
                break;
            case R.id.rlyt_me:
                StatusBarUtils.setLightMode(this);
                mainP.showFragment(fragmentFactory, ParameterUtils.FRAGMENT_TAB_FIVE);
                break;
            default:
                break;
        }
    }

    /**
     * 保存fragment状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ParameterUtils.FRAGMENT_TAG, mainP.currentIndex());
        super.onSaveInstanceState(outState);
    }

    /**
     * 复位fragment状态
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mainP.showFragment(fragmentFactory, savedInstanceState.getInt(ParameterUtils.FRAGMENT_TAG));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * hide最近展示的fragment
     */
    @Override
    public void hideLastFragment(BaseFragment nowFragment) {
        //如果不为空，就先隐藏起来
        if (lastFragment != null) {
            if (!lastFragment.getClass().getSimpleName().equals(nowFragment.getClass().getSimpleName())) {
                hideFragment(lastFragment);
            }
        } else {
            //隐藏所有
            if (mfragmentManager != null && mfragmentManager.getFragments().size() > 0) {
                for (Fragment frg : mfragmentManager.getFragments()) {
                    frg.setUserVisibleHint(false);
                    hideFragment(frg);
                }
            }
        }
        tvTabSchool.setSelected(false);
        tvTabCourse.setSelected(false);
        tvTabNews.setSelected(false);
        tvTabQa.setSelected(false);
        tvTabMe.setSelected(false);
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            fragment.setUserVisibleHint(false);
            if (fragment.isAdded()) {
                mfragmentManager.beginTransaction().hide(fragment)
                    .commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onShowFragment(BaseFragment fragment) {
        FragmentTransaction ft = mfragmentManager.beginTransaction();
        if (fragment instanceof BannerFragment) {
            bannerFragment = (BannerFragment) fragment;
        } else if (fragment instanceof CourseListFragment) {
            Bundle data = new Bundle();
            data.putString("data_flag", "home");
            fragment.setArguments(data);
        } else if (fragment instanceof QaFragment) {
            qaFragment = (QaFragment) fragment;
        } else if (fragment instanceof MeFragment) {
            meFragment = (MeFragment) fragment;
        }
        mfragmentManager.executePendingTransactions();
        if (!fragment.isAdded()) {
            ft.add(R.id.content_fg, fragment);
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        lastFragment = fragment;
        ft = null;
    }

    @Override
    public void selectedTab(int index) {
        switch (index) {
            case ParameterUtils.FRAGMENT_TAB_ONE:
                tvTabSchool.setSelected(true);
                break;
            case ParameterUtils.FRAGMENT_TAB_TWO:
                tvTabCourse.setSelected(true);
                break;
            case ParameterUtils.FRAGMENT_TAB_THREE:
                tvTabNews.setSelected(true);
                break;
            case ParameterUtils.FRAGMENT_TAB_THOUR:
                tvTabQa.setSelected(true);
                break;
            case ParameterUtils.FRAGMENT_TAB_FIVE:
                tvTabMe.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        if (presenter != null) {
            this.mainP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    /**
     * 连接版本下载service
     */
    private void initServiceConn() {
        if (conn == null) {
            conn = new ServiceConnection() {

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isBinded = false;
                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (VersionUpdateService.DownloadBinder) service;
                    // 开始下载
                    isBinded = true;
                    binder.start(updateType);
                    if (updateType == ParameterUtils.FLAG_UPDATE_NOW) {
                        final Button btn = updateDialog.findViewById(R.id.dialog_base_confirm_btn);
                        final TextView title = updateDialog.findViewById(R.id.dialog_base_title_tv);
                        btn.setVisibility(View.GONE);
                        updateDialog.findViewById(R.id.dialog_base_text_tv).setVisibility(View.GONE);
                        updateDialog.findViewById(R.id.llyt_progress).setVisibility(View.VISIBLE);
                        title.setText(R.string.start_update);
                        binder.setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onProgress(int progress) {
                                if (progress < 100) {
                                    Message msg = Message.obtain();
                                    msg.what = ParameterUtils.FLAG_UPDATE;
                                    msg.arg1 = progress;
                                    myHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void onFinish(final String path) {
                                apkPath = path;
                                progressbar.setProgress(100);
                                tvProgress.setText("100%");
                                btn.setVisibility(View.VISIBLE);
                                btn.setText(R.string.install);
                                title.setText(R.string.install_now);
                            }
                        });
                    }
                }
            };
        }
    }

    /**
     * 提示更新，由用户选择是否更新
     *
     * @param downUrl
     * @param des
     */
    @Override
    public void updateable(final String downUrl, String version, String des) {
        this.lastVersion = version;
        updateType = ParameterUtils.FLAG_UPDATE;
        //提示当前有版本更新
        String isToUpdate = (String) SPCacheUtils.get("isToUpdate", "");
        if ("".equals(isToUpdate) || "yes".equals(isToUpdate)) {
            File apkFile = SDCardUtils.getFileDirPath("Xxd/file");
            String fileName = AppUtils.getAppName(this) + "_" + lastVersion + ".apk";
            String filePath = apkFile.getAbsolutePath() + File.separator + fileName;
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
                                    Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                                    it.putExtra("version", lastVersion);
                                    startService(it);
                                    bindService(it, conn, Context.BIND_AUTO_CREATE);
                                }
                                updateDialog.dismiss();
                                break;
                            case R.id.negative_btn:
                                SPCacheUtils.put("isToUpdate", "no");
                                updateDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            ((TextView) updateDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER_VERTICAL);
            updateDialog.show();
        }
    }

    /**
     * 强制更新
     *
     * @param downUrl
     * @param des
     */
    @Override
    public void forceUpdate(final String downUrl, String version, String des) {
        this.lastVersion = version;
        updateType = ParameterUtils.FLAG_UPDATE_NOW;
        File apkFile = SDCardUtils.getFileDirPath("Xxd/file");
        String fileName = AppUtils.getAppName(this) + "_" + lastVersion + ".apk";
        String filePath = apkFile.getAbsolutePath() + File.separator + fileName;
        final boolean exist = FileUtils.fileIsExists(filePath);
        String title = getString(R.string.update_vs_now);
        if (exist) {
            apkPath = filePath;
            title = getString(R.string.install_now);
        }
        updateDialog = DialogCreator.createBaseCustomDialog(MainActivity.this, title, des, new DialogClickListener() {
            @Override
            public void onClick(AppBasicDialog dialog, View v) {
                if (apkPath != null) {
                    handleInstallApk();
                } else {
                    BaseApplication.getInstance().setDownload(true);
                    BaseApplication.getInstance().setDownLoadUrl(downUrl);
                    //开始下载
                    Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                    it.putExtra("version", lastVersion);
                    startService(it);
                    bindService(it, conn, Context.BIND_AUTO_CREATE);
                }
            }
        }, null);
        progressbar = (ProgressBar) updateDialog.findViewById(R.id.progressbar);
        tvProgress = (TextView) updateDialog.findViewById(R.id.tv_progress);
        updateDialog.findViewById(R.id.dialog_base_confirm_btn).setBackgroundResource(R.drawable.bg_btn_wm_lrb_radius);
        ((TextView) updateDialog.findViewById(R.id.dialog_base_text_tv)).setGravity(Gravity.CENTER_VERTICAL);
        updateDialog.show();
    }

    private void handleInstallApk() {
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

    public boolean isQaRed() {
        return tabQaRed.isShown();
    }

    @Override
    public void showQaRed(int visivle) {
        tabQaRed.setVisibility(visivle);
        // 我的问答消息红点展示
        if (qaFragment != null) {
            qaFragment.showMyQaRedIsVisible(visivle);
        }
    }

    @Override
    public void refreshBannerFragment(boolean show) {
        if (bannerFragment != null && bannerFragment.isAdded()) {
            bannerFragment.setAvailable(show);
            bannerFragment.setUserVisibleHint(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_MANAGE_APP_SOURCE:
                handleInstallApk();
                break;
            default:
                break;
        }
    }

}
