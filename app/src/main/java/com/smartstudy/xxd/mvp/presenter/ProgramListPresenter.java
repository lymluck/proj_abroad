package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.MajorProgramInfo;
import com.smartstudy.xxd.mvp.contract.ProgramListContract;
import com.smartstudy.xxd.mvp.model.MajorModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */
public class ProgramListPresenter implements ProgramListContract.Presenter {

    private ProgramListContract.View view;

    public ProgramListPresenter(ProgramListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getProgramList() {
        MajorModel.getProgramList(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<MajorProgramInfo> datas = JSON.parseArray(result, MajorProgramInfo.class);
                if (datas != null) {
                    view.showData(datas);
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }

}
