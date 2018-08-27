package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.CourseIntroInfo;

/**
 * Created by louis on 2017/3/1.
 */

public interface CourseIntroContract {

    interface View extends BaseView<CourseIntroContract.Presenter> {

        void showIntro(CourseIntroInfo info);

        void showTeacher(CourseIntroInfo.TeachersEntity teachersEntity);

    }

    interface Presenter extends BasePresenter {

        void getIntros(String courseId);
    }
}
