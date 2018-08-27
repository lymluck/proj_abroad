package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.MySchoolInfo;
import com.smartstudy.xxd.mvp.contract.MySchoolContract;
import com.smartstudy.xxd.mvp.model.MyChooseSchoolModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class MySchoolPresenter implements MySchoolContract.Presenter {

    private MySchoolContract.View view;

    public MySchoolPresenter(MySchoolContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void getMySchool(int cacheType, final List<MySchoolInfo> schoolInfo) {
        MyChooseSchoolModel.getMySchool(cacheType, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                schoolInfo.clear();
                //有数据就隐藏sample
                int school_len = 0;
                JSONObject object = JSON.parseObject(result);
                List<MySchoolInfo> top_list = JSON.parseArray(object.getString("top"), MySchoolInfo.class);
                if (top_list != null) {
                    school_len += top_list.size();
                    schoolInfo.addAll(top_list);
                }
                List<MySchoolInfo> mid_list = JSON.parseArray(object.getString("middle"), MySchoolInfo.class);
                if (mid_list != null) {
                    school_len += mid_list.size();
                    schoolInfo.addAll(mid_list);
                }
                List<MySchoolInfo> bot_list = JSON.parseArray(object.getString("bottom"), MySchoolInfo.class);
                if (bot_list != null) {
                    school_len += bot_list.size();
                    schoolInfo.addAll(bot_list);
                }
                boolean selectionVisible = object.getBooleanValue("selectionVisible");
                view.getMySchoolSuccess(school_len, selectionVisible);
            }
        });
    }

    @Override
    public void editMySchool(final String school_Id, final String match_id) {
        MyChooseSchoolModel.editMySchool(school_Id, match_id, ParameterUtils.MATCH_SOURCE_SELECT, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.editMySchoolSuccess(match_id);
            }
        });
    }

    @Override
    public void deleteMySchool(final String school_Id, final int position) {
        MyChooseSchoolModel.deleteMySchool(school_Id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.delMySchoolSuccess(position);
            }
        });
    }

    @Override
    public void setPrivacy(final boolean visible) {
        MyChooseSchoolModel.setPrivacy(visible, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.setPrivacySuccess(visible);
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
