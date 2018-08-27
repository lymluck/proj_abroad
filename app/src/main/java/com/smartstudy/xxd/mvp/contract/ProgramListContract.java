package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.MajorProgramInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface ProgramListContract {

    interface View extends BaseView<ProgramListContract.Presenter> {

        void showData(List<MajorProgramInfo> datas);
    }

    interface Presenter extends BasePresenter {

        void getProgramList();
    }
}
