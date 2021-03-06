package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MarjorProgramRankContract {

    interface View extends BaseView<MarjorProgramRankContract.Presenter> {

        void getRankSuccess(List<HomeHotProgramInfo> data, int request_state);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getRank(int cacheType, String categoryId, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
