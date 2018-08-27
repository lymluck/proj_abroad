package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.MyRemarkPlanInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyRemarksPlanContract {
    interface View extends BaseView<MyRemarksPlanContract.Presenter> {

        void getMyRemarksPlanSuccess(List<MyRemarkPlanInfo> myRemarkPlanInfos);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getMyRemarks();

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
