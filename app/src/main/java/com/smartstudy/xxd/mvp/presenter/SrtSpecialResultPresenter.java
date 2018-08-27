package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.mvp.contract.SrtSpecialResultContract;
import com.smartstudy.xxd.mvp.model.MyChooseSchoolModel;
import com.smartstudy.xxd.mvp.model.SchoolModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtSpecialResultPresenter implements SrtSpecialResultContract.Presenter {

    private SrtSpecialResultContract.View view;


    public SrtSpecialResultPresenter(SrtSpecialResultContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getRecSchools(String ids, final List<SchoolInfo> list) {
        SchoolModel.getSchools(ids, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<SchoolInfo> data = JSON.parseArray(dataListInfo.getData(), SchoolInfo.class);
                if (data != null) {
                    list.clear();
                    list.addAll(data);
                    view.dataRefres();
                }
            }
        });
    }

    @Override
    public void add2MySchool(String match_type, int school_Id, final int position) {
        MyChooseSchoolModel.editMySchool(school_Id + "", match_type, ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.noitfyItem(position);
                view.showTip(null, "已添加至我的选校！");
            }
        });
    }

    @Override
    public void addAll2Myschool(String match_type, final List<SchoolInfo> schoolInfo) {
        MyChooseSchoolModel.editMySchool(initData(schoolInfo, match_type), ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                for (SchoolInfo info : schoolInfo) {
                    if (!info.isSelected()) {
                        info.setSelected(true);
                    }
                }
                view.addAll2MyschoolSucc();
                view.showTip(null, "一键加入成功！");
            }
        });
    }

    @Override
    public void deleteMyChoose(String school_Id, final int position) {
        MyChooseSchoolModel.deleteMySchool(school_Id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.noitfyItem(position);
                view.showTip(null, "已从我的选校中移除！");
            }
        });
    }

    private String initData(List<SchoolInfo> schoolInfo, String match_type) {
        JSONArray arr = new JSONArray();
        JSONObject object = null;
        for (SchoolInfo info : schoolInfo) {
            object = new JSONObject();
            object.put("schoolId", info.getId());
            object.put("matchTypeId", match_type);
            arr.add(object);
        }
        return arr.toString();
    }
}
