package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HotRankInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface HotRankContract {

    interface View extends BaseView<HotRankContract.Presenter> {

        void showList(List<HotRankInfo> data);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getRank(String flag);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
