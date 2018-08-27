package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.SpecialRankInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface SpecialRankContract {

    interface View extends BaseView<SpecialRankContract.Presenter> {

        void getTypeSuccess();

        void showEmptyView(android.view.View view);

        void reload();
    }

    interface Presenter extends BasePresenter {

        void getTypeList(int cacheType, int flag, List<SpecialRankInfo> specialRankInfos);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
