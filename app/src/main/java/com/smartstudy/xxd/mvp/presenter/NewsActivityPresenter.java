package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.NewsActivityContract;
import com.smartstudy.xxd.mvp.model.NewsListModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class NewsActivityPresenter implements NewsActivityContract.Presenter {

    private NewsActivityContract.View view;

    public NewsActivityPresenter(NewsActivityContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getVisaNews(String countryId, int page, final int request_state) {
        NewsListModel.getVisaNews(countryId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<NewsInfo> data = JSON.parseArray(dataListInfo.getData(), NewsInfo.class);
                if (data != null) {
                    for (NewsInfo info : data) {
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
                    }
                    view.getNewsSuccess(data, request_state);
                }
            }
        });
    }

    @Override
    public void getSchoolNews(String schoolId, int page, final int request_state) {
        NewsListModel.getSchoolNews(schoolId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<NewsInfo> data = JSON.parseArray(dataListInfo.getData(), NewsInfo.class);
                if (data != null) {
                    for (NewsInfo info : data) {
                        JSONArray arr = JSON.parseArray(info.getTags());
                        if (arr != null) {
                            int len = arr.size();
                            if (len > 0) {
                                for (int i = 0; i < len; i++) {
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
                    }
                    view.getNewsSuccess(data, request_state);
                }
            }
        });
    }

    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
    }

    @Override
    public void setEmptyView(View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        view.showEmptyView(emptyView);
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
