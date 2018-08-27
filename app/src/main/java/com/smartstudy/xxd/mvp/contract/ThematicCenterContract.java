package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ThematicCenterInfo;

import java.util.List;

/**
 * Created by yqy on 2017/10/10.
 */

public interface ThematicCenterContract {
    interface View extends BaseView<Presenter> {

        void showThematicCenter(List<ThematicCenterInfo> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getThematicCenter(int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

    }
}
