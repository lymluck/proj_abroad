package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ReportReasonsInfo;

import java.util.List;

/**
 * Created by yqy on 2017/12/27.
 */

public interface ReportContract {
    interface View extends BaseView<ReportContract.Presenter> {

        void showReportReson(List<ReportReasonsInfo> reportReasonsInfos);

        void submitReportResult();

    }

    interface Presenter extends BasePresenter {

        void getReportReson();

        void submitReport(String targetId, String reasonId);
    }
}
