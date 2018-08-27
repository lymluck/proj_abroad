package com.smartstudy.xxd.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HomeSearchListInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface HomeSearchContract {

    interface View extends BaseView<HomeSearchContract.Presenter> {

        void showEmptyView(android.view.View view);

        void showResult(List<HomeSearchListInfo> mDatas);
    }

    interface Presenter extends BasePresenter {

        void getResults(String keyword);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent);
    }
}
