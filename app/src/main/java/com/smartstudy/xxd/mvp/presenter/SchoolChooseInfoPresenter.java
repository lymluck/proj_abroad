package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.entity.SchoolChooseInfo;
import com.smartstudy.xxd.mvp.contract.SchoolChooseInfoContract;
import com.smartstudy.xxd.mvp.model.ChoiceSchoolListModel;
import com.smartstudy.xxd.mvp.model.SchoolChooseModel;

/**
 * Created by louis on 2017/3/2.
 */
public class SchoolChooseInfoPresenter implements SchoolChooseInfoContract.Presenter {

    private SchoolChooseInfoContract.View view;

    public SchoolChooseInfoPresenter(SchoolChooseInfoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getSchoolStat(String id) {
        SchoolChooseModel.getSchoolStat(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                SchoolChooseInfo info = JSON.parseObject(result, SchoolChooseInfo.class);
                if (info != null) {
                    view.showSchoolStat(info);
                }
            }
        });

    }

    @Override
    public void getOtherStudentDetail(String id) {
        ChoiceSchoolListModel.getOtherStudentDetail(id + "", new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo = JSON.parseObject(result, OtherStudentChoiceDetailInfo.class);
                if (otherStudentChoiceDetailInfo != null) {
                    view.getOtherStudentDetailSuccess(otherStudentChoiceDetailInfo);
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }


}
