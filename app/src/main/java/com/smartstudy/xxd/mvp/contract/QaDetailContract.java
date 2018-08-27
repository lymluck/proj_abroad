package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ConsultantsInfo;
import com.smartstudy.xxd.entity.QaDetailInfo;

/**
 * Created by yqy on 2017/12/4.
 */

public interface QaDetailContract {

    interface View extends BaseView<QaDetailContract.Presenter> {

        void getQaDetails(QaDetailInfo data);

        void postQuestionSuccess();

        void getTeacherInfoSuccess(ConsultantsInfo consultantsInfo);

        void updateRatingSuccess();
    }

    interface Presenter extends BasePresenter {

        void getQaDetails(String id);

        void postQuestion(String id, String answerId, String content);

        void getTeacher(String targeId);

        void updateRating(String questionId, String answerId, String score, String comment);
    }
}
