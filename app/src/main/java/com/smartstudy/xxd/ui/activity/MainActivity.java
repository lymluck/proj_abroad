package com.smartstudy.xxd.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.OnProgressListener;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.MainActivityContract;
import com.smartstudy.xxd.mvp.presenter.MainActivityPresenter;
import com.smartstudy.xxd.service.VersionUpdateService;
import com.smartstudy.xxd.ui.fragment.CourseListFragment;
import com.smartstudy.xxd.ui.fragment.MeFragment;
import com.smartstudy.xxd.ui.fragment.NewsFragment;
import com.smartstudy.xxd.ui.fragment.QaFragment;
import com.smartstudy.xxd.ui.fragment.SchoolFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by louis on 2017/2/22.
 */
@Route("MainActivity")
public class MainActivity extends UIActivity implements MainActivityContract.View {
    private TextView tv_tab_school;
    private TextView tv_tab_course;
    private TextView tv_tab_news;
    private TextView tv_tab_qa;
    private TextView tv_tab_me;
    private AppBasicDialog updateDialog;
    private ProgressBar progressbar;
    private TextView tv_progress;
    private LinearLayout llyt_tab;
    private TextView tab_me_red;

    private FragmentManager mfragmentManager;
    private SchoolFragment schoolFragment;
    private CourseListFragment courseListFragment;
    private NewsFragment newsFragment;
    private QaFragment qaFragment;
    private MeFragment meFragment;
    private int update_type;
    private VersionUpdateService.DownloadBinder binder;
    private boolean isBinded;
    private boolean isDestroy = true;
    private String apk_path;
    private MainActivityContract.Presenter mainP;
    private WeakHandler myHandler;
    private SystemBarTintManager tintManager;
    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        state = savedInstanceState;
        setContentView(R.layout.activity_main);
    }

    /**
     * 初始化状态栏
     */
    private void initSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            setTranslucentStatus(true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_top_color);
            controlDarkMode(false);
        }
    }

    @Override
    protected void initViewAndData() {
        initSysBar();
        llyt_tab = (LinearLayout) findViewById(R.id.llyt_tab);
        tv_tab_school = (TextView) findViewById(R.id.tv_tab_school);
        tv_tab_course = (TextView) findViewById(R.id.tv_tab_course);
        tv_tab_news = (TextView) findViewById(R.id.tv_tab_news);
        tv_tab_qa = (TextView) findViewById(R.id.tv_tab_qa);
        tv_tab_me = (TextView) findViewById(R.id.tv_tab_me);
        tab_me_red = (TextView) findViewById(R.id.tab_me_red);
        mfragmentManager = getSupportFragmentManager();
        new MainActivityPresenter(this);
        if (getIntent().hasExtra("flag")) {
            addActivity(this);
            //判断是否是从其它入口过来的
            int flag = getIntent().getIntExtra("flag", 0);
            if (flag == ParameterUtils.FRAGMENT_ONE) {
                llyt_tab.setVisibility(View.GONE);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
            } else if (flag == ParameterUtils.FRAGMENT_TWO) {
                llyt_tab.setVisibility(View.GONE);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_TWO);
            } else if (flag == ParameterUtils.FRAGMENT_THREE) {
                llyt_tab.setVisibility(View.GONE);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_THREE);
            }
        } else {
            hideFragment(mfragmentManager);
            if (state == null) {
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
            }
        }
        releaseDialog();
        if (!application.isDownload()) {
            String channel = WalleChannelReader.getChannel(this, "xxd");
            mainP.getVersion(channel);
        }
        mainP.isGetMyPlan();
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
        if (isDestroy && application.isDownload()) {
            Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
        //美洽未读消息处理
        int meiqia_unread = (int) SPCacheUtils.get("meiqia_unread", 0);
        int xxd_unread = (int) SPCacheUtils.get("xxd_unread", 0);
        if (meiqia_unread > 0 || xxd_unread > 0) {
            tab_me_red.setVisibility(View.VISIBLE);
        } else {
            tab_me_red.setVisibility(View.GONE);
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
            stopService(it);
            binder = null;
        }
        if (application != null) {
            application = null;
        }
        if (myHandler != null) {
            myHandler = null;
        }
        if (tintManager != null) {
            tintManager = null;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgMeiQiaEvent msgMeiQiaEvent) {
        //美洽未读消息处理
        if (msgMeiQiaEvent.getUnRead() > 0) {
            tab_me_red.setVisibility(View.VISIBLE);
        } else {
            tab_me_red.setVisibility(View.GONE);
        }
        if (meFragment != null) {
            meFragment.handleMeiQiaRed(msgMeiQiaEvent.getUnRead());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgXxdEvent msgXxdEvent) {
        //极光未读消息处理
        if (msgXxdEvent.getUnRead() > 0) {
            tab_me_red.setVisibility(View.VISIBLE);
        } else {
            tab_me_red.setVisibility(View.GONE);
        }
        if (meFragment != null) {
            meFragment.handleMsgRed(msgXxdEvent.getUnRead());
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
                        tv_progress.setText(msg.arg1 + "%");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        tv_tab_school.setOnClickListener(this);
        tv_tab_course.setOnClickListener(this);
        tv_tab_news.setOnClickListener(this);
        tv_tab_qa.setOnClickListener(this);
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
                if (schoolFragment != null && !schoolFragment.isDarkMode()) {
                    controlDarkMode(false);
                }
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
                break;
            case R.id.tv_tab_course:
                controlDarkMode(true);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_TWO);
                break;
            case R.id.tv_tab_news:
                controlDarkMode(true);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_THREE);
                break;
            case R.id.tv_tab_qa:
                controlDarkMode(true);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_THOUR);
                break;
            case R.id.rlyt_me:
                controlDarkMode(true);
                mainP.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_FIVE);
                break;
            default:
                break;
        }
    }

    //控制状态栏字体颜色
    public void controlDarkMode(boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarLightMode(this, darkMode);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (darkMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
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
            hideFragment(mfragmentManager);
            mainP.showFragment(mfragmentManager, savedInstanceState.getInt(ParameterUtils.FRAGMENT_TAG));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * hide所有fragment
     *
     * @param fragmentManager
     */
    @Override
    public void hideFragment(FragmentManager fragmentManager) {
        //如果不为空，就先隐藏起来
        if (fragmentManager != null && fragmentManager.getFragments().size() > 0) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                fragment.setUserVisibleHint(false);
                if (fragment.isAdded()) {
                    fragmentManager.beginTransaction().hide(fragment)
                            .commitAllowingStateLoss();
                }
            }
        }
        tv_tab_school.setSelected(false);
        tv_tab_course.setSelected(false);
        tv_tab_news.setSelected(false);
        tv_tab_qa.setSelected(false);
        tv_tab_me.setSelected(false);

    }

    /**
     * 展示院校
     *
     * @param ft
     */
    @Override
    public void showSchools(FragmentTransaction ft) {
        tv_tab_school.setSelected(true);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (schoolFragment == null) {
            schoolFragment = new SchoolFragment();
            ft.add(R.id.content, schoolFragment);
        } else {
            ft.show(schoolFragment);
        }
        schoolFragment.setUserVisibleHint(true);
        ft = null;
    }

    /**
     * 展示课程
     *
     * @param ft
     */
    @Override
    public void showCourse(FragmentTransaction ft) {
        tv_tab_course.setSelected(true);
        if (courseListFragment == null) {
            courseListFragment = new CourseListFragment();
            Bundle data = new Bundle();
            data.putString("data_flag", "home");
            courseListFragment.setArguments(data);
            ft.add(R.id.content, courseListFragment);
        } else {
            ft.show(courseListFragment);
        }
        courseListFragment.setUserVisibleHint(true);
        ft = null;
    }


    /**
     * 展示资讯
     *
     * @param ft
     */
    @Override
    public void showNews(FragmentTransaction ft) {
        tv_tab_news.setSelected(true);
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
            ft.add(R.id.content, newsFragment);
        } else {
            ft.show(newsFragment);
        }
        newsFragment.setUserVisibleHint(true);
        ft = null;
    }

    /**
     * 展示问答
     *
     * @param ft
     */
    @Override
    public void showQa(FragmentTransaction ft) {
        tv_tab_qa.setSelected(true);
        if (qaFragment == null) {
            qaFragment = new QaFragment();
            Bundle data = new Bundle();
            data.putString("data_flag", "list");
            qaFragment.setArguments(data);
            ft.add(R.id.content, qaFragment);
        } else {
            ft.show(qaFragment);
        }
        qaFragment.setUserVisibleHint(true);
        ft = null;
    }

    /**
     * 展示我的
     *
     * @param ft
     */
    @Override
    public void showMe(FragmentTransaction ft) {
        tv_tab_me.setSelected(true);
        if (meFragment == null) {
            meFragment = new MeFragment();
            ft.add(R.id.content, meFragment);
        } else {
            ft.show(meFragment);
        }
        meFragment.setUserVisibleHint(true);
        ft = null;
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        if (presenter != null) {
            this.mainP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    /**
     * 连接版本下载service
     */
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
            binder.start(update_type);
            if (update_type == ParameterUtils.FLAG_UPDATE_NOW) {
                final Button btn = (Button) updateDialog.findViewById(R.id.dialog_base_confirm_btn);
                final TextView title = (TextView) updateDialog.findViewById(R.id.dialog_base_title_tv);
                btn.setVisibility(View.GONE);
                updateDialog.findViewById(R.id.dialog_base_text_tv).setVisibility(View.GONE);
                updateDialog.findViewById(R.id.llyt_progress).setVisibility(View.VISIBLE);
                title.setText("开始更新");
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
                        apk_path = path;
                        progressbar.setProgress(100);
                        tv_progress.setText("100%");
                        btn.setVisibility(View.VISIBLE);
                        btn.setText("安装");
                        title.setText("立即安装");
                    }
                });
            }
        }
    };

    /**
     * 提示更新，由用户选择是否更新
     *
     * @param downUrl
     * @param des
     */
    @Override
    public void updateable(final String downUrl, final String version, String des) {
        update_type = ParameterUtils.FLAG_UPDATE;
        //提示当前有版本更新
        String isToUpdate = (String) SPCacheUtils.get("isToUpdate", "");
        if ("".equals(isToUpdate) || "yes".equals(isToUpdate)) {
            updateDialog = DialogCreator.createAppBasicDialog(this, getString(R.string.version_update), des,
                    getString(R.string.update_vs_now), getString(R.string.not_update), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.positive_btn:
                                    application.setDownload(true);
                                    application.setDownLoadUrl(downUrl);
                                    //开始下载
                                    Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                                    it.putExtra("version", version);
                                    startService(it);
                                    bindService(it, conn, Context.BIND_AUTO_CREATE);
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
            WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
            p.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
            updateDialog.getWindow().setAttributes(p);
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
    public void forceUpdate(final String downUrl, final String version, String des) {
        update_type = ParameterUtils.FLAG_UPDATE_NOW;
        String title = getString(R.string.update_vs_now);
        updateDialog = DialogCreator.createBaseCustomDialog(MainActivity.this, title, des, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apk_path != null) {
                    Utils.installApk(getApplicationContext(), apk_path);
                } else {
                    application.setDownload(true);
                    application.setDownLoadUrl(downUrl);
                    //开始下载
                    Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                    it.putExtra("version", version);
                    startService(it);
                    bindService(it, conn, Context.BIND_AUTO_CREATE);
                }
            }
        });
        progressbar = (ProgressBar) updateDialog.findViewById(R.id.progressbar);
        tv_progress = (TextView) updateDialog.findViewById(R.id.tv_progress);
        updateDialog.findViewById(R.id.dialog_base_confirm_btn).setBackgroundResource(R.drawable.bg_btn_wm_lrb_radius);
        ((TextView) updateDialog.findViewById(R.id.dialog_base_text_tv)).setGravity(Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        updateDialog.getWindow().setAttributes(p);
        updateDialog.show();
    }

    @Override
    public void showPlanDialog(boolean show) {
        String key = SPCacheUtils.get("user_id", "").toString() + "QA";
        if (!show && !(Boolean) SPCacheUtils.get(key, false)) {
            if (!this.isFinishing()) {
                if (SPCacheUtils.get("TARGE_COUNTRY", "").equals("COUNTRY_226")) {
                    DialogCreator.createHomeQaDialog(this);
                }
            }
        }
    }
}
