package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.QaDetailInfo;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public interface QaDetailContract {

    interface View extends BaseView<QaDetailContract.Presenter> {

        void getQaDetails(QaDetailInfo data);

        void postQuestionSuccess();

        void getTeacherInfoSuccess(ConsultantsInfo consultantsInfo);
    }

    interface Presenter extends BasePresenter {

        void getQaDetails(String id);

        void postQuestion(String id, String answerId, String content);

        void getTeacher(String targeId);

    }
}
