package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.QaDetailInfo;
import com.smartstudy.xxd.mvp.contract.QaDetailContract;
import com.smartstudy.xxd.mvp.model.QaDetailModel;

/**
 * Created by yqy on 2017/12/4.
 */

public class QaDetailPresenter implements QaDetailContract.Presenter {

    private QaDetailContract.View view;

    public QaDetailPresenter(QaDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getQaDetails(final String id) {
        QaDetailModel.getQaDetail(ParameterUtils.NETWORK_ELSE_CACHED, id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                QaDetailInfo qaDetailInfo = JSON.parseObject(result, QaDetailInfo.class);

                if (qaDetailInfo != null) {
                    view.getQaDetails(qaDetailInfo);
                }
            }
        });
    }

    @Override
    public void postQuestion(String id, String answerId, String question) {
        QaDetailModel.postQuestion(id, answerId, question, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.postQuestionSuccess();
            }
        });
    }

    @Override
    public void getTeacher(String targeId) {
        QaDetailModel.getTeacheInfo(ParameterUtils.NETWORK_ELSE_CACHED, targeId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                ConsultantsInfo consultantsInfo = JSON.parseObject(result, ConsultantsInfo.class);
                if (consultantsInfo != null) {
                    view.getTeacherInfoSuccess(consultantsInfo);
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}