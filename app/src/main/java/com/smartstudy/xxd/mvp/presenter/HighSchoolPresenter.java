package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.xxd.mvp.contract.HighSchoolContract;
import com.smartstudy.xxd.mvp.model.CollectionModel;
import com.smartstudy.xxd.mvp.model.SchoolModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class HighSchoolPresenter implements HighSchoolContract.Presenter {

    private HighSchoolContract.View view;

    public HighSchoolPresenter(HighSchoolContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void isCollected(String id) {
        CollectionModel.isCollected("highschool", id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.isCollected(Boolean.parseBoolean(result));
            }
        });
    }

    @Override
    public void getSchoolDetail(final String schoolId) {
        SchoolModel.getHighSchoolInfo(schoolId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                String name = data.getString("chineseName");
                String egName = data.getString("englishName");
                String addr = data.getString("location");
                String rank = data.getString("rank");
                //显示头部
                view.showTop(name, egName, addr, rank);
                //隐藏loading
                view.hideLoading();
                //显示简介
                String introduction = data.getString("introduction");
                if (!TextUtils.isEmpty(introduction)) {
                    view.showIntro(introduction);
                }
                //显示照片
                List<String> photos = JSONArray.parseArray(data.getString("photos"), String.class);
                if (photos != null && photos.size() > 0) {
                    view.showPhotos(photos);
                }
                //显示概况
                List<IdNameInfo> summs = JSONArray.parseArray(data.getString("information"), IdNameInfo.class);
                if (summs != null && summs.size() > 0) {
                    view.showSummary(summs);
                }
                //显示数据
                List<IdNameInfo> schoolDatas = JSONArray.parseArray(data.getString("stat"), IdNameInfo.class);
                if (schoolDatas != null && schoolDatas.size() > 0) {
                    view.showData(schoolDatas);
                }
                //显示预算
                List<IdNameInfo> fees = JSONArray.parseArray(data.getString("fees"), IdNameInfo.class);
                if (fees != null && fees.size() > 0) {
                    view.showFees(fees);
                }
                //显示申请情况
                List<IdNameInfo> application = JSONArray.parseArray(data.getString("application"), IdNameInfo.class);
                if (application != null && application.size() > 0) {
                    view.showApplications(application);
                }
                //显示暑期学校
                List<IdNameInfo> summerSchool = JSONArray.parseArray(data.getString("summerSchool"), IdNameInfo.class);
                if (summerSchool != null && summerSchool.size() > 0) {
                    view.showSummer(summerSchool);
                }
                //显示联系信息
                List<IdNameInfo> contact = JSONArray.parseArray(data.getString("contact"), IdNameInfo.class);
                if (contact != null && contact.size() > 0) {
                    view.showContact(contact);
                }
                //体育项目
                List<String> sports = JSONArray.parseArray(data.getString("sports"), String.class);
                if (sports != null && sports.size() > 0) {
                    view.showSports(getItems(sports));
                }
                //AP课程
                List<String> apCourses = JSONArray.parseArray(data.getString("apCourses"), String.class);
                if (apCourses != null && apCourses.size() > 0) {
                    view.showApCourses(getItems(apCourses));
                }
                //社团
                List<String> communities = JSONArray.parseArray(data.getString("communities"), String.class);
                if (communities != null && communities.size() > 0) {
                    view.showCommunities(getItems(communities));
                }
                //学校特色
                List<String> features = JSONArray.parseArray(data.getString("features"), String.class);
                if (features != null && features.size() > 0) {
                    view.showFeatures(features);
                }
                //学校优势
                List<String> pros = JSONArray.parseArray(data.getString("pros"), String.class);
                if (pros != null && pros.size() > 0) {
                    view.showPros(pros);
                }
                //学校荣誉
                List<String> honors = JSONArray.parseArray(data.getString("honors"), String.class);
                if (honors != null && honors.size() > 0) {
                    view.showHonors(honors);
                }
                //学校设施
                List<String> facilities = JSONArray.parseArray(data.getString("facilities"), String.class);
                if (facilities != null && facilities.size() > 0) {
                    view.showFacilities(facilities);
                }
                //esl课程
                String esl = data.getString("eslCourse");
                if (!TextUtils.isEmpty(esl)) {
                    view.showEsl(esl);
                }
            }
        });
    }

    @Override
    public void collect(String schoolId) {
        CollectionModel.collect("highschool", schoolId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.collectSuccess();
            }
        });
    }

    @Override
    public void disCollect(String schoolId) {
        CollectionModel.disCollect("highschool", schoolId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.discollectSuccess();
            }
        });
    }

    private List<String> getItems(List<String> data) {
        List<String> items = new ArrayList<>();
        for (String item : data) {
            String egName = JSON.parseObject(item).getString("englishName");
            String chName = JSON.parseObject(item).getString("chineseName");
            String str = (TextUtils.isEmpty(egName) ? "" : egName) + " " + (TextUtils.isEmpty(chName) ? "" : chName);
            if (!TextUtils.isEmpty(str.replaceAll("", " "))) {
                items.add(str);
            }
        }
        return items;
    }
}
