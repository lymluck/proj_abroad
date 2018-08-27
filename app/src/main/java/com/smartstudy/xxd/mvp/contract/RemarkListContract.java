package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.SpecialRankInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.RemarkListInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface RemarkListContract {

    interface View extends BaseView<RemarkListContract.Presenter> {

        void getRemarkListSuccess(List<RemarkListInfo> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getRemarkList(String id, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
