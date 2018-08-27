package com.smartstudy.xxd.mvp.presenter;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.mvp.contract.StudyGetPlanningContract;
import com.smartstudy.xxd.mvp.model.StudyGetPlanningModel;


/**
 * Created by yqy on 2017/12/8.
 */

public class StudyGetPlanningPresenter implements StudyGetPlanningContract.Presenter{
    private StudyGetPlanningContract.View view;

    public StudyGetPlanningPresenter(StudyGetPlanningContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void postStudyPlanning(String currentGradeId, String targetDegreeId) {
        StudyGetPlanningModel.postStudyGetPlanning(currentGradeId,targetDegreeId,new BaseCallback<String>(){
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.postSuccess();
            }
        });

    }
}
