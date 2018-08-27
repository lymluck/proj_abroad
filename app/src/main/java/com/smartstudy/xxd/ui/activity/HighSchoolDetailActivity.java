package com.smartstudy.xxd.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.ExpandableTextView;
import com.smartstudy.commonlib.ui.customview.FullyLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.PullParallaxScrollView;
import com.smartstudy.commonlib.ui.customview.StartSnapHelper;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.HighSchoolContract;
import com.smartstudy.xxd.mvp.presenter.HighSchoolPresenter;
import com.smartstudy.xxd.ui.adapter.HighSchoolPicAdapter;
import com.smartstudy.xxd.ui.adapter.HighSchoolStrItemsAdapter;
import com.smartstudy.xxd.ui.adapter.HighSchoolValueItemsAdapter;
import com.smartstudy.xxd.utils.AppContants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smartstudy.commonlib.utils.ParameterUtils.MEIQIA_UNREAD;
import static com.smartstudy.xxd.utils.AppContants.ROUTE_HIGHTSCHOOL_DETAIL;
import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 高中院校详情页
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
@Route(ROUTE_HIGHTSCHOOL_DETAIL)
public class HighSchoolDetailActivity extends BaseActivity implements HighSchoolContract.View {

    private RelativeLayout topSchooldetail;
    private View viewBg;
    private PullParallaxScrollView ppsvHighschool;
    private TextView tvTitle;
    private ImageView ivTopLeft;
    private View topLine;
    private LinearLayout llContent;
    private TextView tvShare;
    private LinearLayout llLocalRank;

    private int topAlpha = 0;
    private boolean isWhite = true;
    private String schoolId;
    private HighSchoolContract.Presenter presenter;
    private boolean isCollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_school_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        topSchooldetail.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
    }

    @Override
    protected void initViewAndData() {
        this.llContent = findViewById(R.id.llyt_content);
        this.topSchooldetail = findViewById(R.id.top_schooldetail);
        this.viewBg = findViewById(R.id.view_bg);
        this.ppsvHighschool = findViewById(R.id.ppsv_highschool);
        this.ppsvHighschool.setHeader(findViewById(R.id.iv_bg));
        this.ivTopLeft = (ImageView) findViewById(R.id.topdefault_leftbutton);
        this.ivTopLeft.setImageResource(R.drawable.ic_go_back_white);
        this.tvTitle = (TextView) findViewById(R.id.topdefault_centertitle);
        this.tvTitle.setVisibility(View.GONE);
        this.topLine = findViewById(R.id.top_line);
        this.tvShare = findViewById(R.id.tv_share);
        initTitleBar();
        schoolId = getIntent().getExtras().getString("id");
        new HighSchoolPresenter(this);
        presenter.isCollected(schoolId);
        presenter.getSchoolDetail(schoolId);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        llLocalRank = findViewById(R.id.llyt_local_rank);
        LinearLayout llyt_school_info = findViewById(R.id.llyt_school_info);
        RelativeLayout.LayoutParams topRankparams = (RelativeLayout.LayoutParams) llLocalRank.getLayoutParams();
        RelativeLayout.LayoutParams topInfoparams = (RelativeLayout.LayoutParams) llyt_school_info.getLayoutParams();
        int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = ScreenUtils.getStatusHeight();
            int height = topHeight + statusBarHeight;
            topSchooldetail.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            viewBg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            topSchooldetail.setPadding(topSchooldetail.getPaddingLeft(), statusBarHeight,
                topSchooldetail.getPaddingRight(), 0);
            topRankparams.topMargin = height;
            topInfoparams.topMargin = height;
        } else {
            topRankparams.topMargin = topHeight;
            topInfoparams.topMargin = topHeight;
        }
        llLocalRank.setLayoutParams(topRankparams);
        llyt_school_info.setLayoutParams(topInfoparams);
    }

    @Override
    public void initEvent() {
        // 标题栏滑出背景图片过程中颜色渐变
        ppsvHighschool.setScrollViewListener(new PullParallaxScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(PullParallaxScrollView scrollView, int x, int y, int oldx, int oldy) {
                int diff = ppsvHighschool.getHeaderVisibleHeight() - topSchooldetail.getHeight();
                if (y > 0 && y <= diff) {
                    topAlpha = y * 255 / diff;
                    tvTitle.setVisibility(View.GONE);
                    topLine.setVisibility(View.GONE);
                } else if (y > diff) {
                    topAlpha = 255;
                    tvTitle.setVisibility(View.VISIBLE);
                    topLine.setVisibility(View.VISIBLE);
                } else {
                    topAlpha = 0;
                    tvTitle.setVisibility(View.GONE);
                    topLine.setVisibility(View.GONE);
                }
                //翻转动画
                if (topAlpha > 128) {
                    // alpha > 0.5设置绿色图标
                    if (isWhite) {
                        ivTopLeft.setImageResource(R.drawable.ic_go_back);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(ivTopLeft,
                            "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = false;
                } else {
                    // 否则设置白色
                    if (!isWhite) {
                        ivTopLeft.setImageResource(R.drawable.ic_go_back_white);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(ivTopLeft,
                            "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = true;
                }
                topSchooldetail.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
            }
        });
        ivTopLeft.setOnClickListener(this);
        findViewById(R.id.llyt_ask).setOnClickListener(this);
        tvShare.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.llyt_ask:
                SPCacheUtils.put(MEIQIA_UNREAD, 0);
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put(AppContants.NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                clientInfo.put(AppContants.TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                Intent intent = new MQIntentBuilder(this)
                    .setClientInfo(clientInfo)
                    .build();
                clientInfo.clear();
                clientInfo = null;
                startActivity(intent);
                break;
            case R.id.tv_share:
                if (isCollected) {
                    presenter.disCollect(schoolId);
                } else {
                    presenter.collect(schoolId);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void setPresenter(HighSchoolContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void isCollected(boolean isCollected) {
        this.isCollected = isCollected;
        if (isCollected) {
            tvShare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collected, 0, 0);
        } else {
            tvShare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        }
    }

    @Override
    public void collectSuccess() {
        this.isCollected = true;
        showTip(null, "收藏成功！");
        tvShare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collected, 0, 0);
    }

    @Override
    public void discollectSuccess() {
        this.isCollected = false;
        showTip(null, "取消收藏！");
        tvShare.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
    }

    @Override
    public void showTop(String name, String egName, String addr, String rank) {
        findViewById(R.id.llyt_bot_bar).setVisibility(View.VISIBLE);
        llLocalRank.setVisibility(View.VISIBLE);
        tvTitle.setText(name);
        ((TextView) findViewById(R.id.tv_schoolname)).setText(name);
        ((TextView) findViewById(R.id.tv_egname)).setText(egName);
        TextView tv_addr = findViewById(R.id.tv_addr);
        tv_addr.setText(addr);
        tv_addr.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_local_rank)).setText(TextUtils.isEmpty(rank) ? "暂无" : rank);
    }

    @Override
    public void showIntro(String intro) {
        View view = mInflater.inflate(R.layout.layout_highschool_info, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("学校简介");
        ExpandableTextView etvIntro = view.findViewById(R.id.etv_intro);
        etvIntro.setText(intro);
        llContent.addView(view, -1);
    }

    @Override
    public void showPhotos(List<String> photos) {
        View view = mInflater.inflate(R.layout.layout_highschool_list, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("学校图片" + "(" + photos.size() + "张)");
        RecyclerView rv_highschool = view.findViewById(R.id.rv_highschool);
        initPicAdapter(rv_highschool, photos);
        llContent.addView(view, -1);
    }

    private void initPicAdapter(RecyclerView rv, List<String> photos) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        layoutManager = null;
        rv.setNestedScrollingEnabled(false);
        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(rv);
        rv.setAdapter(new HighSchoolPicAdapter(this, R.layout.item_highschool_pics, photos));
    }

    @Override
    public void showSummary(List<IdNameInfo> items) {
        showValueView(items, "学校概况");
    }

    @Override
    public void showData(List<IdNameInfo> items) {
        showValueView(items, "学校数据");
    }

    @Override
    public void showFees(List<IdNameInfo> items) {
        showValueView(items, "费用预算");
    }

    @Override
    public void showApplications(List<IdNameInfo> items) {
        showValueView(items, "申请情况");
    }

    @Override
    public void showSummer(List<IdNameInfo> items) {
        showValueView(items, "暑期学校");
    }

    @Override
    public void showContact(List<IdNameInfo> items) {
        showValueView(items, "联系信息");
    }

    @Override
    public void showSports(List<String> items) {
        showStrView(items, "体育项目");
    }

    @Override
    public void showApCourses(List<String> items) {
        showStrView(items, "AP课程");
    }

    @Override
    public void showCommunities(List<String> items) {
        showStrView(items, "社团");
    }

    @Override
    public void showFeatures(List<String> items) {
        showStrView(items, "学校特色");
    }

    @Override
    public void showPros(List<String> items) {
        showStrView(items, "学校优势");
    }

    @Override
    public void showHonors(List<String> items) {
        showStrView(items, "学校荣誉");
    }

    @Override
    public void showFacilities(List<String> items) {
        showStrView(items, "学校设施");
    }

    @Override
    public void showEsl(String esl) {
        View view = mInflater.inflate(R.layout.layout_highschool_info, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("ESL课程");
        ExpandableTextView etvIntro = view.findViewById(R.id.etv_intro);
        etvIntro.setText(esl);
        llContent.addView(view, -1);
    }

    /**
     * 初始化recyclerView
     *
     * @param rv
     */
    private void initRecyclerView(RecyclerView rv) {
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f))
            .margin(DensityUtils.dip2px(16f), DensityUtils.dip2px(16f))
            .colorResId(R.color.horizontal_line_color).build());
        layoutManager = null;
        rv.setNestedScrollingEnabled(false);
    }

    /**
     * 左name右value列表展开更多动作处理
     *
     * @param items
     * @param rv
     * @param llyt_more
     */
    private void initValueMorAction(final List<IdNameInfo> items, RecyclerView rv, final LinearLayout llyt_more) {
        int len = items.size();
        final List<IdNameInfo> mDatas = new ArrayList<>();
        if (len > 5) {
            mDatas.addAll(items.subList(0, 5));
            llyt_more.setVisibility(View.VISIBLE);
        } else {
            mDatas.addAll(items);
            llyt_more.setVisibility(View.GONE);
        }
        final HighSchoolValueItemsAdapter mAdapter = new HighSchoolValueItemsAdapter(this,
            R.layout.item_highschool_value_items, mDatas);
        rv.setAdapter(mAdapter);
        llyt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                mDatas.addAll(items);
                mAdapter.notifyDataSetChanged();
                llyt_more.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 纯文本列表展开更多动作处理
     *
     * @param items
     * @param rv
     * @param llyt_more
     */
    private void initStrMorAction(final List<String> items, RecyclerView rv, final LinearLayout llyt_more) {
        int len = items.size();
        final List<String> mDatas = new ArrayList<>();
        if (len > 5) {
            mDatas.addAll(items.subList(0, 5));
            llyt_more.setVisibility(View.VISIBLE);
        } else {
            mDatas.addAll(items);
            llyt_more.setVisibility(View.GONE);
        }
        final HighSchoolStrItemsAdapter mAdapter = new HighSchoolStrItemsAdapter(this,
            R.layout.item_highschool_str_items, mDatas);
        rv.setAdapter(mAdapter);
        llyt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                mDatas.addAll(items);
                mAdapter.notifyDataSetChanged();
                llyt_more.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void hideLoading() {
        findViewById(R.id.llyt_loading).setVisibility(View.GONE);
    }

    /**
     * 左name右value样式列表展示
     *
     * @param items 列表数据集合
     * @param title 区块标题
     */
    private void showValueView(List<IdNameInfo> items, String title) {
        View view = mInflater.inflate(R.layout.layout_highschool_list, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setPadding(DensityUtils.dip2px(16f), 0, 0, DensityUtils.dip2px(8f));
        tvTitle.setText(title);
        RecyclerView rv_highschool = view.findViewById(R.id.rv_highschool);
        LinearLayout llyt_more = view.findViewById(R.id.llyt_more);
        initRecyclerView(rv_highschool);
        initValueMorAction(items, rv_highschool, llyt_more);
        llContent.addView(view, -1);
    }

    /**
     * 纯文本列表样式展示
     *
     * @param items 列表数据集合
     * @param title 区块标题
     */
    private void showStrView(List<String> items, String title) {
        View view = mInflater.inflate(R.layout.layout_highschool_list, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setPadding(DensityUtils.dip2px(16f), 0, 0, DensityUtils.dip2px(8f));
        tvTitle.setText(title);
        RecyclerView rv_highschool = view.findViewById(R.id.rv_highschool);
        LinearLayout llyt_more = view.findViewById(R.id.llyt_more);
        initRecyclerView(rv_highschool);
        initStrMorAction(items, rv_highschool, llyt_more);
        llContent.addView(view, -1);
    }
}
