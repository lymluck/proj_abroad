package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.RankTypeContract;
import com.smartstudy.xxd.mvp.model.RankModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class RankTypePresenter implements RankTypeContract.Presenter {

    private RankTypeContract.View view;

    public RankTypePresenter(RankTypeContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getRankType() {
        RankModel.getRankType(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<RankTypeInfo> data = new ArrayList<>();
                List<RankTypeInfo> schoolRanks = JSON.parseArray(JSON.parseObject(result).getString("integratedRankings"), RankTypeInfo.class);
                int schooLen = schoolRanks.size();
                if (schoolRanks != null && schooLen > 0) {
                    schoolRanks.get(0).setTop(true);
                    schoolRanks.get(schooLen - 1).setBottom(true);
                    data.addAll(schoolRanks);
                }
                List<RankTypeInfo> majorRanks = JSON.parseArray(JSON.parseObject(result).getString("majorRankings"), RankTypeInfo.class);
                int majorLen = majorRanks.size();
                if (majorRanks != null && majorLen > 0) {
                    majorRanks.get(0).setTop(true);
                    majorRanks.get(majorLen - 1).setBottom(true);
                    data.addAll(majorRanks);
                }
                view.showRankTypes(data, schoolRanks.size());
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
        emptyView = null;
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
