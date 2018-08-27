package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.AbroadPlanInfo;
import com.smartstudy.xxd.mvp.contract.AbroadPlanContract;
import com.smartstudy.xxd.mvp.model.AbroadPlanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */
public class AbroadPlanPresenter implements AbroadPlanContract.Presenter {

    private AbroadPlanContract.View view;

    public AbroadPlanPresenter(AbroadPlanContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getPlan(int cacheType) {
        AbroadPlanModel.getPlan(cacheType, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                if ("SCHOOL_2101".equals(errCode)) {
                    view.toPlan();
                }
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<AbroadPlanInfo> datas = JSON.parseArray(result, AbroadPlanInfo.class);
//                List<BaseFragment> fragments = new ArrayList<>();
                List<String> names = new ArrayList<>();
                int position = 0;
                int currentIndex = 0;
                ArrayList<AbroadPlanInfo.StagesEntity> states = new ArrayList<>();
                if (datas != null) {
                    AbroadPlanInfo info;
                    AbroadPlanInfo.StagesEntity state;
                    for (int i = 0, len = datas.size(); i < len; i++) {
                        info = datas.get(i);
                        if (info.getIsCurrentGrade()) {
                            currentIndex = i;
                            if (states != null && states.size() > 0) {
                                position = states.size();
                            }
                        }
                        state = new AbroadPlanInfo.StagesEntity();
                        state.setCurrentIndex(i);
                        state.setTime("关键词");
                        state.setName(info.getGrade());
                        state.setItems(info.getKeywords());
                        states.add(state);
                        if (info.getStages() != null) {
                            for (AbroadPlanInfo.StagesEntity stagesEntity : info.getStages()) {
                                stagesEntity.setCurrentIndex(i);
                            }
                        }
                        states.addAll(info.getStages());
                        names.add(info.getGrade());

                        state = null;
                        info = null;
                    }
                    view.getPlanSuccess(datas, names, states, position, currentIndex);
                }
                datas = null;
                states = null;
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
