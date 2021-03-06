package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.mvp.model.QuestionsModel;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.RemarkListInfo;
import com.smartstudy.xxd.mvp.contract.QaListContract;
import com.smartstudy.xxd.mvp.contract.RemarkListContract;
import com.smartstudy.xxd.mvp.model.RemarkListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RemarkListPresenter implements RemarkListContract.Presenter {

    private RemarkListContract.View view;

    public RemarkListPresenter(RemarkListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getRemarkList(String id, int page, final int request_state) {
        RemarkListModel.getRemarkList(ParameterUtils.NETWORK_ELSE_CACHED, id + "", page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<RemarkListInfo> data = JSON.parseArray(dataListInfo.getData(), RemarkListInfo.class);
                if (data != null) {
                    view.getRemarkListSuccess(data, request_state);
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
        context = null;
        emptyView = null;
    }


    @Override
    public void setEmptyView(Context context, View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        ImageView ivErr = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tvErrTip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        ivErr.setVisibility(View.INVISIBLE);
        tvErrTip.setText("目前还没有人备考");
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }
}
