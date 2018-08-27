package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.VisaAddrInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface VisaAddrContract {

    interface View extends BaseView<VisaAddrContract.Presenter> {

        void showVisaAddr(List<VisaAddrInfo> data);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getVisaAddr(String regionId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
