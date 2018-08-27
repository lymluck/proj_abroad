package com.smartstudy.xxd.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.smartstudy.commonlib.ui.activity.base.UIFragment;

import java.util.List;

/**
 * Created by louis on 2017/2/27.
 */

public class XxdViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<UIFragment> fragments;

    public XxdViewPagerFragmentAdapter(FragmentManager fm, List<String> list, List<UIFragment> fragments) {
        super(fm);
        this.titles = list;
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
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
