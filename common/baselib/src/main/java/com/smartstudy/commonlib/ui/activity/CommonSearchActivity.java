package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.CourseInfo;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.mvp.contract.CommonSearchContract;
import com.smartstudy.commonlib.mvp.presenter.CommonSearchPresenter;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.EditTextWithClear;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;

import java.util.ArrayList;
import java.util.List;

public class CommonSearchActivity extends UIActivity implements CommonSearchContract.View {
    private EditTextWithClear searchView;
    private LinearLayout llyt_top_search;
    private LoadMoreRecyclerView rclv_search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<SchoolInfo> mSchoolAdapter;
    private CommonAdapter<SchooolRankInfo> mRankAdapter;
    private CommonAdapter<NewsInfo> mNewsAdapter;
    private CommonAdapter<QuestionInfo> mQuesAdapter;
    private CommonAdapter<IdNameInfo> mIdNameAdapter;
    private CommonAdapter<CourseInfo> mCourseAdapter;
    private CommonAdapter<RankTypeInfo.RankingsEntity> mRankTypeAdapter;
    private LoadMoreWrapper loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private LinearLayout llyt_choose;

    private WeakHandler mHandler;
    private int mPage = 1;
    private String flag_value;
    private CommonSearchContract.Presenter searchP;
    private List<SchoolInfo> schoolsInfoList;
    private ArrayList<SchooolRankInfo> schoolsRankList;
    private List<QuestionInfo> questionInfoList;
    private List<NewsInfo> newsInfoList;
    private List<IdNameInfo> idNameInfoList;
    private List<CourseInfo> courseInfoList;
    private List<RankTypeInfo.RankingsEntity> rankingsEntities;
    private String keyword;
    private static int spaceTime = 300;//时间间隔
    private static long lastSearchTime = 0;//上次搜索的时间
    private String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusColor(R.color.white);//修改状态栏颜色
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
        removeActivity(CommonSearchActivity.class.getSimpleName());
    }

    private void releaseRes() {
        if (rclv_search != null) {
            rclv_search.removeAllViews();
            rclv_search = null;
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.removeAllViews();
            swipeRefreshLayout = null;
        }
        if (mSchoolAdapter != null) {
            mSchoolAdapter.destroy();
            mSchoolAdapter = null;
        }
        if (mRankAdapter != null) {
            mRankAdapter.destroy();
            mRankAdapter = null;
        }
        if (mNewsAdapter != null) {
            mNewsAdapter.destroy();
            mNewsAdapter = null;
        }
        if (mQuesAdapter != null) {
            mQuesAdapter.destroy();
            mQuesAdapter = null;
        }
        if (mIdNameAdapter != null) {
            mIdNameAdapter.destroy();
            mIdNameAdapter = null;
        }
        if (mCourseAdapter != null) {
            mCourseAdapter.destroy();
            mCourseAdapter = null;
        }
        if (mRankTypeAdapter != null) {
            mRankTypeAdapter.destroy();
            mRankTypeAdapter = null;
        }
        clearList();
        schoolsInfoList = null;
        questionInfoList = null;
        schoolsRankList = null;
        newsInfoList = null;
        idNameInfoList = null;
        courseInfoList = null;
        rankingsEntities = null;
        if (searchP != null) {
            searchP = null;
        }
    }

    @Override
    protected void initViewAndData() {
        llyt_choose = (LinearLayout) findViewById(R.id.llyt_choose);
        llyt_top_search = (LinearLayout) findViewById(R.id.llyt_top_search);
        Intent data = getIntent();
        flag_value = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        keyword = data.getStringExtra("keyword");
        typeName = data.getStringExtra("typeName");
        //初始化View
        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value) || ParameterUtils.HOME_RATE_FLAG.equals(flag_value)) {
            addActivity(this);
            llyt_top_search.setBackgroundResource(R.color.main_bg);
        }
        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
            llyt_choose.setVisibility(View.VISIBLE);
        }
        rclv_search = (LoadMoreRecyclerView) findViewById(R.id.rclv_search);
        rclv_search.setHasFixedSize(true);
        //这里做layoutmanager匹配
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_search.setLayoutManager(mLayoutManager);
        initItemDecoration();
        initAdapter();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_search);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        searchView = (EditTextWithClear) findViewById(R.id.searchView);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(this);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        llyt_top_search.setBackgroundResource(0);
                        break;
                }
                return false;
            }
        });
        searchView.getMyEditText().setHint(R.string.search);
        if (data.hasExtra("searchHint")) {
            searchView.getMyEditText().setHint(data.getStringExtra("searchHint"));
        }
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        new CommonSearchPresenter(this);
        if (!TextUtils.isEmpty(keyword)) {
            searchView.getMyEditText().setText(keyword);
            pullRefresh(ParameterUtils.CACHED_ELSE_NETWORK, ParameterUtils.PULL_DOWN);
        } else {
            searchView.getMyEditText().requestFocus();
            KeyBoardUtils.openKeybord(searchView.getMyEditText(), this);
        }
    }

    //item分割线匹配
    private void initItemDecoration() {
        if (ParameterUtils.QA_FLAG.equals(flag_value)) {
            rclv_search.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                    .size(DensityUtils.dip2px(10f)).colorResId(R.color.search_bg).build());
        } else if (!ParameterUtils.COURSE_FLAG.equals(flag_value)) {
            rclv_search.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                    .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        }
    }

    @Override
    public void initEvent() {
        initRefresh();
        initTextWatcher();
        initEditor();
        initLoadMore();
        findViewById(R.id.llyt_school).setOnClickListener(this);
        findViewById(R.id.llyt_rank).setOnClickListener(this);
        findViewById(R.id.llyt_smart).setOnClickListener(this);
    }

    private void initRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                //开始搜索
                //这里做刷新匹配
                pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
            }
        });
    }

    private void pullRefresh(int cacheMode, int pullTo) {
        if (ParameterUtils.RANKS_FLAG.equals(flag_value)) {
            searchP.getRanks(cacheMode, getIntent().getStringExtra("categoryId"), keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
            searchP.getRanks(cacheMode, "607", keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.NEWS_FLAG.equals(flag_value)) {
            searchP.getNews(cacheMode, keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.SPEDATA_FLAG.equals(flag_value)) {
            searchP.getSpecialData(cacheMode, keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.PROGRAMSPE_FLAG.equals(flag_value)) {
            searchP.getProgram(cacheMode, getIntent().getStringExtra("typeId"), keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.RANKTYPE_FLAG.equals(flag_value)) {
            searchP.getRankType(cacheMode, keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.QA_FLAG.equals(flag_value) || ParameterUtils.HOME_QA_FLAG.equals(flag_value)) {
            searchP.getQa(cacheMode, keyword, mPage, pullTo, flag_value);
        } else if (ParameterUtils.COURSE_FLAG.equals(flag_value)) {
            searchP.getCourses(cacheMode, keyword, mPage, pullTo, flag_value);
        } else {
            //默认搜索学校
            searchP.getSchools(cacheMode, getIntent().getStringExtra("countryId"), keyword, mPage, pullTo, flag_value);
        }
    }

    private void initLoadMore() {
        rclv_search.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (rclv_search != null) {
                        rclv_search.loadComplete(true);
                    }
                    return;
                }
                mPage = mPage + 1;
                //这里做上拉匹配
                pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_UP);
            }
        });
    }

    private void initEditor() {
        searchView.getMyEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeybord(searchView.getMyEditText(), CommonSearchActivity.this);
                    //这里做搜索匹配
                    pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
                }
                return true;
            }
        });
    }

    private void initTextWatcher() {
        searchView.getMyEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    keyword = s.toString();
                    //开始搜索
                    //这里做搜索匹配
                    //延迟搜索，防止用户在输入过程中频繁调用搜索接口
                    long currentTime = System.currentTimeMillis();//当前系统时间
                    if (currentTime - lastSearchTime > spaceTime) {
                        pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
                    }
                    lastSearchTime = currentTime;
                } else {
                    //这里做清空操作
                    clearList();
                    rclv_search.loadComplete(true);
                    loadMoreWrapper.notifyDataSetChanged();
                    Utils.convertActivityToTranslucent(CommonSearchActivity.this);
                    if (!ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                        showEmptyView(null);
                        mHandler.sendEmptyMessageDelayed(1, 250);
                    } else {
                        llyt_choose.setVisibility(View.VISIBLE);
                        llyt_top_search.setBackgroundResource(R.color.main_bg);
                    }
                }
            }
        });
    }

    private void clearList() {
        keyword = null;
        if (schoolsInfoList != null) {
            schoolsInfoList.clear();
        }
        if (schoolsRankList != null) {
            schoolsRankList.clear();
        }
        if (newsInfoList != null) {
            newsInfoList.clear();
        }
        if (rankingsEntities != null) {
            rankingsEntities.clear();
        }
        if (idNameInfoList != null) {
            idNameInfoList.clear();
        }
        if (questionInfoList != null) {
            questionInfoList.clear();
        }
        if (courseInfoList != null) {
            courseInfoList.clear();
        }
    }

    @Override
    protected void doClick(View v) {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        int id = v.getId();
        if (id == R.id.tv_cancle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        } else if (id == R.id.llyt_school) {
            Bundle bundle = new Bundle();
            bundle.putString("flag", ParameterUtils.SCHOOLS_FLAG);
            bundle.putString("countryId", "");
            bundle.putString("title", "全球");
            Router.build("SchoolListActivity").with(bundle).go(CommonSearchActivity.this);
        } else if (id == R.id.llyt_rank) {
            Bundle bundle = new Bundle();
            bundle.putString("flag", ParameterUtils.RANKTYPE_FLAG);
            Router.build("RankTypeActivity").with(bundle).go(CommonSearchActivity.this);
        } else if (id == R.id.llyt_smart) {
            removeActivity(CommonSearchActivity.class.getSimpleName());
            Router.build("SrtChooseSchoolActivity").go(CommonSearchActivity.this);
        }
    }

    private void initAdapter() {
        //这里做列表adapter匹配
        if (ParameterUtils.RANKS_FLAG.equals(flag_value) || ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
            initRankAdapter();
        } else if (ParameterUtils.NEWS_FLAG.equals(flag_value)) {
            initNewsAdapter();
        } else if (ParameterUtils.SPEDATA_FLAG.equals(flag_value) || ParameterUtils.PROGRAMSPE_FLAG.equals(flag_value)) {
            initSpeAdapter();
        } else if (ParameterUtils.RANKTYPE_FLAG.equals(flag_value)) {
            initRankTypeAdapter();
        } else if (ParameterUtils.QA_FLAG.equals(flag_value) || ParameterUtils.HOME_QA_FLAG.equals(flag_value)) {
            initQuesAdapter();
        } else if (ParameterUtils.COURSE_FLAG.equals(flag_value)) {
            initCourseAdapter();
        } else {
            //默认适配搜索学校
            initSchoolAdapter();
        }
        rclv_search.setAdapter(loadMoreWrapper);
    }

    private void initSchoolAdapter() {
        schoolsInfoList = new ArrayList<>();
        mSchoolAdapter = new CommonAdapter<SchoolInfo>(this, R.layout.item_school_list, schoolsInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, SchoolInfo schoolInfo, int position) {
                if (schoolInfo != null) {
                    if (!TextUtils.isEmpty(typeName)) {
                        LinearLayout llyt_type = holder.getView(R.id.llyt_type_name);
                        if (position == 0) {
                            llyt_type.setVisibility(View.VISIBLE);
                            holder.setText(R.id.tv_type_name, typeName);
                        } else {
                            llyt_type.setVisibility(View.GONE);
                        }
                    }
                    if (holder.getView(R.id.llyt_home_rate).getVisibility() == View.GONE) {
                        holder.getView(R.id.llyt_home_rate).setVisibility(View.VISIBLE);
                    }
                    holder.setCircleImageUrl(R.id.iv_logo, schoolInfo.getLogo(), true);
                    holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                    holder.setText(R.id.tv_English_name, schoolInfo.getEnglishName());
                    if (schoolInfo.getCityName() != null && !"".equals(schoolInfo.getCityName())) {
                        holder.setText(R.id.tv_area, schoolInfo.getProvinceName() + "-" + schoolInfo.getCityName());
                    } else {
                        holder.setText(R.id.tv_area, schoolInfo.getProvinceName());
                    }
                    holder.setText(R.id.tv_rate, String.format(getString(R.string.rate),
                            TextUtils.isEmpty(schoolInfo.getTIE_ADMINSSION_RATE()) ? "暂无" : schoolInfo.getTIE_ADMINSSION_RATE()));
                }
            }
        };
        mSchoolAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < schoolsInfoList.size()) {
                    SchoolInfo info = schoolsInfoList.get(position);
                    if (ParameterUtils.HOME_RATE_FLAG.equals(flag_value)) {
                        Bundle data = new Bundle();
                        data.putString("schoolId", info.getId() + "");
                        data.putString("countryId", info.getCountryId());
                        data.putString("flag", flag_value);
                        Router.build("rateTest").with(data).go(CommonSearchActivity.this);
                    } else {
                        Bundle data = new Bundle();
                        data.putString("id", info.getId() + "");
                        Router.build("SchoolDetailActivity").with(data).go(CommonSearchActivity.this);
                    }
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mSchoolAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initRankAdapter() {
        schoolsRankList = new ArrayList<>();
        mRankAdapter = new CommonAdapter<SchooolRankInfo>(this, R.layout.item_rank_list, schoolsRankList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, SchooolRankInfo rankInfo, int position) {
                if (rankInfo != null) {
                    if (!TextUtils.isEmpty(rankInfo.getRank())) {
                        if (rankInfo.getRank().contains("-")) {
                            ((TextView) holder.getView(R.id.tv_school_rank)).setTextSize(12);

                        } else {
                            ((TextView) holder.getView(R.id.tv_school_rank)).setTextSize(17);
                        }
                        holder.setText(R.id.tv_school_rank, rankInfo.getRank());
                    }
                    holder.setText(R.id.tv_school_name, rankInfo.getChineseName());
                }
            }
        };
        mRankAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < schoolsRankList.size()) {
                    SchooolRankInfo info = schoolsRankList.get(position);
                    if (info.getSchoolId() != null && !TextUtils.isEmpty(info.getSchoolId())) {
                        Bundle data = new Bundle();
                        data.putString("id", info.getSchoolId());
                        Router.build("SchoolDetailActivity").with(data).go(CommonSearchActivity.this);
                    } else {
                        showTip(null, "该学校暂无更多详情可查看！");
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mRankAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initNewsAdapter() {
        newsInfoList = new ArrayList<>();
        mNewsAdapter = new CommonAdapter<NewsInfo>(this, R.layout.item_news_list, newsInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
                if (!TextUtils.isEmpty(typeName)) {
                    LinearLayout llyt_type = holder.getView(R.id.llyt_type_name);
                    if (position == 0) {
                        llyt_type.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_type_name, typeName);
                    } else {
                        llyt_type.setVisibility(View.GONE);
                    }
                }
                ImageView iv_cover = holder.getView(R.id.iv_cover);
                if (TextUtils.isEmpty(newsInfo.getCoverUrl())) {
                    iv_cover.setVisibility(View.GONE);
                } else {
                    iv_cover.setVisibility(View.VISIBLE);
                    holder.setImageUrl(iv_cover, newsInfo.getCoverUrl(), true);
                }
                holder.setText(R.id.tv_title, newsInfo.getTitle());
                TextView tv_tag = holder.getView(R.id.tv_tag);
                if (TextUtils.isEmpty(newsInfo.getTags())) {
                    tv_tag.setVisibility(View.GONE);
                } else {
                    tv_tag.setVisibility(View.VISIBLE);
                    tv_tag.setText(newsInfo.getTags());
                }
                holder.setText(R.id.tv_see_num, String.format(getString(R.string.news_see),
                        TextUtils.isEmpty(newsInfo.getVisitCount()) ? "0" : newsInfo.getVisitCount()));
            }
        };
        mNewsAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < newsInfoList.size()) {
                    NewsInfo info = newsInfoList.get(position);
                    Bundle data = new Bundle();
                    data.putString("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_NEWS_DETAIL), info.getId()));
                    data.putString("title", info.getTitle());
                    data.putString("url_action", "get");
                    Router.build("showWebView").with(data).go(CommonSearchActivity.this);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mNewsAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initSpeAdapter() {
        idNameInfoList = new ArrayList<>();
        mIdNameAdapter = new CommonAdapter<IdNameInfo>(this, R.layout.item_search_text_list, idNameInfoList, mInflater) {

            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                holder.setText(R.id.tv_title_name, idNameInfo.getName());
            }
        };
        mIdNameAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < idNameInfoList.size()) {
                    IdNameInfo info = idNameInfoList.get(position);
                    if (ParameterUtils.SPEDATA_FLAG.equals(flag_value)) {
                        Bundle data = new Bundle();
                        data.putString("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_SPE_INTRO), info.getId()));
                        data.putString("title", info.getName());
                        data.putString("url_action", "get");
                        Router.build("showWebView").with(data).go(CommonSearchActivity.this);
                    } else if (ParameterUtils.PROGRAMSPE_FLAG.equals(flag_value)) {
                        Bundle data = new Bundle();
                        data.putString("id", info.getId());
                        data.putString("title", info.getName() + "推荐院校");
                        Router.build("CommandSchoolListActivity").with(data).go(CommonSearchActivity.this);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mIdNameAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initRankTypeAdapter() {
        rankingsEntities = new ArrayList<>();
        mRankTypeAdapter = new CommonAdapter<RankTypeInfo.RankingsEntity>(this, R.layout.item_search_text_list, rankingsEntities, mInflater) {
            @Override
            protected void convert(ViewHolder holder, RankTypeInfo.RankingsEntity rankingsEntity, int position) {
                holder.setText(R.id.tv_title_name, rankingsEntity.getYear() + rankingsEntity.getType() + rankingsEntity.getTitle());

            }
        };
        mRankTypeAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < rankingsEntities.size()) {
                    RankTypeInfo.RankingsEntity info = rankingsEntities.get(position);
                    if (info != null) {
                        Bundle data = new Bundle();
                        data.putString("title", info.getYear() + info.getType() + info.getTitle());
                        data.putString("id", info.getId());
                        Router.build("RankActivity").with(data).go(CommonSearchActivity.this);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mRankTypeAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initQuesAdapter() {
        questionInfoList = new ArrayList<>();
        mQuesAdapter = new CommonAdapter<QuestionInfo>(this, R.layout.item_question_list, questionInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, QuestionInfo questionInfo, int position) {
                if (!TextUtils.isEmpty(typeName)) {
                    LinearLayout llyt_type = holder.getView(R.id.llyt_type_name);
                    if (position == 0) {
                        llyt_type.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_type_name, typeName);
                    } else {
                        llyt_type.setVisibility(View.GONE);
                    }
                }
                holder.getView(R.id.llyt_qa_person).setVisibility(View.VISIBLE);
                holder.getView(R.id.tv_see).setVisibility(View.VISIBLE);
                if (ParameterUtils.QA_FLAG.equals(flag_value)) {
                    holder.getView(R.id.view_home_list).setVisibility(View.VISIBLE);
                    holder.getView(R.id.llyt_answer_person).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_answer_name, questionInfo.getFirstAnswerer().getName());
                    holder.setText(R.id.tv_title, questionInfo.getFirstAnswerer().getTitle());
                } else if (ParameterUtils.HOME_QA_FLAG.equals(flag_value)) {
                    holder.getView(R.id.view_home_list).setVisibility(View.GONE);
                    holder.getView(R.id.llyt_answer_person).setVisibility(View.GONE);
                }
                String avatar = questionInfo.getAsker().getAvatar();
                String askName = questionInfo.getAsker().getName();
                TextView tv_default_name = holder.getView(R.id.tv_default_name);
                holder.setPersonImageUrl(R.id.iv_asker, avatar, true);

                tv_default_name.setVisibility(View.GONE);

                holder.setText(R.id.tv_qa_name, askName);
                holder.setText(R.id.tv_see, String.format(getString(R.string.visit_count), questionInfo.getVisitCount()));
                holder.setText(R.id.tv_qa, questionInfo.getContent());
                holder.setText(R.id.tv_time, questionInfo.getCreateTimeText());
            }
        };
        emptyWrapper = new EmptyWrapper<>(mQuesAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        mQuesAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < questionInfoList.size()) {
                    QuestionInfo info = questionInfoList.get(position);
                    Bundle data = new Bundle();
                    data.putString("id", info.getId() + "");
                    Router.build("qaDetail").with(data).go(CommonSearchActivity.this);
                    info = null;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initCourseAdapter() {
        courseInfoList = new ArrayList<>();
        mCourseAdapter = new CommonAdapter<CourseInfo>(this, R.layout.item_course_search, courseInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, final CourseInfo courseInfo, int position) {
                if (!TextUtils.isEmpty(typeName)) {
                    LinearLayout llyt_type = holder.getView(R.id.llyt_type_name);
                    if (position == 0) {
                        llyt_type.setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_type_name, typeName);
                    } else {
                        llyt_type.setVisibility(View.GONE);
                    }
                }
                DisplayImageUtils.formatImgUrl(CommonSearchActivity.this, courseInfo.getCoverUrl(), (ImageView) holder.getView(R.id.iv_course_cover));
                holder.setText(R.id.tv_course_name, courseInfo.getName());
                RatingBar rb_course_rate = holder.getView(R.id.rb_course_rate);
                rb_course_rate.setStar(Float.parseFloat(courseInfo.getRate()));
                holder.setText(R.id.tv_course_rate, courseInfo.getRate());
                holder.setText(R.id.tv_course_see, String.format(getString(R.string.visit_count), courseInfo.getPlayCount()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Router.build("courseDetail").with("id", courseInfo.getProductId()).with("courseCover", courseInfo.getCoverUrl()).go(CommonSearchActivity.this);
                    }
                });
            }
        };
        emptyWrapper = new EmptyWrapper<>(mCourseAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public void showResult(List data, int request_state, String flag) {
        if (searchP != null) {
            llyt_top_search.setBackgroundResource(R.color.main_bg);
            searchP.setEmptyView(mInflater, this, rclv_search, flag_value);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = data.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclv_search.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                            llyt_choose.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                            llyt_choose.setVisibility(View.GONE);
                        }
                    }
                    datas.clear();
                    datas.addAll(data);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();

                    //下拉刷新
                    if (len > 0) {
                        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                            llyt_choose.setVisibility(View.GONE);
                        }
                    } else {
                        rclv_search.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                            llyt_choose.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclv_search.loadComplete(false);
                        } else {
                            rclv_search.loadComplete(true);
                        }
                    } else {
                        if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                            llyt_choose.setVisibility(View.GONE);
                        }
                        rclv_search.loadComplete(true);
                        datas.addAll(data);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
        data = null;
    }

    private List getNowList() {
        if (ParameterUtils.RANKS_FLAG.equals(flag_value) || ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
            return schoolsRankList;
        } else if (ParameterUtils.NEWS_FLAG.equals(flag_value)) {
            return newsInfoList;
        } else if (ParameterUtils.SPEDATA_FLAG.equals(flag_value) || ParameterUtils.PROGRAMSPE_FLAG.equals(flag_value)) {
            return idNameInfoList;
        } else if (ParameterUtils.RANKTYPE_FLAG.equals(flag_value)) {
            return rankingsEntities;
        } else if (ParameterUtils.QA_FLAG.equals(flag_value) || ParameterUtils.HOME_QA_FLAG.equals(flag_value)) {
            return questionInfoList;
        } else if (ParameterUtils.COURSE_FLAG.equals(flag_value)) {
            return courseInfoList;
        } else {
            //默认搜索学校
            return schoolsInfoList;
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_search.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void setPresenter(CommonSearchContract.Presenter presenter) {
        if (presenter != null) {
            this.searchP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (searchP != null) {
            if (ParameterUtils.RESPONE_CODE_NETERR.equals(errCode)) {
                llyt_top_search.setBackgroundResource(R.color.main_bg);
                searchP.setEmptyView(mInflater, this, rclv_search, flag_value);
            }
            swipeRefreshLayout.setRefreshing(false);
            rclv_search.loadComplete(true);
            ToastUtils.showToast(this, message);
        }
    }
}
