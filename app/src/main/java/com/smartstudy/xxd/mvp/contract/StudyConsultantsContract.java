package com.smartstudy.xxd.mvp.contract;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by yqy on 2017/12/21.
 */

public interface StudyConsultantsContract {
    interface View extends BaseView<StudyConsultantsContract.Presenter> {

        void hideFragment(FragmentManager fragmentManager);

        void showChatListFragment(FragmentTransaction ft);

        void showConsultantsListFragment(FragmentTransaction ft);

    }

    interface Presenter extends BasePresenter {

        void showFragment(FragmentManager fragmentManager, int index);

        int currentIndex();

    }
}
