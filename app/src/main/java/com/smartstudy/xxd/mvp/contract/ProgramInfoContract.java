package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/1.
 */

public interface ProgramInfoContract {

    interface View extends BaseView<ProgramInfoContract.Presenter> {

        void showTop(String logo, String name, String egName, String schoolName, String rank);

        void showFeature(String content);

        void showRequest(String content);

        void showCourse(String content);
    }

    interface Presenter extends BasePresenter {

        void getProgramInfo(String id);
    }
}
