package com.smartstudy.xxd.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.FloatingActionButton;
import com.smartstudy.commonlib.ui.customview.FloatingActionMenu;
import com.smartstudy.commonlib.ui.customview.calendar.component.CirclePointMonthView;
import com.smartstudy.commonlib.ui.customview.calendar.component.MonthView;
import com.smartstudy.commonlib.ui.customview.calendar.entity.CalendarInfo;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.FastBlur;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ExamDateInfo;
import com.smartstudy.xxd.listener.ExamDialogClickListener;
import com.smartstudy.xxd.mvp.contract.ExamDateContract;
import com.smartstudy.xxd.mvp.presenter.ExamDatePresenter;
import com.smartstudy.xxd.ui.dialog.ExamDateDialog;
import com.smartstudy.xxd.ui.dialog.HasOptExamDialog;
import com.smartstudy.xxd.ui.dialog.OptExamDialog;
import com.smartstudy.xxd.utils.AppContants;

import java.util.List;

/**
 * @author luoyongming
 * @date on 2018/5/24
 * @describe 考试时间查询
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ExamDateActivity extends BaseActivity implements ExamDateContract.View {

    private FloatingActionMenu menu;
    private RelativeLayout topRect;
    private LinearLayout llCalendar;
    private ImageView ivBg;
    private ImageView ivExamRestore;
    private View emptyView;
    private RelativeLayout rlUpdate;

    private ExamDateContract.Presenter presenter;
    private List<ExamDateInfo> dateList;
    private boolean plain;
    private String exam;
    private TextView tvMyPlan;
    private TextView tvUpdate;
    private WeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_date);
    }

    @Override
    protected void initViewAndData() {
        this.topRect = findViewById(R.id.top_test_date);
        ivExamRestore = findViewById(R.id.iv_exam_restore);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("出国考试时间查询");
        llCalendar = findViewById(R.id.ll_calendar);
        tvMyPlan = findViewById(R.id.tv_my_plan);
        rlUpdate = findViewById(R.id.rl_update);
        tvUpdate = findViewById(R.id.tv_update);
        initTitleBar();
        menu = findViewById(R.id.menu_choose);
        menu.setIconAnimated(false);
        new ExamDatePresenter(this);
        exam = menu.getFabText();
        emptyView = findViewById(R.id.view_empty);
        plain = getIntent().getBooleanExtra("plain", true);
        presenter.showLoading(this, emptyView);
        presenter.getExamDate(ParameterUtils.NETWORK_ELSE_CACHED, plain, exam);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight();
            topRect.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            topRect.setPadding(topRect.getPaddingLeft(), statusBarHeight, topRect.getPaddingRight(), 0);
        }
    }

    @Override
    public void initEvent() {
        ivExamRestore.setOnClickListener(this);
        ivBg = findViewById(R.id.iv_bg);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.iv_close_update).setOnClickListener(this);
        tvMyPlan.setOnClickListener(this);
        menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.isOpened()) {
                    closeMenu();
                } else {
                    // 全屏高斯模糊
                    final Bitmap bmp = ScreenUtils.snapShotWithStatusBar(ExamDateActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap blurBmp = FastBlur.ScreenBlur(bmp, 6f);
                            Message msg = Message.obtain();
                            msg.obj = blurBmp;
                            msg.what = ParameterUtils.MSG_WHAT_REFRESH;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                    menu.setFabText("");
                    menu.setFabIcon(getResources().getDrawable(R.drawable.ic_fab_add));
                    menu.open(true);
                    ivExamRestore.setVisibility(View.GONE);
                }
            }
        });
        handleBlur();
        // menu显现时消费touch事件
        ivBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        fabClickInit(R.id.fab_gre);
        fabClickInit(R.id.fab_gmat);
        fabClickInit(R.id.fab_sat);
        fabClickInit(R.id.fab_islet);
        fabClickInit(R.id.fab_tolft);
        fabClickInit(R.id.fab_my);
    }

    private void handleBlur() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        BitmapDrawable drawable = new BitmapDrawable(getResources(), (Bitmap) msg.obj);
                        ivBg.setImageDrawable(drawable);
                        ivBg.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void fabClickInit(int fabId) {
        final FloatingActionButton fab = findViewById(fabId);
        if (fabId == R.id.fab_my) {
            fab.setVisibility(!plain ? View.VISIBLE : View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exam = fab.getText();
                closeMenu();
                // 筛选数据
                presenter.getExamDate(ParameterUtils.CACHED_ELSE_NETWORK, plain, exam);
            }
        });

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_my_plan:
                startActivity(new Intent(this, MyRemarksPlanActivity.class));
                overridePendingTransition(R.anim.remark_open, 0);
                break;
            case R.id.iv_exam_restore:
                // 筛选数据
                exam = CirclePointMonthView.DEFAULT_FILTER;
                presenter.getExamDate(ParameterUtils.CACHED_ELSE_NETWORK, plain, exam);
                ivExamRestore.setVisibility(View.GONE);
                menu.setFabText(exam);
                break;
            case R.id.iv_close_update:
                String userId = (String) SPCacheUtils.get("user_id", "");
                SPCacheUtils.put("close_exam_update_" + userId, true);
                rlUpdate.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (menu != null && menu.isOpened()) {
            closeMenu();
            return;
        }
        finish();
    }

    private void closeMenu() {
        menu.setFabText(exam);
        menu.setFabIcon(null);
        menu.close(true);
        ivBg.setVisibility(View.GONE);
        if (!CirclePointMonthView.DEFAULT_FILTER.equals(exam)) {
            ivExamRestore.setVisibility(View.VISIBLE);
        } else {
            ivExamRestore.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(ExamDateContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void getDateSuccess(String updateTime, String count, List<ExamDateInfo> datas) {
        // 移除视图里面的内容
        llCalendar.removeAllViewsInLayout();
        findViewById(R.id.ll_week).setVisibility(View.VISIBLE);
        tvMyPlan.setText(String.format(getString(R.string.exam_plan_count), count));
        tvMyPlan.setVisibility(!plain ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(View.GONE);
        llCalendar.setVisibility(View.VISIBLE);
        String examUpdateTime = (String) SPCacheUtils.get("time_exam_update", "");
        if (!examUpdateTime.equals(updateTime)) {
            SPCacheUtils.put("time_exam_update", updateTime);
            findViewById(R.id.rl_update).setVisibility(View.VISIBLE);
        } else {
            String userId = (String) SPCacheUtils.get("user_id", "");
            boolean exam_update = (boolean) SPCacheUtils.get("close_exam_update_" + userId, false);
            findViewById(R.id.rl_update).setVisibility(exam_update ? View.GONE : View.VISIBLE);
        }
        tvUpdate.setText(String.format(getString(R.string.time_exam_update), updateTime));
        menu.setVisibility(View.VISIBLE);
        this.dateList = datas;
    }

    @Override
    public void initDates(List<CalendarInfo> selectDatas, int currYear, int currMonth,
                          boolean needLine, String exam) {
        CirclePointMonthView monthView = new CirclePointMonthView(this, null, currYear,
            currMonth, exam);
        if (!plain) {
            monthView.setDateClick(new MonthView.IDateClick() {
                @Override
                public void onClickOnDate(CalendarInfo info, String dateStr, List<String> history) {
                    String title = String.format(getString(R.string.the_exam), info.des);
                    String countStr = String.format(getString(R.string.exam_count), info.selectCount);
                    if (info.select) {
                        // 已经参加考试，可取消
                        String myTitle = String.format(getString(R.string.my_exam_plan), info.des);
                        hasJoin(info.id, info.des, myTitle, dateStr, countStr);
                    } else {
                        if ("多种考试".equals(info.des)) {
                            List<ExamDateInfo.ItemsEntity> items = JSON.parseArray(info.selectCount,
                                ExamDateInfo.ItemsEntity.class);
                            chooseExam(items, dateStr);
                        } else {
                            chooseDate(info.id, info.des, title, dateStr, countStr);
                        }
                    }
                }
            });
        }
        monthView.setCalendarInfos(selectDatas);
        currYear = monthView.getCurrYear();
        currMonth = monthView.getCurrMonth();
        TextView tvTitle = new TextView(this);
        tvTitle.setPadding(0, DensityUtils.dip2px(24f), 0, DensityUtils.dip2px(12f));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
        tvTitle.setText(String.format(getString(R.string.year_month), currYear + "",
            (currMonth + 1) + ""));
        tvTitle.setTextColor(getResources().getColor(R.color.app_main_color));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setLayoutParams(titleParams);
        llCalendar.addView(tvTitle, -1);
        llCalendar.addView(monthView, -1);
        if (needLine) {
            View line = new View(this);
            line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(0.5f));
            int margin = DensityUtils.dip2px(16f);
            lineParams.leftMargin = margin;
            lineParams.rightMargin = margin;
            lineParams.topMargin = DensityUtils.dip2px(8f);
            line.setLayoutParams(lineParams);
            llCalendar.addView(line, -1);
        }
    }

    @Override
    public void joinExamSuccess() {
        //重新获取数据
        presenter.getExamDate(ParameterUtils.NETWORK_ELSE_CACHED, plain, exam);
    }

    @Override
    public void delExamSuccess() {
        //重新获取数据
        presenter.getExamDate(ParameterUtils.NETWORK_ELSE_CACHED, plain, exam);
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getExamDate(ParameterUtils.NETWORK_ELSE_CACHED, plain, exam);
    }

    private void chooseDate(final int id, String des, String title, String chooseDate, String countStr) {
        List<String> joinDateStr = presenter.getJoinDates(dateList, des);
        new ExamDateDialog(this, joinDateStr, title, chooseDate, countStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.joinExam(id);
            }
        });
    }

    private void chooseExam(List<ExamDateInfo.ItemsEntity> items, final String chooseDate) {
        new OptExamDialog(this, items, new ExamDialogClickListener() {
            @Override
            public void onClick(ExamDateInfo.ItemsEntity info) {
                String title = String.format(getString(R.string.the_exam), info.getExam());
                String countStr = String.format(getString(R.string.exam_count), info.getSelectCount());
                chooseDate(info.getId(), info.getExam(), title, chooseDate, countStr);
            }
        }).show();
    }

    private void hasJoin(final int id, final String des, final String title, final String dateStr, String countStr) {
        new HasOptExamDialog(this, title, dateStr, countStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AppContants.DIALOG_WHICH_DEL_EXAM:
                        // 取消本次考试计划
                        presenter.delExam(id);
                        break;
                    case AppContants.DIALOG_WHICH_EXAM_OTHER:
                        // 查看哪些人在备考
                        startActivity(new Intent(ExamDateActivity.this, RemarkListActivity.class)
                            .putExtra("remarkId", id + "")
                            .putExtra("title", des)
                            .putExtra("dateStr", dateStr.substring(5, dateStr.length() - 3)));
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

}
