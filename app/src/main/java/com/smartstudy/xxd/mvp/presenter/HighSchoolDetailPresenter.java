package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HighSchoolRankDetailInfo;
import com.smartstudy.xxd.mvp.contract.HighSchoolRankDetailContract;
import com.smartstudy.xxd.mvp.model.HighSchoolRankDetailModel;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolDetailPresenter implements HighSchoolRankDetailContract.Presenter {

    private HighSchoolRankDetailContract.View view;

    public HighSchoolDetailPresenter(HighSchoolRankDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void getHighSchoolDetail(String categoryId, String page, final int request_state) {
        HighSchoolRankDetailModel.getHighSchoolRankDetail(categoryId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<HighSchoolRankDetailInfo> data = JSON.parseArray(dataListInfo.getData(), HighSchoolRankDetailInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getHighSchoolListSuccess(data, request_state);
                    data = null;
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
        context = null;
        emptyView = null;
    }

    @Override
    public void setEmptyView(View emptyView) {
        if (emptyView != null) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            view.showEmptyView(emptyView);
            emptyView = null;
        }

    }

}

