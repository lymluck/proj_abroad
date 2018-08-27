package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.CommentInfo;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.CourseCommentContract;
import com.smartstudy.xxd.mvp.model.CourseModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class CourseCommentPresenter implements CourseCommentContract.Presenter {

    private CourseCommentContract.View view;

    public CourseCommentPresenter(CourseCommentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getComments(String courseId, int page, final int request_state) {
        CourseModel.getCourseComments(courseId, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                view.showCommentCount(dataListInfo.getPagination().getCount() + "");
                JSONObject meta = JSON.parseObject(dataListInfo.getMeta());
                view.isComment(meta.getBooleanValue("commented"), meta.getBooleanValue("played"));
                List<CommentInfo> data = JSON.parseArray(dataListInfo.getData(), CommentInfo.class);
                if (data != null) {
                    view.showComments(data, request_state);
                }
            }
        });
    }

    @Override
    public void CommentCourse(String courseId, String rate, String comment) {
        CourseModel.commentCourse(courseId, rate, comment, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.CommentSuccess();
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
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv_loading.getLayoutParams();
            params.topMargin = DensityUtils.dip2px(50);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            iv_loading.setLayoutParams(params);
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
    public void hideEmptyView(View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
