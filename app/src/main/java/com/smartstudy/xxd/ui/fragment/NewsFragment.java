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
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.customView.PagerSlidingTabStrip;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends UIFragment {

    private TextView topdefault_rightmenu;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_news, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
    }

    @Override
    protected void initView(View rootView) {
        RelativeLayout top_news = (RelativeLayout) rootView.findViewById(R.id.top_news);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top_news.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight(mActivity);
            top_news.setLayoutParams(params);
            top_news.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.topdefault_centertitle)).setText(mActivity.getString(R.string.news));
        topdefault_rightmenu = (TextView) rootView.findViewById(R.id.topdefault_rightmenu);
        topdefault_rightmenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_blue, 0, 0, 0);
        topdefault_rightmenu.setVisibility(View.VISIBLE);
        topdefault_rightmenu.setOnClickListener(this);
        ImageView topdefault_leftbutton = (ImageView) rootView.findViewById(R.id.topdefault_leftbutton);
        topdefault_leftbutton.setOnClickListener(this);
        Bundle data = getArguments();
        if (data == null) {
            topdefault_leftbutton.setVisibility(View.GONE);
        }
        ViewPager pager_news = (ViewPager) rootView.findViewById(R.id.pager_news);
        ArrayList<UIFragment> fragments = new ArrayList<>();
        List<CountryTypeInfo> countryTypeInfos = JSON.parseArray(Utils.getJson("home_school_title.json"), CountryTypeInfo.class);
        List<String> titles = new ArrayList<>();
        for (CountryTypeInfo info : countryTypeInfos) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("title", info);
            titles.add(info.getCountryName());
            fragments.add(NewsListFragment.getInstance(bundle));
        }
        pager_news.setAdapter(new XxdViewPagerFragmentAdapter(getChildFragmentManager(), titles, fragments));
        pager_news.setOffscreenPageLimit(4);
        PagerSlidingTabStrip tabs_news = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs_news);
        tabs_news.setViewPager(pager_news);
        pager_news.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            case R.id.topdefault_rightmenu:
                Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.NEWS_FLAG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                            topdefault_rightmenu, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
                break;
        }
    }
}
