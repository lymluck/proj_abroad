package com.smartstudy.xxd.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.xxd.entity.ReportReasonsInfo;
import com.smartstudy.xxd.mvp.contract.ReportContract;
import com.smartstudy.xxd.mvp.model.ReportModel;
import com.smartstudy.xxd.mvp.model.SchoolModel;

import java.util.List;

/**
 * Created by yqy on 2017/12/27.
 */

public class ReportPresenter implements ReportContract.Presenter {

    private ReportContract.View view;

    public ReportPresenter(ReportContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }



    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getReportReson() {
        ReportModel.getReportReason(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<ReportReasonsInfo> data = JSON.parseArray(result, ReportReasonsInfo.class);
                if (data != null) {
                    view.showReportReson(data);
                    data = null;
                }
            }
        });
    }

    @Override
    public void submitReport(String targetId, final String reasonId) {
        ReportModel.submitReport(targetId,reasonId,new BaseCallback<String>(){
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {

                view.submitReportResult();
            }
        });
    }
}
