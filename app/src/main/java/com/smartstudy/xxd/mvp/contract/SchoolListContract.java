package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface SchoolListContract {

    interface View extends BaseView<SchoolListContract.Presenter> {

        void getSchoolsSuccess(List<SchoolInfo> data, int request_state);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getSchools(String country_id, String rankRange, String egRang, String feeRang, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
