package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment {

    private TextView topRightMenu;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
        UApp.actionEvent(mActivity, "17_B_news_list");
    }

    @Override
    protected void initView() {
        RelativeLayout newsTop = rootView.findViewById(R.id.top_news);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newsTop.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight();
            newsTop.setLayoutParams(params);
            newsTop.setPadding(0, ScreenUtils.getStatusHeight(), 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.topdefault_centertitle)).setText(mActivity.getString(R.string.news));
        topRightMenu = rootView.findViewById(R.id.topdefault_rightmenu);
        topRightMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_blue, 0, 0, 0);
        topRightMenu.setVisibility(View.VISIBLE);
        topRightMenu.setOnClickListener(this);
        ImageView topLeftBtn = rootView.findViewById(R.id.topdefault_leftbutton);
        topLeftBtn.setOnClickListener(this);
        Bundle data = getArguments();
        if (data == null) {
            topLeftBtn.setVisibility(View.GONE);
        }
        ViewPager pagerNews = rootView.findViewById(R.id.pager_news);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        List<CountryTypeInfo> countryTypeInfos = JSON.parseArray(
            Utils.getJson("json/home_school_title.json"), CountryTypeInfo.class);
        List<String> titles = new ArrayList<>();
        List<String> eventIds = new ArrayList<>();
        for (CountryTypeInfo info : countryTypeInfos) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("title", info);
            titles.add(info.getCountryName());
            eventIds.add(info.getEventId());
            fragments.add(NewsListFragment.getInstance(bundle));
        }
        pagerNews.setAdapter(new XxdViewPagerFragmentAdapter(getChildFragmentManager(), titles, eventIds, fragments));
        pagerNews.setOffscreenPageLimit(4);
        PagerSlidingTabStrip newTabs = rootView.findViewById(R.id.tabs_news);
        newTabs.setViewPager(pagerNews);
        pagerNews.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            case R.id.topdefault_rightmenu:
                UApp.actionEvent(mActivity, "17_A_search_btn");
                Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.NEWS_FLAG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                        topRightMenu, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
                break;
            default:
                break;
        }
    }
}
