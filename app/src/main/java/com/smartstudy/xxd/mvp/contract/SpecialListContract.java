package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.MajorInfo;
import com.smartstudy.commonlib.entity.ProgramInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface SpecialListContract {

    interface View extends BaseView<SpecialListContract.Presenter> {

        void showSpecialData(List<MajorInfo> data);

        void showProgramSpecial(List<ProgramInfo> data);

        void expandItem(int position);

        void showEmptyView(android.view.View view);

        void reload();
    }

    interface Presenter extends BasePresenter {

        void getData(String flag, String title);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
