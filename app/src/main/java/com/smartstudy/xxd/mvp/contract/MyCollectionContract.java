package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.CollectionInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MyCollectionContract {

    interface View extends BaseView<MyCollectionContract.Presenter> {

        void getCollectionSuccess(List<CollectionInfo> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getCollection(int cacheType, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);

    }
}
