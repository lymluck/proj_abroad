package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface QaListContract {

    interface View extends BaseView<QaListContract.Presenter> {

        void getQuestionsSuccess(List<QuestionInfo> data, int request_state);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getQuestions(int cacheType, boolean answered, int page, int request_state);

        void getMyQuestions(int cacheType, int page, int request_state);

        void getSchoolQa(String schoolId, int page, int request_state);

        void getTeacherQa(String teacherId, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView, String flag);
    }
}
