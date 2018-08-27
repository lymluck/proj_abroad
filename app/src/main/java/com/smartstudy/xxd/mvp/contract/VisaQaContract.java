package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.VisaQaInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface VisaQaContract {

    interface View extends BaseView<VisaQaContract.Presenter> {

        void showVisaQas(List<VisaQaInfo> data);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getVisaQa(String regionId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
