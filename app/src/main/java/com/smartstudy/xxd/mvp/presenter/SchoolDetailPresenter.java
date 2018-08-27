package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.SchoolDegreeInfo;
import com.smartstudy.xxd.entity.SchoolIntroInfo;
import com.smartstudy.xxd.mvp.contract.SchoolDetailContract;
import com.smartstudy.xxd.mvp.model.MyChooseSchoolModel;
import com.smartstudy.xxd.mvp.model.SchoolModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class SchoolDetailPresenter implements SchoolDetailContract.Presenter {

    private SchoolDetailContract.View view;

    public SchoolDetailPresenter(SchoolDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void getSchoolIntro(String schoolId) {
        SchoolModel.getSchoolIntro(schoolId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                SchoolIntroInfo info = JSON.parseObject(result, SchoolIntroInfo.class);
                if (info != null) {
                    view.showInfo(info);
                }
            }
        });
    }

    @Override
    public void getSchoolDetail(final String schoolId) {
        SchoolModel.getSchoolInfo(schoolId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                JSONObject school = data.getJSONObject("school");
                if (school != null) {
                    String intro = school.getString("textIntroduction");
                    String countryId = school.getString("countryId");
                    boolean selected = school.getBooleanValue("selected");
                    view.showIntro(intro, countryId, selected);
                }
                List<NewsInfo> news = new ArrayList<>();
                JSONObject objNews = data.getJSONObject("news");
                if (objNews != null) {
                    news.addAll(JSON.parseArray(objNews.getString("data"), NewsInfo.class));
                }
                for (NewsInfo info : news) {
                    JSONArray arr = JSON.parseArray(info.getTags());
                    if (arr != null) {
                        int len = arr.size();
                        if (len > 0) {
                            for (int i = 0; i < len; i++) {
                                info.setTags(null);
                                JSONObject obj = arr.getJSONObject(i);
                                if ("stage".equals(obj.getString("type"))) {
                                    info.setTags(obj.getString("name"));
                                    break;
                                }
                            }
                        } else {
                            info.setTags(null);
                        }
                    }
                    view.showNews(news);
                }

                List<QuestionInfo> qa = new ArrayList<>();
                JSONObject objQa = data.getJSONObject("questions");
                if (objQa != null) {
                    Log.w("kim","--->"+objQa.getString("data"));
                    qa.addAll(JSON.parseArray(objQa.getString("data"), QuestionInfo.class));
                }
                view.showQa(qa);
                view.showBk(handleApplication(data.getJSONObject("undergraduateApplication"), false));
                view.showYjs(handleApplication(data.getJSONObject("graduateApplication"), false));
                view.showSia(handleApplication(data.getJSONObject("siaInfo"), true));

            }
        });
    }

    @Override
    public void add2MySchool(String match_type, String school_Id) {
        MyChooseSchoolModel.editMySchool(school_Id + "", match_type, ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.addSuccess();
                view.showTip(null, "已添加至我的选校！");
            }
        });
    }

    @Override
    public void deleteMyChoose(String school_Id) {
        MyChooseSchoolModel.deleteMySchool(school_Id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.delSuccess();
                view.showTip(null, "已从我的选校中移除！");
            }
        });
    }

    private List<SchoolDegreeInfo> handleApplication(JSONObject object, boolean isSia) {
        List<SchoolDegreeInfo> data = new ArrayList<>();
        if (object != null) {
            SchoolDegreeInfo row1 = new SchoolDegreeInfo();
            SchoolDegreeInfo row2 = new SchoolDegreeInfo();
            SchoolDegreeInfo row3 = new SchoolDegreeInfo();
            SchoolDegreeInfo row4 = new SchoolDegreeInfo();
            SchoolDegreeInfo row5 = new SchoolDegreeInfo();
            SchoolDegreeInfo row6 = new SchoolDegreeInfo();
            if (!isSia) {
                String adminssionRate = object.getString("adminssionRate");
                if (!TextUtils.isEmpty(adminssionRate)) {
                    row1.setLeftName("录取率");
                    row1.setLeftValue(adminssionRate);
                }
                String applicationStudentAmount = object.getString("applicationStudentAmount");
                if (!TextUtils.isEmpty(applicationStudentAmount)) {
                    row2.setLeftName("申请人数");
                    row2.setLeftValue(applicationStudentAmount);
                }
                String timeApplicationDeadline = object.getString("timeApplicationDeadline");
                if (!TextUtils.isEmpty(timeApplicationDeadline)) {
                    row3.setLeftName("申请截止时间");
                    row3.setLeftValue(timeApplicationDeadline);
                }
            } else {
                String applicationStudentAmount = object.getString("admissionRateSia");
                if (!TextUtils.isEmpty(applicationStudentAmount)) {
                    applicationStudentAmount = (int) (Double.parseDouble(applicationStudentAmount) * 100) + "%";
                    row1.setLeftName("SIA录取率");
                    row1.setLeftValue(applicationStudentAmount);
                }
                String adminssionRate = object.getString("admissionRate");
                if (!TextUtils.isEmpty(adminssionRate)) {
                    adminssionRate = (int) (Double.parseDouble(adminssionRate) * 100) + "%";
                    row2.setLeftName("平均录取率");
                    row2.setLeftValue(adminssionRate);
                }
                String timeApplicationDeadline = object.getString("difficultyName");
                if (!TextUtils.isEmpty(timeApplicationDeadline)) {
                    row3.setLeftName("申请难度");
                    row3.setLeftValue(timeApplicationDeadline);
                }
            }

            String timeOffer = object.getString("timeOffer");
            if (!TextUtils.isEmpty(timeOffer)) {
                row4.setLeftName("offer发放时间");
                row4.setLeftValue(timeOffer);
            }
            String timeOfferDeadline = object.getString("timeOfferDeadline");
            if (!TextUtils.isEmpty(timeOfferDeadline)) {
                row5.setLeftName("offer最晚发放时间");
                row5.setLeftValue(timeOfferDeadline);
            }

            String feeTotal = object.getString("feeTotal");
            if (!TextUtils.isEmpty(feeTotal)) {
                row1.setRightName("总花费");
                row1.setRightValue(feeTotal);
            }
            String feeApplication = object.getString("feeApplication");
            if (!TextUtils.isEmpty(feeApplication)) {
                row2.setRightName("申请费");
                row2.setRightValue(feeApplication);
            }
            String feeTuition = object.getString("feeTuition");
            if (!TextUtils.isEmpty(feeTuition)) {
                row3.setRightName("学费");
                row3.setRightValue(feeTuition);
            }
            String feeBook = object.getString("feeBook");
            if (!TextUtils.isEmpty(feeBook)) {
                row4.setRightName("书本费");
                row4.setRightValue(feeBook);
            }
            String feeAccommodation = object.getString("feeAccommodation");
            if (!TextUtils.isEmpty(feeAccommodation)) {
                row5.setRightName("住宿费");
                row5.setRightValue(feeAccommodation);
            }
            String feeLife = object.getString("feeLife");
            if (!TextUtils.isEmpty(feeLife)) {
                row6.setRightName("生活费");
                row6.setRightValue(feeLife);
            }


            if (!TextUtils.isEmpty(row1.getLeftValue()) || !TextUtils.isEmpty(row1.getRightValue())) {
                data.add(row1);
            }
            if (!TextUtils.isEmpty(row2.getLeftValue()) || !TextUtils.isEmpty(row2.getRightValue())) {
                data.add(row2);
            }
            if (!TextUtils.isEmpty(row3.getLeftValue()) || !TextUtils.isEmpty(row3.getRightValue())) {
                data.add(row3);
            }
            if (!TextUtils.isEmpty(row4.getLeftValue()) || !TextUtils.isEmpty(row4.getRightValue())) {
                data.add(row4);
            }
            if (!TextUtils.isEmpty(row5.getLeftValue()) || !TextUtils.isEmpty(row5.getRightValue())) {
                data.add(row5);
            }
            if (!TextUtils.isEmpty(row6.getLeftValue()) || !TextUtils.isEmpty(row6.getRightValue())) {
                data.add(row6);
            }
        }

        return data;
    }

}
