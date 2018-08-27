package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HomeHotRankInfo;
import com.smartstudy.xxd.mvp.contract.HomeFragmentContract;
import com.smartstudy.xxd.mvp.model.HomeFragmentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class HomeFragmentPresenter implements HomeFragmentContract.Presenter {

    private HomeFragmentContract.View view;

    public HomeFragmentPresenter(HomeFragmentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getBanners(int cacheType, final List<BannerInfo> infos) {
        HomeFragmentModel.getBanners(cacheType, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<BannerInfo> datas = JSON.parseArray(result, BannerInfo.class);
                if (datas != null) {
                    infos.clear();
                    infos.addAll(datas);
                    view.showBanner();
                    datas = null;
                }
            }
        });
    }

    @Override
    public void getHomeHot() {
        HomeFragmentModel.getHomeHot(ParameterUtils.NETWORK_ELSE_CACHED, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {
                    List<HomeHotListInfo> mDatas = new ArrayList<>();

                    List<HomeHotInfo> hotSchools = JSON.parseArray(obj.getString("hotSchools"), HomeHotInfo.class);
                    if (hotSchools != null && hotSchools.size() > 0) {
                        HomeHotListInfo hotInfo = new HomeHotListInfo();
                        hotInfo.setTypeName("热门学校");
                        hotInfo.setHotInfoList(hotSchools);
                        mDatas.add(hotInfo);
                        hotInfo = null;
                        hotSchools = null;
                    }
                    List<HomeHotInfo> subjects = JSON.parseArray(obj.getString("subjects"), HomeHotInfo.class);
                    if (subjects != null && subjects.size() > 0) {
                        view.showHotSub(subjects);
                    }
                    view.showHomeHot(mDatas);
                    mDatas = null;

                    JSONObject ranks = JSON.parseObject(obj.getString("ranks"));
                    if (ranks != null) {
                        HomeHotRankInfo world = JSON.parseObject(ranks.getString("world"), HomeHotRankInfo.class);
                        HomeHotRankInfo us = JSON.parseObject(ranks.getString("us"), HomeHotRankInfo.class);
                        HomeHotRankInfo uk = JSON.parseObject(ranks.getString("uk"), HomeHotRankInfo.class);
                        HomeHotRankInfo ca = JSON.parseObject(ranks.getString("ca"), HomeHotRankInfo.class);
                        HomeHotRankInfo au = JSON.parseObject(ranks.getString("au"), HomeHotRankInfo.class);
                        view.showHotRank(world, us, uk, ca, au);
                        world = null;
                        us = null;
                        uk = null;
                        ca = null;
                        au = null;
                        ranks = null;
                    }
                    obj = null;
                }
            }
        });
    }
}
