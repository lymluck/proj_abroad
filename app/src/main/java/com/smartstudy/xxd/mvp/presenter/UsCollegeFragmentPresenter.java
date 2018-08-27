package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.ColumnListItemInfo;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;
import com.smartstudy.xxd.entity.HotRankInfo;
import com.smartstudy.xxd.entity.RecommendedCourse;
import com.smartstudy.xxd.mvp.contract.UsCollegeFragmentContract;
import com.smartstudy.xxd.mvp.model.HomeHotModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class UsCollegeFragmentPresenter implements UsCollegeFragmentContract.Presenter {

    private UsCollegeFragmentContract.View view;

    public UsCollegeFragmentPresenter(UsCollegeFragmentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getHomeHot() {
        HomeHotModel.getUsCollegeHomeHot(ParameterUtils.NETWORK_ELSE_CACHED, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
                view.hideLoading();
            }

            @Override
            public void onSuccess(String result) {
                view.hideLoading();
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {

                    //推荐课程
                    RecommendedCourse recommendedCourse = JSON.parseObject(obj.getString("recommendedCourse"),
                        RecommendedCourse.class);
                    if (recommendedCourse != null) {
                        view.showRecommendedCourse(recommendedCourse);
                    }
                    // 首页热门排名
                    List<HotRankInfo> homeHotRankInfoList = JSON.parseArray(obj.getString("hotRanks"),
                        HotRankInfo.class);
                    if (homeHotRankInfoList != null) {
                        view.showHotRank(homeHotRankInfoList);
                    }
                    // 选校
                    List<HomeHotListInfo> mDatas = new ArrayList<>();
                    List<HomeHotInfo> hotSchools = JSON.parseArray(obj.getString("hotSchools"),
                        HomeHotInfo.class);
                    if (hotSchools != null && hotSchools.size() > 0) {
                        HomeHotListInfo hotInfo = new HomeHotListInfo();
                        hotInfo.setTypeName("浏览热度选校");
                        hotInfo.setHotInfoList(hotSchools);
                        mDatas.add(hotInfo);
                        hotInfo = null;
                        hotSchools = null;
                    }
                    List<HomeHotInfo> hotRecentSchools = JSON.parseArray(obj.getString("mostSelectedSchools"),
                        HomeHotInfo.class);
                    if (hotRecentSchools != null && hotRecentSchools.size() > 0) {
                        HomeHotListInfo hotInfo = new HomeHotListInfo();
                        hotInfo.setTypeName("选校热度选校");
                        hotInfo.setHotInfoList(hotRecentSchools);
                        mDatas.add(hotInfo);
                        hotInfo = null;
                        hotRecentSchools = null;
                    }
                    view.showHomeHot(mDatas);
                    mDatas = null;
                    // 热门专业选校
                    if (obj.containsKey("hotMajors")) {
                        List<HomeHotProgramInfo> hotMajors = JSON.parseArray(obj.getString("hotMajors"),
                            HomeHotProgramInfo.class);
                        if (hotMajors != null && hotMajors.size() > 0) {
                            view.showHotMajor(hotMajors);
                        }
                    }
                    // 专栏
                    if (obj.containsKey("columnNews")) {
                        List<ColumnListItemInfo> columnNews = JSON.parseArray(obj.getString("columnNews"),
                            ColumnListItemInfo.class);
                        if (columnNews != null && columnNews.size() > 0) {
                            view.showColumnNews(columnNews);
                        }
                    }
                    // 最近浏览
                    List<HomeHotInfo> subjects = JSON.parseArray(obj.getString("recentViewedSchools"),
                        HomeHotInfo.class);
                    if (subjects != null && subjects.size() > 0) {
                        view.showRecentView(subjects);
                    }
                    obj = null;
                }
            }
        });
    }

    @Override
    public void deleCourse(String productId) {
        HomeHotModel.deleteCourse(productId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                //推荐课程
                RecommendedCourse recommendedCourse = JSON.parseObject(result, RecommendedCourse.class);
                view.showRecommendedCourse(recommendedCourse);
            }
        });
    }
}
