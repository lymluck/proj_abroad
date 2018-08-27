package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface AddQaContract {

    interface View extends BaseView<AddQaContract.Presenter> {

        void getOptionsSuccess(List<IdNameInfo> country, List<IdNameInfo> degree);

        void addQaSuccess();

    }

    interface Presenter extends BasePresenter {

        void getOptions();

        void addQa(String content, String countryId, String projectId);
    }
}
