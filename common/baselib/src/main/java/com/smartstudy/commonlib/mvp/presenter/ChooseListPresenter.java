package com.smartstudy.commonlib.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.contract.ChooseListContract;
import com.smartstudy.commonlib.mvp.model.ChooseListModel;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class ChooseListPresenter implements ChooseListContract.Presenter {


    private ChooseListContract.View view;

    public ChooseListPresenter(ChooseListContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void editMyInfo(final PersonalParamsInfo myInfo, final IdNameInfo nameInfo) {
        PersonalInfoModel.editMyInfo(myInfo, new BaseCallback<String>() {

            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.editMyInfoSuccess(result, nameInfo);
            }
        });
    }

    @Override
    public void getOptionsData(final String type, final String value, final String flag, final List<IdNameInfo> nameInfos) {
        ChooseListModel.getOptionsData(type, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (ParameterUtils.TYPE_OPTIONS_SCHOOL.equals(type)) {
                    if (ParameterUtils.EDIT_TOP_SCHOOL.equals(flag)) {
                        initData(data.getString("range"), value, nameInfos);
                    } else if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                        initData(data.getString("country"), value, nameInfos);
                    } else if (ParameterUtils.CHOOSE_APPLY_PROJ.equals(flag)) {
                        initData(data.getString("degreeType"), value, nameInfos);
                    }
                } else if (ParameterUtils.TYPE_OPTIONS_RATE.equals(type)) {
                    if (ParameterUtils.EDIT_GRADE.equals(flag)) {
                        initData(data.getString("gradeType"), value, nameInfos);
                    } else if (ParameterUtils.CHOOSE_APPLY_PROJ.equals(flag)) {
                        initData(data.getString("degreeType"), value, nameInfos);
                    }
                }
            }
        });
    }

    private void initData(String result, String value, List<IdNameInfo> nameInfos) {
        List<IdNameInfo> datas = JSON.parseArray(result, IdNameInfo.class);
        if (datas != null) {
            nameInfos.clear();
            for (IdNameInfo info : datas) {
                if (value != null && value.equals(info.getName())) {
                    info.setIs_selected(true);
                } else {
                    info.setIs_selected(false);
                }
            }
            nameInfos.addAll(datas);
            view.refreshItems();
        }
    }

    @Override
    public void doItemClick(String flag, int position, List<IdNameInfo> nameInfos) {
        PersonalParamsInfo paramsModel = new PersonalParamsInfo();
        if (ParameterUtils.EDIT_ABOARD_YEAR.equals(flag)) {
            paramsModel.setAdmissionTime(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_INTENT.equals(flag)) {
            paramsModel.setTargetCountry(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_APPLY_PROJ.equals(flag)) {
            paramsModel.setTargetDegree(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_APPLY_FEE.equals(flag)) {
            paramsModel.setBudget(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_TOP_SCHOOL.equals(flag)) {
            paramsModel.setTargetSchoolRank(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_MAJOR.equals(flag)) {
            paramsModel.setTargetMajorDirection(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_BK_SCORE.equals(flag) || ParameterUtils.EDIT_GZ_SCORE.equals(flag)
            || ParameterUtils.EDIT_CZ_SCORE.equals(flag)) {
            paramsModel.setScore(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_SX_EVENT.equals(flag)) {
            paramsModel.setActivityInternship(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_KY_EVENT.equals(flag)) {
            paramsModel.setActivityResearch(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_KW_EVENT.equals(flag)) {
            paramsModel.setActivityCommunity(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_GJ_EVENT.equals(flag)) {
            paramsModel.setActivityExchange(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_EG_SCORE.equals(flag)) {
            paramsModel.setScoreLanguage(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_GRE_SCORE.equals(flag) || ParameterUtils.EDIT_SAT_SCORE.equals(flag)) {
            paramsModel.setScoreStandard(nameInfos.get(position).getId());
        } else if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {

        }
        view.editData(nameInfos.get(position), paramsModel);
    }

    @Override
    public void showLoading(Context context, android.view.View emptyView) {
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
    public void setEmptyView(android.view.View emptyView) {
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
