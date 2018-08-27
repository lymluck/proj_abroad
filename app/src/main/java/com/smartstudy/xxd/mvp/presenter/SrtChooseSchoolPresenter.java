package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtChooseSchoolContract;
import com.smartstudy.xxd.mvp.model.SrtChooseSchoolModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtChooseSchoolPresenter implements SrtChooseSchoolContract.Presenter {

    private SrtChooseSchoolContract.View view;

    public SrtChooseSchoolPresenter(SrtChooseSchoolContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void goChoose(SmartChooseInfo info, final ArrayList<SmartSchoolRstInfo> schoolInfo) {
        SrtChooseSchoolModel.goChoose(info, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                schoolInfo.clear();
                //有数据就隐藏sample
                JSONObject object = JSON.parseObject(result);
                List<SmartSchoolRstInfo> top_list = JSON.parseArray(object.getString("top"), SmartSchoolRstInfo.class);
                if (top_list != null && top_list.size() > 0) {
                    top_list.get(0).setTop(true);
                    setMatchType(top_list, ParameterUtils.MATCH_TOP);
                    schoolInfo.addAll(top_list);
                }
                List<SmartSchoolRstInfo> mid_list = JSON.parseArray(object.getString("middle"), SmartSchoolRstInfo.class);
                if (mid_list != null && mid_list.size() > 0) {
                    mid_list.get(0).setTop(true);
                    setMatchType(mid_list, ParameterUtils.MATCH_MID);
                    schoolInfo.addAll(mid_list);
                }
                List<SmartSchoolRstInfo> bot_list = JSON.parseArray(object.getString("bottom"), SmartSchoolRstInfo.class);
                if (bot_list != null && bot_list.size() > 0) {
                    bot_list.get(0).setTop(true);
                    setMatchType(bot_list, ParameterUtils.MATCH_BOT);
                    schoolInfo.addAll(bot_list);
                }
                view.doChooseSuccess(schoolInfo);
                top_list = null;
                mid_list = null;
                bot_list = null;
            }
        });
    }

    @Override
    public void getHasTestNum() {
        SrtChooseSchoolModel.getHasTest(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    view.getHasTestNumSuccess(result);
                }
            }
        });
    }

    private void setMatchType(List<SmartSchoolRstInfo> list, String type) {
        for (SmartSchoolRstInfo info : list) {
            info.setMatch_type(type);
        }
    }
}
