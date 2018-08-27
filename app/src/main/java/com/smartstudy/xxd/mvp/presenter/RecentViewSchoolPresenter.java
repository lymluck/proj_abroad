package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.RecentViewSchoolInfo;
import com.smartstudy.xxd.mvp.contract.RankContract;
import com.smartstudy.xxd.mvp.contract.RecentViewSchoolContract;
import com.smartstudy.xxd.mvp.model.RankModel;
import com.smartstudy.xxd.mvp.model.RecentViewSchoolModel;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RecentViewSchoolPresenter implements RecentViewSchoolContract.Presenter {

    private RecentViewSchoolContract.View view;

    public RecentViewSchoolPresenter(RecentViewSchoolContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void getRecentSchoolList(int page, final int request_state) {

        RecentViewSchoolModel.getRecentViewSchool(page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<RecentViewSchoolInfo> data = JSON.parseArray(dataListInfo.getData(), RecentViewSchoolInfo.class);
                view.getRecentSuccess(data, request_state);
            }
        });
    }

    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
            Button tv_refresh_btn = (Button) emptyView.findViewById(R.id.tv_refresh_btn);
            tv_refresh_btn.setVisibility(View.VISIBLE);
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
    }

    @Override
    public void setEmptyView(Context context, View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        view.showEmptyView(emptyView);
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
