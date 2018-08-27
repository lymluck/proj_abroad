package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by yqy on 2017/12/21.
 */

public interface ConsultantsContract {
    interface View extends BaseView<ConsultantsContract.Presenter> {

        void showConsultantsList(List<ConsultantsInfo> data, int request_state);

        void showEmptyView(android.view.View view);

        void toChat(int status, ConsultantsInfo info);
    }

    interface Presenter extends BasePresenter {

        void getConsultantsList(int page, int request_state);

        void getTeacherInfo(ConsultantsInfo info);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

    }
}
