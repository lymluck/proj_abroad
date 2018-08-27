package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ChoiceSchoolListInfo;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.entity.RemarkListInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ChoiceSchoolListContract {
    interface View extends BaseView<ChoiceSchoolListContract.Presenter> {

        void getChoiceSchoolListSuccess(List<ChoiceSchoolListInfo> data, int request_state);

        void showEmptyView(android.view.View view);

        void getOtherStudentDetailSuccess(OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo);

    }

    interface Presenter extends BasePresenter {

        void getChoiceSchoolList(String id, int page, int request_state);

        void getOtherStudentDetail(String id);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
