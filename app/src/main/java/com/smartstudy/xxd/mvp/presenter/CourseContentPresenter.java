package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseContentInfo;
import com.smartstudy.xxd.mvp.contract.CourseContentContract;
import com.smartstudy.xxd.mvp.model.CourseModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class CourseContentPresenter implements CourseContentContract.Presenter {

    private CourseContentContract.View view;

    public CourseContentPresenter(CourseContentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getContents(String courseId) {
        CourseModel.getCourseContent(courseId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<CourseContentInfo> data = JSON.parseArray(result, CourseContentInfo.class);
                if (data != null) {
                    view.showContent(data);
                }
            }
        });
    }

    @Override
    public void getPlayUrl(String courseId, String id, final String title, final long lastTime, final long duration) {
        CourseModel.playCourse(courseId, id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject url_obj = JSON.parseObject(result);
//                ArrayList<VideoijkInfo> data = new ArrayList<>();
//                VideoijkInfo lc = new VideoijkInfo();
//                lc.setStream("流畅");
//                lc.setUrl(url_obj.getString("smooth"));
//                data.add(lc);
//                VideoijkInfo bq = new VideoijkInfo();
//                bq.setStream("标清");
//                bq.setUrl(url_obj.getString("shd"));
//                data.add(bq);
//                VideoijkInfo gq = new VideoijkInfo();
//                gq.setStream("高清");
//                gq.setUrl(url_obj.getString("hd"));
//                data.add(gq);
                view.showUrl(url_obj.getString("hd"), title, url_obj.getString("sectionId"), lastTime, duration);
            }
        });
    }

    @Override
    public void recordPlayUrl(String courseId, String id, String playTime, String playDuration) {
        CourseModel.recordPlay(courseId, id, playTime, playDuration, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.recordSuccess();
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
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv_loading.getLayoutParams();
            params.topMargin = DensityUtils.dip2px(120);
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
    public void onDetachView() {
        view = null;
    }
}
