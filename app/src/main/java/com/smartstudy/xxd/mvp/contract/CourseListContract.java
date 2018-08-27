package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.CourseInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.CourseGroup;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface CourseListContract {

    interface View extends BaseView<CourseListContract.Presenter> {

        void showCourses(List<CourseGroup> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getCourses(int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

    }
}
