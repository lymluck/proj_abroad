package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ActivityInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface ActivityListFragmentContract {

    interface View extends BaseView<ActivityListFragmentContract.Presenter> {

        void showList(List<ActivityInfo> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getActivities(String major, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
