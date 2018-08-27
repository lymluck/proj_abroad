package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ColumnListItemInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ColumnListContract {
    interface View extends BaseView<ColumnListContract.Presenter> {

        void showList(List<ColumnListItemInfo> datas, int request_state);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getColumn(int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
