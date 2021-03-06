package com.smartstudy.xxd.ui.fragment;

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
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
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
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.mvp.contract.NewsListFragmentContract;
import com.smartstudy.xxd.mvp.presenter.NewsListPresenter;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;


public class NewsListFragment extends BaseFragment implements NewsListFragmentContract.View {
    private LoadMoreRecyclerView lmrvNews;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<NewsInfo> mAdapter;
    private LoadMoreWrapper<NewsInfo> loadMoreWrapper;
    private EmptyWrapper<NewsInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private CountryTypeInfo titleInfo;
    private NewsListFragmentContract.Presenter presenter;
    private List<NewsInfo> newsInfoList;
    private int mPage = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_news_list;
    }

    public static NewsListFragment getInstance(Bundle bundle) {
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvNews != null) {
            lmrvNews.removeAllViews();
            lmrvNews = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (newsInfoList != null) {
            newsInfoList.clear();
            newsInfoList = null;
        }
        if (titleInfo != null) {
            titleInfo = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvNews,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getNews(ParameterUtils.CACHED_ELSE_NETWORK, titleInfo.getTagId(), mPage,
            ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initView() {
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_news);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getNews(ParameterUtils.NETWORK_ELSE_CACHED, titleInfo.getTagId(), mPage,
                    ParameterUtils.PULL_DOWN);
            }
        });
        lmrvNews = rootView.findViewById(R.id.rclv_news);
        lmrvNews.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvNews.setLayoutManager(mLayoutManager);
        lmrvNews.setItemAnimator(new DefaultItemAnimator());
        lmrvNews.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f))
            .margin(DensityUtils.dip2px(16), 0)
            .colorResId(R.color.horizontal_line_color)
            .build());
        initAdapter();
        titleInfo = getArguments().getParcelable("title");
        new NewsListPresenter(this);
    }

    private void initAdapter() {
        newsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<NewsInfo>(mActivity, R.layout.item_news_list, newsInfoList) {
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
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvNews.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvNews.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvNews.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getNews(ParameterUtils.CACHED_ELSE_NETWORK, titleInfo.getTagId(), mPage,
                    ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UApp.actionEvent(mActivity, "17_A_news_details_cell");
                //跳转到webview
                NewsInfo info = newsInfoList.get(position);
                Intent toMoreDetails = new Intent(mActivity, ShowWebViewActivity.class);
                toMoreDetails.putExtra(WEBVIEW_URL,
                    String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_NEWS_DETAIL), info.getId()));
                toMoreDetails.putExtra("from", "homeTabNews");
                toMoreDetails.putExtra("title", info.getTitle());
                toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
                startActivity(toMoreDetails);
                info = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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
                    lmrvNews.loadComplete(true);
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
                        lmrvNews.loadComplete(false);
                    } else {
                        lmrvNews.loadComplete(true);
                    }
                } else {
                    lmrvNews.loadComplete(true);
                    newsInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    /**
     * 展示空页面
     *
     * @param view
     */
    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvNews.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void setPresenter(NewsListFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            lmrvNews.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }
}
