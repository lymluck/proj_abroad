package com.smartstudy.xxd.mvp.contract;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/1.
 */

public interface MainActivityContract {

    interface View extends BaseView<MainActivityContract.Presenter> {

        void updateable(String downUrl, String des);

        void forceUpdate(String downUrl, String des);

        void hideFragment(FragmentTransaction ft);

        void showSchools(FragmentTransaction ft);

        void showCourse(FragmentTransaction ft);

        void showNews(FragmentTransaction ft);

        void showQa(FragmentTransaction ft);

        void showMe(FragmentTransaction ft);

    }

    interface Presenter extends BasePresenter {

        void getVersion();

        void showFragment(FragmentManager fragmentManager, int index);

        int currentIndex();
    }
}
