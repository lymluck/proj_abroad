package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HotMajorInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface FindMajorContract {

    interface View extends BaseView<FindMajorContract.Presenter> {

        void showHotMajor(List<HotMajorInfo> datas);

    }

    interface Presenter extends BasePresenter {

        void getHotMajor();
    }
}
