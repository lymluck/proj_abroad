package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.commonlib.ui.customview.calendar.entity.CalendarInfo;
import com.smartstudy.xxd.entity.ExamDateInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface ExamDateContract {

    interface View extends BaseView<ExamDateContract.Presenter> {

        void getDateSuccess(String updateTime, String count, List<ExamDateInfo> datas);

        void initDates(List<CalendarInfo> datas, int currYear, int currMonth, boolean needLine, String exam);

        void joinExamSuccess();

        void delExamSuccess();

        void reload();
    }

    interface Presenter extends BasePresenter {

        void getExamDate(int cacheType, boolean plain, String exam);

        void joinExam(int id);

        void delExam(int id);

        List<String> getJoinDates(List<ExamDateInfo> datas, String des);

        void showLoading(Context context, android.view.View emptyView);
    }
}
