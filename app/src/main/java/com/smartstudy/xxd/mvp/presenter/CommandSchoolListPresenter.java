package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.CommandSchoolListContract;
import com.smartstudy.xxd.mvp.model.MyChooseSchoolModel;
import com.smartstudy.xxd.mvp.model.SpecialListModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class CommandSchoolListPresenter implements CommandSchoolListContract.Presenter {

    private CommandSchoolListContract.View view;

    public CommandSchoolListPresenter(CommandSchoolListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getSchools(int cacheType, final String id, final int page, final int request_state) {
        SpecialListModel.getCommandSchool(cacheType, id, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<SchoolInfo> data = JSON.parseArray(dataListInfo.getData(), SchoolInfo.class);
                if (data != null) {
                    view.getSchoolsSuccess(data, request_state);
                }
            }
        });
    }

    @Override
    public void add2MySchool(String match_type, int school_Id, final int position) {
        MyChooseSchoolModel.editMySchool(school_Id + "", match_type, ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.noitfyItem(position);
                view.showTip(null, "已添加至我的选校！");
            }
        });
    }

    @Override
    public void deleteMyChoose(String school_Id, final int position) {
        MyChooseSchoolModel.deleteMySchool(school_Id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.noitfyItem(position);
                view.showTip(null, "已从我的选校中移除！");
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
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
