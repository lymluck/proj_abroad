package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.GpaCalculationDetail;

import java.util.List;

/**
 * Created by yqy on 2017/10/24.
 */

public interface GpaCalculationDetailContract {

    interface View extends BaseView<GpaCalculationDetailContract.Presenter> {

        void showGpaDetail(List<GpaCalculationDetail> data);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getGpaDetail(String results);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
