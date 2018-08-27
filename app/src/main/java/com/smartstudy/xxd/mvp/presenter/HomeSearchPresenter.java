package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.entity.HomeSearchInfo;
import com.smartstudy.xxd.entity.HomeSearchListInfo;
import com.smartstudy.xxd.mvp.contract.HomeSearchContract;
import com.smartstudy.xxd.mvp.model.HomeSearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class HomeSearchPresenter implements HomeSearchContract.Presenter {

    private HomeSearchContract.View view;

    public HomeSearchPresenter(HomeSearchContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void setEmptyView(LayoutInflater mInflater, final Context context, ViewGroup parent) {
        View emptyView = mInflater.inflate(com.smartstudy.commonlib.R.layout.layout_empty, parent, false);
        emptyView.findViewById(com.smartstudy.commonlib.R.id.iv_loading).setVisibility(View.GONE);
        emptyView.findViewById(com.smartstudy.commonlib.R.id.llyt_err).setVisibility(View.VISIBLE);
        ImageView iv_err = (ImageView) emptyView.findViewById(com.smartstudy.commonlib.R.id.iv_err);
        Button tv_qa_btn = (Button) emptyView.findViewById(com.smartstudy.commonlib.R.id.tv_qa_btn);
        TextView tv_err_tip = (TextView) emptyView.findViewById(com.smartstudy.commonlib.R.id.tv_err_tip);
        tv_err_tip.setText(context.getString(com.smartstudy.commonlib.R.string.no_search_tip));
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(com.smartstudy.commonlib.R.id.llyt_err).setVisibility(View.VISIBLE);
            tv_qa_btn.setVisibility(View.GONE);
            iv_err.setImageResource(com.smartstudy.commonlib.R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(com.smartstudy.commonlib.R.string.no_net_tip));
        }
        view.showEmptyView(emptyView);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getResults(final String keyword) {
        HomeSearchModel.getResults(keyword, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                JSONObject total = obj.getJSONObject("total");
                List<HomeSearchListInfo> mDatas = new ArrayList<>();
                if (total != null) {
                    //大学院校
                    int schoolTotal = total.getIntValue("schools");
                    if (schoolTotal > 0) {
                        HomeSearchListInfo schoolInfo = new HomeSearchListInfo();
                        schoolInfo.setTypeName("大学院校");
                        schoolInfo.setKeyword(keyword);
                        List<HomeSearchInfo> datas = JSON.parseArray(obj.getString("schools"), HomeSearchInfo.class);
                        schoolInfo.setDatas(datas);
                        schoolInfo.setTypeTotal((schoolTotal - datas.size()) + "");
                        mDatas.add(schoolInfo);
                        schoolInfo = null;
                        datas = null;
                    }
                    //高中院校
                    int highSchoolTotal = total.getIntValue("highschools");
                    if (highSchoolTotal > 0) {
                        HomeSearchListInfo schoolInfo = new HomeSearchListInfo();
                        schoolInfo.setTypeName("高中院校");
                        schoolInfo.setKeyword(keyword);
                        List<HomeSearchInfo> datas = JSON.parseArray(obj.getString("highschools"), HomeSearchInfo.class);
                        schoolInfo.setDatas(datas);
                        schoolInfo.setTypeTotal((highSchoolTotal - datas.size()) + "");
                        mDatas.add(schoolInfo);
                        schoolInfo = null;
                        datas = null;
                    }
                    //课程
                    int courseTotal = total.getIntValue("courses");
                    if (courseTotal > 0) {
                        HomeSearchListInfo courseInfo = new HomeSearchListInfo();
                        courseInfo.setTypeName("课程");
                        courseInfo.setKeyword(keyword);
                        List<HomeSearchInfo> datas = JSON.parseArray(obj.getString("courses"), HomeSearchInfo.class);
                        courseInfo.setDatas(datas);
                        courseInfo.setTypeTotal((courseTotal - datas.size()) + "");
                        mDatas.add(courseInfo);
                        courseInfo = null;
                        datas = null;
                    }
                    //问答
                    int qaTotal = total.getIntValue("questions");
                    if (qaTotal > 0) {
                        HomeSearchListInfo qaInfo = new HomeSearchListInfo();
                        qaInfo.setTypeName("问答");
                        qaInfo.setKeyword(keyword);
                        List<HomeSearchInfo> datas = JSON.parseArray(obj.getString("questions"), HomeSearchInfo.class);
                        qaInfo.setDatas(datas);
                        qaInfo.setTypeTotal((qaTotal - datas.size()) + "");
                        mDatas.add(qaInfo);
                        qaInfo = null;
                        datas = null;
                    }
                    //资讯
                    int newsTotal = total.getIntValue("news");
                    if (newsTotal > 0) {
                        HomeSearchListInfo newsInfo = new HomeSearchListInfo();
                        newsInfo.setTypeName("资讯");
                        newsInfo.setKeyword(keyword);
                        List<HomeSearchInfo> infos = new ArrayList<>();
                        HomeSearchInfo searchInfo;
                        List<NewsInfo> data = JSON.parseArray(obj.getString("news"), NewsInfo.class);
                        if (data != null) {
                            for (NewsInfo info : data) {
                                searchInfo = new HomeSearchInfo();
                                searchInfo.setTitle(info.getTitle());
                                searchInfo.setVisitCount(info.getVisitCount());
                                searchInfo.setId(info.getId());
                                JSONArray arr = JSON.parseArray(info.getTags());
                                if (arr != null) {
                                    int len = arr.size();
                                    if (len > 0) {
                                        for (int i = 0; i < len; i++) {
                                            JSONObject tag = arr.getJSONObject(i);
                                            if ("stage".equals(tag.getString("type"))) {
                                                searchInfo.setTagName(tag.getString("name"));
                                                break;
                                            }
                                        }
                                    } else {
                                        info.setTags(null);
                                    }
                                }
                                infos.add(searchInfo);
                                searchInfo = null;
                            }
                        }
                        newsInfo.setDatas(infos);
                        newsInfo.setTypeTotal((newsTotal - infos.size()) + "");
                        mDatas.add(newsInfo);
                        newsInfo = null;
                    }
                }
                view.showResult(mDatas);
            }
        });
    }
}
