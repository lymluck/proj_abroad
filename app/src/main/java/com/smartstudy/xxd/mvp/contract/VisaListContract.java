package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.VisaInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface VisaListContract {

    interface View extends BaseView<VisaListContract.Presenter> {

        void showVisas(List<VisaInfo> data);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getVisas(String regionId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

    }
}
