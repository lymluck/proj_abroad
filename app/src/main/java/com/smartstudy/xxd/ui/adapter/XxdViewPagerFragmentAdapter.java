package com.smartstudy.xxd.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.sdk.api.UApp;

import java.util.List;

/**
 * Created by louis on 2017/2/27.
 */

public class XxdViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<String> eventIds;
    private List<BaseFragment> fragments;

    public XxdViewPagerFragmentAdapter(FragmentManager fm, List<String> titles, List<String> eventIds, List<BaseFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.eventIds = eventIds;
        this.fragments = fragments;
    }

    public XxdViewPagerFragmentAdapter(FragmentManager fm, List<String> titles, List<BaseFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        if (titles != null && titles.size() > 0) {
            return titles.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (eventIds != null) {
            UApp.actionEvent(container.getContext(), eventIds.get(position));
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
