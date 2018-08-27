package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MajorInfoContract {

    interface View extends BaseView<MajorInfoContract.Presenter> {
        void showFeature(String content);

        void showFactor(String content);

        void showEmployment(String content);

        void showAdvices(String content);

        void showPrograms(List<HomeHotProgramInfo> datas);
    }

    interface Presenter extends BasePresenter {

        void getMajorInfo(String id);
    }
}
