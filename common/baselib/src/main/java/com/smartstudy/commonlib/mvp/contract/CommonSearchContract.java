package com.smartstudy.commonlib.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/4.
 */

public interface CommonSearchContract {
    interface View extends BaseView<Presenter> {

        void showResult(List data, int request_state, String flag);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getHighSchools(int cacheType, String countryId, String keyword, int page, int request_state, String flag);

        void getSchools(int cacheType, String countryId, String keyword, int page, int request_state, String flag);

        void getRanks(int cacheType, String categoryId, String keyword, int page, int request_state, String flag);

        void getNews(int cacheType, String keyword, int page, int request_state, String flag);

        void getCourses(int cacheType, String keyword, int page, int request_state, String flag);

        void getSpecialData(int cacheType, String keyword, int page, int request_state, String flag);

        void getProgram(int cacheType, String typeId, String keyword, int page, int request_state, String flag);

        void getRankType(int cacheType, String keyword, int page, int request_state, String flag);

        void getRankArt(int cacheType, String keyword, int page, int request_state, String flag);

        void getQa(int cacheType, String keyword, int page, int request_state, String flag);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent, String from);
    }
}
