package com.smartstudy.xxd.ui.fragment;

import android.util.SparseArray;

import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.utils.ParameterUtils;

/**
 * @author louis
 * @date on 2018/4/2
 * @describe fragment工厂方法模式
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class FragmentFactory {

    private SparseArray<BaseFragment> mFragments = new SparseArray<>();

    public BaseFragment createMainFragment(int position) {
        BaseFragment fragment = mFragments.get(position);
        if (fragment == null) {
            switch (position) {
                case ParameterUtils.FRAGMENT_TAB_ONE:
                    fragment = new BannerFragment();
                    break;
                case ParameterUtils.FRAGMENT_TAB_TWO:
                    fragment = new CourseListFragment();
                    break;
                case ParameterUtils.FRAGMENT_TAB_THREE:
                    fragment = new NewsFragment();
                    break;
                case ParameterUtils.FRAGMENT_TAB_THOUR:
                    fragment = new QaFragment();
                    break;
                case ParameterUtils.FRAGMENT_TAB_FIVE:
                    fragment = new MeFragment();
                    break;
                default:
                    break;
            }
        }
        if (fragment != null) {
            mFragments.put(position, fragment);
            if (!fragment.getUserVisibleHint()) {
                fragment.setUserVisibleHint(true);
            }
        }
        return fragment;
    }

    public void delFragment(int position) {
        mFragments.remove(position);
    }

    public BaseFragment getFragment(int position) {
        return mFragments.get(position);
    }

}
