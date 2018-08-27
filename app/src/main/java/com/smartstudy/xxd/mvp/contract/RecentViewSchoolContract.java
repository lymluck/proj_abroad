package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.RecentViewSchoolInfo;
import com.smartstudy.xxd.entity.RemarkListInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface RecentViewSchoolContract {
    interface View extends BaseView<RecentViewSchoolContract.Presenter> {

        void getRecentSuccess(List<RecentViewSchoolInfo> data,int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getRecentSchoolList(int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
