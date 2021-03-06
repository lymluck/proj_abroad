package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.SlideInBottomAnimationAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.NewsActivityContract;
import com.smartstudy.xxd.mvp.presenter.NewsActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

public class NewsActivity extends BaseActivity implements NewsActivityContract.View {

    private LoadMoreRecyclerView rclv_visa_news;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<NewsInfo> mAdapter;
    private LoadMoreWrapper<NewsInfo> loadMoreWrapper;
    private EmptyWrapper<NewsInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private NewsActivityContract.Presenter presenter;
    private List<NewsInfo> newsInfoList;
    private int mPage = 1;
    private String countryId;
    private String schoolId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    @Override
    protected void onDestroy() {
        if (rclv_visa_news != null) {
            rclv_visa_news.removeAllViews();
            rclv_visa_news = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (newsInfoList != null) {
            newsInfoList.clear();
            newsInfoList = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        countryId = data.getStringExtra("countryId");
        schoolId = data.getStringExtra("schoolId");
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(data.getStringExtra(TITLE));
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_visa_news);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getNews(mPage, ParameterUtils.PULL_DOWN);
            }
        });
        rclv_visa_news = (LoadMoreRecyclerView) findViewById(R.id.rclv_visa_news);
        rclv_visa_news.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_visa_news.setLayoutManager(mLayoutManager);
        rclv_visa_news.setItemAnimator(new DefaultItemAnimator());
        rclv_visa_news.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16), 0).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new NewsActivityPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_visa_news, false);
        presenter.showLoading(this, emptyView);
        getNews(mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        newsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<NewsInfo>(this, R.layout.item_news_list, newsInfoList) {
            @Override
            protected void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
                if (newsInfo != null) {
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
                    if (TextUtils.isEmpty(newsInfo.getVisitCount())) {
                        holder.setText(R.id.tv_see_num, String.format(getString(R.string.news_see), "0"));
                    } else {
                        holder.setText(R.id.tv_see_num, String.format(getString(R.string.news_see), newsInfo.getVisitCount()));
                    }
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(new SlideInBottomAnimationAdapter(mAdapter));
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclv_visa_news.setAdapter(loadMoreWrapper);
        rclv_visa_news.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rclv_visa_news.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                getNews(mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                NewsInfo info = newsInfoList.get(position);
                Intent toMoreDetails = new Intent(NewsActivity.this, ShowWebViewActivity.class);
                toMoreDetails.putExtra(WEBVIEW_URL, String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_NEWS_DETAIL), info.getId()));
                toMoreDetails.putExtra(TITLE, info.getTitle());
                toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
                startActivity(toMoreDetails);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void setPresenter(NewsActivityContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            rclv_visa_news.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getNewsSuccess(List<NewsInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_visa_news.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                newsInfoList.clear();
                newsInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (newsInfoList.size() > 0) {
                        rclv_visa_news.loadComplete(false);
                    } else {
                        rclv_visa_news.loadComplete(true);
                    }
                } else {
                    rclv_visa_news.loadComplete(true);
                    newsInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    private void getNews(int page, int pullAction) {
        if (!TextUtils.isEmpty(countryId)) {
            presenter.getVisaNews(countryId, page, pullAction);
        } else if (!TextUtils.isEmpty(schoolId)) {
            presenter.getSchoolNews(schoolId, page, pullAction);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_visa_news.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }
}
