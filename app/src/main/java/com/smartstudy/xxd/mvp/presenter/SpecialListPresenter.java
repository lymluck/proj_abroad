package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.MajorInfo;
import com.smartstudy.commonlib.entity.ProgramInfo;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SpecialListContract;
import com.smartstudy.xxd.mvp.model.SpecialListModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class SpecialListPresenter implements SpecialListContract.Presenter {

    private SpecialListContract.View view;

    public SpecialListPresenter(SpecialListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getData(String flag) {
        if ("home".equals(flag)) {
            getSpecialData();
        } else if ("bk".equals(flag)) {
            getProgramSpecial("PROG_UNDERGRADUATE");
        } else if ("yjs".equals(flag)) {
            getProgramSpecial("PROG_GRADUATE");
        }
    }

    private void getSpecialData() {
        SpecialListModel.getSpecialData(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<MajorInfo> data = JSON.parseArray(result, MajorInfo.class);
                if (data != null) {
                    view.showSpecialData(data);
                    data = null;
                }
            }
        });
    }

    private void getProgramSpecial(String typeId) {
        SpecialListModel.getProgram(typeId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<ProgramInfo> data = JSON.parseArray(result, ProgramInfo.class);
                if (data != null) {
                    view.showProgramSpecial(data);
                    data = null;
                }
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
            Button tv_refresh_btn = emptyView.findViewById(R.id.tv_refresh_btn);
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
            ImageView iv_loading = emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
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
