package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ThematicCenterInfo;

import java.util.List;

/**
 * Created by yqy on 2017/12/8.
 */

public interface StudyGetPlanningContract {
    interface View extends BaseView<StudyGetPlanningContract.Presenter> {
        void postSuccess();
    }

    interface Presenter extends BasePresenter {
        void postStudyPlanning(String currentGradeId, String targetDegreeId);
    }
}
