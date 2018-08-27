package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.mvp.model.QuestionsModel;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.QaListContract;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class QuestionsPresenter implements QaListContract.Presenter {

    private QaListContract.View view;

    public QuestionsPresenter(QaListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getQuestions(int cacheType, boolean answered, int page, final int request_state) {
        QuestionsModel.getQuestions(cacheType, answered, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
                    data = null;
                }
            }
        });
    }

    @Override
    public void getMyQuestions(int cacheType, int page, final int request_state) {
        QuestionsModel.getMyQuestions(cacheType, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
                    data = null;
                }
            }
        });
    }

    @Override
    public void getSchoolQa(String schoolId, int page, final int request_state) {
        QuestionsModel.getSchoolQa(schoolId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
                    data = null;
                }
            }
        });
    }

    @Override
    public void getTeacherQa(String teacherId, int page, final int request_state) {

        QuestionsModel.getTeacherQa(teacherId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
                    data = null;
                }
            }
        });
    }

    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }

    @Override
    public void setEmptyView(Context context, View emptyView, String flag) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!"list".equals(flag)) {
            iv_err.setImageResource(R.drawable.ic_no_question);
            if (flag.equals("teacher")) {
                tv_err_tip.setText("还没有回答过别人的问题哦～");
            } else {
                tv_err_tip.setText(context.getString(R.string.no_qa_tip));
            }
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }
}
