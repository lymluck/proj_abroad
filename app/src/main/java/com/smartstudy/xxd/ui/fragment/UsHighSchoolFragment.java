package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ColumnListItemInfo;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HotRankInfo;
import com.smartstudy.xxd.entity.RecommendedCourse;
import com.smartstudy.xxd.mvp.contract.UsHighSchoolContract;
import com.smartstudy.xxd.mvp.presenter.UsHighSchoolPresenter;
import com.smartstudy.xxd.ui.activity.ActivityCenterActivity;
import com.smartstudy.xxd.ui.activity.ColumnActivity;
import com.smartstudy.xxd.ui.activity.ColumnListActivity;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;
import com.smartstudy.xxd.ui.activity.ExamDateActivity;
import com.smartstudy.xxd.ui.activity.FindMajorActivity;
import com.smartstudy.xxd.ui.activity.HighSchoolLibraryActivity;
import com.smartstudy.xxd.ui.activity.HighSchoolRankActivity;
import com.smartstudy.xxd.ui.activity.HighSchoolRankDetailActivity;
import com.smartstudy.xxd.ui.activity.HomeQaActivity;
import com.smartstudy.xxd.ui.activity.HotRankTypeActivity;
import com.smartstudy.xxd.ui.activity.MainActivity;
import com.smartstudy.xxd.ui.activity.SrtChooseSpecialActivity;
import com.smartstudy.xxd.ui.activity.ThematicCenterActivity;
import com.smartstudy.xxd.ui.adapter.homeschool.HomeHotAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.CATEGORY_ID;
import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 高中院校详情页
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class UsHighSchoolFragment extends BaseFragment implements UsHighSchoolContract.View {

    private RecyclerView rvHots;
    private HomeHotAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderFooter;
    private View headView;
    private View footView;
    private List<HomeHotListInfo> hotListInfos;
    private List<HomeHotInfo> hotSubList;
    private UsHighSchoolContract.Presenter presenter;
    private RecommendedCourse recommendedCourse;
    private ImageView ivReconmendCourse;
    private AppBasicDialog recommendedCourseDialog;
    private boolean isvisible;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_us_high_school;
    }

    @Override
    public void onDestroy() {
        releaseRes();
        super.onDestroy();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        presenter.getHomeHot();
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        isvisible = false;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (!isvisible) {
            isvisible = true;
            presenter.getHomeHot();
        }
    }

    private void releaseRes() {
        if (presenter != null) {
            presenter = null;
        }
        if (rvHots != null) {
            rvHots.removeAllViews();
            rvHots = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }

        if (hotListInfos != null) {
            hotListInfos.clear();
            hotListInfos = null;
        }
        if (hotSubList != null) {
            hotSubList.clear();
            hotSubList = null;
        }
        if (mHeaderFooter != null) {
            mHeaderFooter = null;
        }
        if (recommendedCourseDialog != null) {
            recommendedCourseDialog.dismiss();
            recommendedCourseDialog = null;
        }
    }

    @Override
    protected void initView() {
        this.rvHots = rootView.findViewById(R.id.rcv_hots);
        hotListInfos = new ArrayList<>();
        mAdapter = new HomeHotAdapter(mActivity, hotListInfos, mActivity.mInflater, UsHighSchoolFragment.class);
        mHeaderFooter = new HeaderAndFooterWrapper(mAdapter);
        final GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        //设置span
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isSectionHeaderPosition(position - mHeaderFooter.getHeadersCount())
                    || mAdapter.isSectionFooterPosition(position - mHeaderFooter.getHeadersCount())) {
                    return manager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rvHots.setHasFixedSize(true);
        rvHots.setLayoutManager(manager);
        rvHots.setNestedScrollingEnabled(false);
        rvHots.setAdapter(mHeaderFooter);
        initHeaderAndFooter();
        initEvent();
        new UsHighSchoolPresenter(this);
    }

    //设置header和footer
    private void initHeaderAndFooter() {
        headView = mActivity.getLayoutInflater().inflate(R.layout.layout_high_school_header,
            null, false);
        footView = mActivity.getLayoutInflater().inflate(R.layout.layout_high_school_footer,
            null, false);
        int screenWidth = ScreenUtils.getScreenWidth();
        int ivWidth = screenWidth - 2 * DensityUtils.dip2px(13f);
        int ivHeight = ivWidth * 12 / 25;

        ivReconmendCourse = headView.findViewById(R.id.iv_reconmend_course);
        ivReconmendCourse.setImageResource(R.drawable.ic_reconmend_course);
        FrameLayout.LayoutParams ivReconmendCourseParas = (FrameLayout.LayoutParams) ivReconmendCourse.getLayoutParams();
        ivReconmendCourseParas.width = ivWidth;
        ivReconmendCourseParas.height = ivHeight;
        ivReconmendCourse.setLayoutParams(ivReconmendCourseParas);
        mHeaderFooter.addHeaderView(headView);
        mHeaderFooter.addFootView(footView);
    }

    private void initEvent() {
        headView.findViewById(R.id.tv_home_school).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_rank).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_special).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_topic).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_srt_spe).setOnClickListener(this);
        headView.findViewById(R.id.tv_abroad_qa).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_activity).setOnClickListener(this);
        headView.findViewById(R.id.tv_see_more_rank).setOnClickListener(this);
        headView.findViewById(R.id.iv_course_cancel).setOnClickListener(this);
        footView.findViewById(R.id.tv_see_more_column).setOnClickListener(this);
        ivReconmendCourse.setOnClickListener(this);
        //刷新数据
        dataRefresh();
    }


    public void dataRefresh() {
        if (presenter != null) {
            presenter.getHomeHot();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_course_cancel:
                showNormalCourseDialog();
                break;

            case R.id.iv_reconmend_course:
                if (recommendedCourse != null) {
                    UApp.actionEvent(mActivity, "15_A_course_cell");
                    mActivity.startActivity(new Intent(mActivity, CourseDetailActivity.class)
                        .putExtra("id", recommendedCourse.getProductId())
                        .putExtra("courseCover", recommendedCourse.getCoverUrl()));
                }
                break;
            case R.id.tv_home_school:
                UApp.actionEvent(mActivity, "8_A_school_library_btn");
                startActivity(new Intent(mActivity, HighSchoolLibraryActivity.class));
                break;
            case R.id.tv_home_rank:
                UApp.actionEvent(mActivity, "8_A_rank_btn");
                startActivity(new Intent(mActivity, HighSchoolRankActivity.class));
                break;
            case R.id.cv_remark:
                startActivity(new Intent(mActivity, ExamDateActivity.class).putExtra("plain", false));
                break;
            case R.id.tv_home_activity:
                startActivity(new Intent(mActivity, ActivityCenterActivity.class));
                break;
            case R.id.tv_home_srt_spe:
                UApp.actionEvent(mActivity, "8_A_choose_professional_btn");
                startActivity(new Intent(mActivity, SrtChooseSpecialActivity.class));
                break;

            case R.id.tv_abroad_qa:
                //留学问答
                Bundle data = new Bundle();
                data.putBoolean("showBackBtn", true);
                data.putBoolean("showRed", ((MainActivity) mActivity).isQaRed());
                startActivity(new Intent(mActivity, HomeQaActivity.class).putExtras(data));
                break;
            case R.id.tv_home_special:
                startActivity(new Intent(mActivity, FindMajorActivity.class));
                break;
            case R.id.tv_see_more_rank:
                UApp.actionEvent(mActivity, "8_A_rank_more_btn");
                startActivity(new Intent(mActivity, HighSchoolRankActivity.class));
                break;
            case R.id.tv_home_topic:
                UApp.actionEvent(mActivity, "8_A_subject_btn");
                startActivity(new Intent(mActivity, ThematicCenterActivity.class));
                break;
            case R.id.tv_see_more_column:
                startActivity(new Intent(mActivity, ColumnListActivity.class));
                break;
            default:
                break;
        }
    }

    private void goSchoolRank(CharSequence title, Object obj) {
        if (obj != null) {
            Intent touk = new Intent(mActivity, HighSchoolRankDetailActivity.class);
            touk.putExtra(TITLE, title);
            touk.putExtra(CATEGORY_ID, obj.toString());
            startActivity(touk);
            obj = null;
        }
    }

    @Override
    public void setPresenter(UsHighSchoolContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void showHotRank(List<HotRankInfo> homeHotRankInfos) {
        if (mActivity != null) {
            int len = homeHotRankInfos.size();
            RelativeLayout rlHomeRankHot = headView.findViewById(R.id.rl_home_rank_hot);
            rlHomeRankHot.removeAllViewsInLayout();
            if (len > 0) {
                headView.findViewById(R.id.llyt_rank_more).setVisibility(View.VISIBLE);
                rlHomeRankHot.setVisibility(View.VISIBLE);
            }
            // 定制样式
            int viewId = 0;
            for (int i = 0; i < len; i++) {
                HotRankInfo info = homeHotRankInfos.get(i);
                CardView cv = getRankItem(info, i, len);
                RelativeLayout.LayoutParams cvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
                if (len > 3) {
                    // 3个以上的条目需要分左右
                    View baseLine = new View(mActivity);
                    baseLine.setId(R.id.line_base);
                    RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(DensityUtils.dip2px(4f),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                    lineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    baseLine.setLayoutParams(lineParams);
                    rlHomeRankHot.addView(baseLine);
                    if (len == 4) {
                        set4ItemParams(i, cvParams, viewId);
                    } else if (len == 5) {
                        set5ItemParams(i, cvParams, viewId);
                    }
                } else {
                    set3ItemParams(i, cvParams, viewId);
                }
                cv.setLayoutParams(cvParams);
                rlHomeRankHot.addView(cv);
                viewId = cv.getId();
            }
        }
    }

    /**
     * 3个条目时的样式
     *
     * @param i
     * @param cvParams
     */
    private void set3ItemParams(int i, RelativeLayout.LayoutParams cvParams, int viewId) {
        if (i > 0) {
            // 除了第一个条目，其它的需要topmargin
            cvParams.setMargins(DensityUtils.dip2px(16f), DensityUtils.dip2px(4f),
                DensityUtils.dip2px(16f), 0);
            cvParams.addRule(RelativeLayout.BELOW, viewId);
        } else {
            cvParams.setMargins(DensityUtils.dip2px(16f), 0,
                DensityUtils.dip2px(16f), 0);
        }
    }

    /**
     * 4个条目时的样式
     *
     * @param i
     * @param cvParams
     */
    private void set4ItemParams(int i, RelativeLayout.LayoutParams cvParams, int viewId) {
        if (i > 1) {
            if (i > 2) {
                cvParams.setMargins(0, DensityUtils.dip2px(4f)
                    , DensityUtils.dip2px(16f), 0);
                cvParams.addRule(RelativeLayout.BELOW, viewId);
            } else {
                cvParams.setMargins(0, 0, DensityUtils.dip2px(16f), 0);
            }
            cvParams.addRule(RelativeLayout.RIGHT_OF, R.id.line_base);
        } else {
            if (i > 0) {
                cvParams.setMargins(DensityUtils.dip2px(16f), DensityUtils.dip2px(4f)
                    , 0, 0);
                cvParams.addRule(RelativeLayout.BELOW, viewId);
            } else {
                cvParams.setMargins(DensityUtils.dip2px(16f), 0, 0, 0);
            }
            cvParams.addRule(RelativeLayout.LEFT_OF, R.id.line_base);
        }
    }

    /**
     * 5个条目时的样式
     *
     * @param i
     * @param cvParams
     */
    private void set5ItemParams(int i, RelativeLayout.LayoutParams cvParams, int viewId) {
        if (i > 2) {
            if (i > 3) {
                cvParams.setMargins(0, DensityUtils.dip2px(4f)
                    , DensityUtils.dip2px(16f), 0);
                cvParams.addRule(RelativeLayout.BELOW, viewId);
            } else {
                cvParams.setMargins(0, 0, DensityUtils.dip2px(16f), 0);
            }
            cvParams.addRule(RelativeLayout.RIGHT_OF, R.id.line_base);
        } else {
            if (i > 0) {
                cvParams.setMargins(DensityUtils.dip2px(16f), DensityUtils.dip2px(4f)
                    , 0, 0);
                cvParams.addRule(RelativeLayout.BELOW, viewId);
            } else {
                cvParams.setMargins(DensityUtils.dip2px(16f), 0, 0, 0);
            }
            cvParams.addRule(RelativeLayout.LEFT_OF, R.id.line_base);
        }
    }


    @Override
    public void showHomeHot(List<HomeHotListInfo> mDatas) {
        if (presenter != null) {
            hotListInfos.clear();
            hotListInfos.addAll(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideLoading() {
        rootView.findViewById(R.id.llyt_loading).setVisibility(View.GONE);
    }


    /**
     * 热门排名item
     *
     * @param info
     * @param i
     * @param len
     * @return
     */
    private CardView getRankItem(HotRankInfo info, int i, int len) {
        CardView cv = new CardView(mActivity);
        // 热门排名点击事件
        rankItemClick(cv, info);
        //设置Id
        setViewId(cv, i);
        cv.setRadius(DensityUtils.dip2px(4f));
        cv.setCardElevation(0f);
        cv.setMaxCardElevation(2f);
        cv.setUseCompatPadding(false);
        ImageView iv = new ImageView(mActivity);
        FrameLayout.LayoutParams ivParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        ivParams.height = DensityUtils.dip2px(70f);
        if (len > 3) {
            if (len == 4) {
                ivParams.height = DensityUtils.dip2px(107f);
            } else if (len == 5) {
                if (i > 2) {
                    ivParams.height = DensityUtils.dip2px(107f);
                }
            }
        }
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(ivParams);
        DisplayImageUtils.formatImgUrl(mActivity, info.getBackgroundImage(), iv);
        cv.addView(iv);
        TextView tv = new TextView(mActivity);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setGravity(Gravity.CENTER);
        tv.setLineSpacing(DensityUtils.dip2px(2f), 1f);
        tv.setMaxLines(2);
        if (mActivity != null && isAdded()) {
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setTextSize(DensityUtils.px2sp(getResources().getDimensionPixelSize(R.dimen.app_text_size4)));
        }
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER;
        tvParams.leftMargin = DensityUtils.dip2px(12f);
        tvParams.rightMargin = DensityUtils.dip2px(12f);
        tv.setLayoutParams(tvParams);
        tv.setText((TextUtils.isEmpty(info.getYear()) ? "" : info.getYear())
            + (TextUtils.isEmpty(info.getType()) ? "" : info.getType())
            + (TextUtils.isEmpty(info.getTitle()) ? "" : info.getTitle()));
        cv.addView(tv);
        return cv;
    }

    /**
     * 热门排名点击事件
     *
     * @param cv
     * @param info
     */
    private void rankItemClick(CardView cv, final HotRankInfo info) {
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UApp.actionEvent(mActivity, "8_A_specific_rank_btn");
                if (!TextUtils.isEmpty(info.getId())) {
                    goSchoolRank(info.getTitle(), info.getId());
                } else {
                    if (info.getRankings() != null) {
                        startActivity(new Intent(mActivity, HotRankTypeActivity.class)
                            .putParcelableArrayListExtra("data", info.getRankings())
                            .putExtra(TITLE, info.getTitle()));
                    }
                }
            }
        });
    }

    /**
     * 设置Id
     *
     * @param view
     * @param i
     * @return
     */
    private void setViewId(View view, int i) {
        if (i == 0) {
            view.setId(R.id.item_hot_rank_1);
        } else if (i == 1) {
            view.setId(R.id.item_hot_rank_2);
        } else if (i == 2) {
            view.setId(R.id.item_hot_rank_3);
        } else if (i == 3) {
            view.setId(R.id.item_hot_rank_4);
        } else if (i == 4) {
            view.setId(R.id.item_hot_rank_5);
        }
    }


    @Override
    public void showRecommendedCourse(RecommendedCourse recommendedCourse) {
        this.recommendedCourse = recommendedCourse;
        if (recommendedCourse != null) {
            headView.findViewById(R.id.ll_recommend).setVisibility(View.VISIBLE);
            headView.findViewById(R.id.fl_reconmend_cours).setVisibility(View.VISIBLE);
            ((TextView) headView.findViewById(R.id.tv_tips)).setText(recommendedCourse.getName());
        } else {
            headView.findViewById(R.id.fl_reconmend_cours).setVisibility(View.GONE);
            if (headView.findViewById(R.id.fl_intentionally).getVisibility() == View.GONE) {
                headView.findViewById(R.id.ll_recommend).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showColumnNews(List<ColumnListItemInfo> infos) {
        int len = infos.size();
        if (len > 0) {
            int lineH = DensityUtils.dip2px(0.5f);
            LinearLayout llHomeColumn = footView.findViewById(R.id.ll_home_column);
            llHomeColumn.removeAllViewsInLayout();
            footView.findViewById(R.id.line_column).setVisibility(View.VISIBLE);
            footView.findViewById(R.id.ll_home_column_more).setVisibility(View.VISIBLE);
            footView.findViewById(R.id.ll_home_column).setVisibility(View.VISIBLE);
            ColumnListItemInfo info = null;
            for (int i = 0; i < len; i++) {
                View view = mActivity.mInflater.inflate(R.layout.item_column_list, null);
                info = infos.get(i);
                DisplayImageUtils.formatImgUrl(mActivity, info.getCoverUrl(), (ImageView) view.findViewById(R.id.iv_cover));
                DisplayImageUtils.formatPersonImgUrl(mActivity, info.getAuthor().getAvatar(), (ImageView) view.findViewById(R.id.iv_user_avatar));
                ((TextView) view.findViewById(R.id.tv_title)).setText(info.getTitle());
                ((TextView) view.findViewById(R.id.tv_user_name)).setText(info.getAuthor().getName());
                ((TextView) view.findViewById(R.id.tv_see)).setText(info.getVisitCount() + "");
                ((TextView) view.findViewById(R.id.tv_like)).setText(info.getLikesCount() + "");
                llHomeColumn.addView(view, -1);
                if (i != len - 1) {
                    View line = new View(mActivity);
                    line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
                    LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        lineH);
                    line.setLayoutParams(lineParam);
                    llHomeColumn.addView(line, -1);
                }
                clickColumnItem(view.findViewById(R.id.ll_top), info);
                info = null;
            }
        }
    }

    private void clickColumnItem(View item, final ColumnListItemInfo info) {
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info != null) {
                    startActivity(new Intent(mActivity, ColumnActivity.class)
                        .putExtra("id", info.getId() + ""));
                }
            }
        });
    }

    private void showNormalCourseDialog() {
        recommendedCourseDialog = DialogCreator.createAppBasicDialog(mActivity, "",
            "要跳过当前课程吗?", "跳过", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.positive_btn:
                            presenter.deleCourse(recommendedCourse.getProductId());
                            recommendedCourseDialog.dismiss();
                            break;
                        case R.id.negative_btn:
                            recommendedCourseDialog.dismiss();
                            break;
                        default:
                            break;

                    }

                }
            });
        recommendedCourseDialog.show();
    }

}
