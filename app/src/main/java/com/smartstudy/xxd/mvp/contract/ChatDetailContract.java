package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.commonlib.entity.ConsultantsInfo;

/**
 * Created by yqy on 2017/12/26.
 */

public interface ChatDetailContract {
    interface View extends BaseView<ChatDetailContract.Presenter> {

        void getTeacherInfoSuccess(ConsultantsInfo teacherInfo);
    }

    interface Presenter extends BasePresenter {

        void getTeacherInfo(String ids);

    }
}
