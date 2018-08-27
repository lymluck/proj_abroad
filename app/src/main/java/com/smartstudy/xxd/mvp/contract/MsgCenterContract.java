package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MsgCenterContract {

    interface View extends BaseView<MsgCenterContract.Presenter> {

        void showMsgs(List<DeviceMsgInfo> data);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getMsg(int page);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

    }
}
