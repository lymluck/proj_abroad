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
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ArtRankContract;
import com.smartstudy.xxd.mvp.model.RankModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class ArtRankPresenter implements ArtRankContract.Presenter {

    private ArtRankContract.View view;

    public ArtRankPresenter(ArtRankContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getArtRank(final String majorId, final String countryId, final String degreeId, int page, final int request_state) {
        RankModel.getArtRank(majorId, countryId, degreeId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, majorId);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null) {
                    String programs = object.getString("programs");
                    if (!TextUtils.isEmpty(programs)) {
                        List<SchooolRankInfo> datas = JSON.parseArray(programs, SchooolRankInfo.class);
                        if (datas != null) {
                            view.getRankSuccess(datas, false, null, request_state);
                        }
                    }
                    String countries = object.getString("countries");
                    if (!TextUtils.isEmpty(countries)) {
                        List<IdNameInfo> datas = JSON.parseArray(countries, IdNameInfo.class);
                        if (datas != null) {
                            initId(datas, countryId);
                            view.setCountries(datas);
                        }
                    }
                    String degrees = object.getString("degrees");
                    if (!TextUtils.isEmpty(degrees)) {
                        List<IdNameInfo> datas = JSON.parseArray(degrees, IdNameInfo.class);
                        if (datas != null) {
                            initId(datas, degreeId);
                            view.setDegrees(datas);
                        }
                    }
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
