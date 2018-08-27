package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SrtSchoolResultContract;
import com.smartstudy.xxd.mvp.model.MyChooseSchoolModel;

import java.util.ArrayList;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtSchoolResultPresenter implements SrtSchoolResultContract.Presenter {

    private SrtSchoolResultContract.View view;

    public SrtSchoolResultPresenter(SrtSchoolResultContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void add2MySchool(String school_Id, String match_type_id, final int position) {
        MyChooseSchoolModel.editMySchool(school_Id, match_type_id, ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
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
    public void addAll2Myschool(final ArrayList<SmartSchoolRstInfo> schoolInfo) {

        MyChooseSchoolModel.editMySchool(initData(schoolInfo), ParameterUtils.MATCH_SOURCE_AUTO, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                for (SmartSchoolRstInfo info : schoolInfo) {
                    if (!info.isSelected()) {
                        info.setSelected(true);
                    }
                }
                view.addAll2MyschoolSucc();
                view.showTip(null, "一键加入成功！");
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
    public void doShare(Intent data) {
        String countryId = data.getStringExtra("country_id");
        if (TextUtils.isEmpty(countryId)) {
            countryId = "0";
        }
        String degreeId = data.getStringExtra("degreeId");
        if (TextUtils.isEmpty(degreeId)) {
            degreeId = "0";
        }
        String topId = data.getStringExtra("topId");
        if (TextUtils.isEmpty(topId)) {
            topId = "0";
        }
        String schoolScore = data.getStringExtra("schoolScore");
        if (TextUtils.isEmpty(schoolScore)) {
            schoolScore = "0";
        }
        String egScore = data.getStringExtra("egScore");
        String toefl = "0";
        String ielts = "0";
        if (!TextUtils.isEmpty(egScore)) {
            double eg = Double.parseDouble(egScore);
            if (eg > 0) {
                if (eg > 9) {
                    toefl = egScore;
                } else {
                    ielts = egScore;
                }
            }
        }
        String sat = data.getStringExtra("sat");
        if (TextUtils.isEmpty(sat)) {
            sat = "0";
        }
        String act = data.getStringExtra("act");
        if (TextUtils.isEmpty(act)) {
            act = "0";
        }
        String gre = data.getStringExtra("gre");
        if (TextUtils.isEmpty(gre)) {
            gre = "0";
        }
        String gmat = data.getStringExtra("gmat");
        if (TextUtils.isEmpty(gmat)) {
            gmat = "0";
        }
        String url = HttpUrlUtils.getWebUrl(String.format(HttpUrlUtils.URL_RESULT_SCHOOL, countryId,
                degreeId, topId, schoolScore, toefl, ielts, sat, act, gre, gmat));
        view.goShare(url, "智能选校结果", "点击查看详细内容");
    }

    @Override
    public void doFinishDelay() {
        new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                view.doFinish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1500);
    }

    private String initData(ArrayList<SmartSchoolRstInfo> schoolInfo) {
        JSONArray arr = new JSONArray();
        JSONObject object = null;
        for (SmartSchoolRstInfo info : schoolInfo) {
            object = new JSONObject();
            object.put("schoolId", info.getSchoolId());
            object.put("matchTypeId", info.getMatch_type());
            object.put("prob", info.getProb());
            arr.add(object);
        }
        return arr.toString();
    }

    @Override
    public void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent) {
        View emptyView = mInflater.inflate(R.layout.layout_empty, parent, false);
        emptyView.findViewById(com.smartstudy.commonlib.R.id.iv_loading).setVisibility(View.GONE);
        emptyView.findViewById(com.smartstudy.commonlib.R.id.llyt_err).setVisibility(View.VISIBLE);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        tv_err_tip.setText(context.getString(R.string.no_school_tip));
        view.showEmptyView(emptyView);
    }
}
