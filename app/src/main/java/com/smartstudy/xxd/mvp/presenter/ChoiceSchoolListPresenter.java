package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ChoiceSchoolListInfo;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.entity.RemarkListInfo;
import com.smartstudy.xxd.mvp.contract.ChoiceSchoolListContract;
import com.smartstudy.xxd.mvp.model.ChoiceSchoolListModel;

import java.util.List;


/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolListPresenter implements ChoiceSchoolListContract.Presenter {

    private ChoiceSchoolListContract.View view;

    public ChoiceSchoolListPresenter(ChoiceSchoolListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void getChoiceSchoolList(String id, int page, final int request_state) {
        ChoiceSchoolListModel.getChoiceSchoolList(ParameterUtils.NETWORK_ELSE_CACHED, id + "", page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<ChoiceSchoolListInfo> data = JSON.parseArray(dataListInfo.getData(), ChoiceSchoolListInfo.class);
                if (data != null) {
                    view.getChoiceSchoolListSuccess(data, request_state);
                }
            }
        });
    }

    @Override
    public void getOtherStudentDetail(String id) {
        ChoiceSchoolListModel.getOtherStudentDetail(id + "", new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo = JSON.parseObject(result, OtherStudentChoiceDetailInfo.class);
                if (otherStudentChoiceDetailInfo != null) {
                    view.getOtherStudentDetailSuccess(otherStudentChoiceDetailInfo);
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
        tvErrTip.setText("目前还没有人选校");
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }


}

