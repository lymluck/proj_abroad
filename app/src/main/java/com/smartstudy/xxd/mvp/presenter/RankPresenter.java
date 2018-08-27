package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.RankContract;
import com.smartstudy.xxd.mvp.model.RankModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class RankPresenter implements RankContract.Presenter {

    private RankContract.View view;

    public RankPresenter(RankContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getRank(int cacheType, String countryId, final String categoryId, final int page, final int request_state) {
        RankModel.getRank(cacheType, countryId, categoryId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                String title = "";
                boolean showBtn = false;
                String meta = dataListInfo.getMeta();
                if (!TextUtils.isEmpty(meta)) {
                    JSONObject objMeta = JSON.parseObject(meta);
                    JSONObject objData = JSON.parseObject(objMeta.getString("category"));
                    if (objData != null) {
                        title = objData.getString("title");
                        showBtn = objData.getBooleanValue("isWorldRank");
                    }
                }
                JSON.parseObject(dataListInfo.getMeta());
                List<SchooolRankInfo> data = JSON.parseArray(dataListInfo.getData(), SchooolRankInfo.class);
                if (data != null) {
                    view.getRankSuccess(data, showBtn, title, request_state);
                }
            }
        });
    }

    private void initId(List<IdNameInfo> datas, String id) {
        for (IdNameInfo info : datas) {
            if (info.isSelected()) {
                id = info.getId();
            }
        }
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
            tv_refresh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //重新加载
                    view.reload();
                }
            });
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
