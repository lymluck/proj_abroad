package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.xxd.ui.fragment.FragmentFactory;

/**
 * Created by louis on 2017/3/1.
 */

public interface MainActivityContract {

    interface View extends BaseView<MainActivityContract.Presenter> {

        void updateable(String downUrl, String version, String des);

        void forceUpdate(String downUrl, String version, String des);

        void hideLastFragment(BaseFragment nowFragment);

        void onShowFragment(BaseFragment fragment);

        void selectedTab(int index);

        void refreshBannerFragment(boolean show);

        void showQaRed(int visible);

    }

    interface Presenter extends BasePresenter {

        void getVersion(String channel);

        void showFragment(FragmentFactory fragmentFactory, int index);

        int currentIndex();

        void isGetMyPlan();
    }
}
