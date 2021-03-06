package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInitOptsInfo;

import java.util.ArrayList;
import java.util.List;

@Route("PerfectUserInfoActivity")
public class PerfectUserInfoActivity extends BaseActivity {

    private ImageView topdefault_leftbutton;
    private RecyclerView rclv_opts;
    private TextView tv_country_diff;

    private List<PersonalInitOptsInfo> optsInfoList;
    private CommonAdapter<PersonalInitOptsInfo> optsInfoCommonAdapter;
    private PersonalParamsInfo info;
    private JSONObject obj_opts;
    private List<String> lastOpts = new ArrayList<>();
    private String nowOpt = "start";
    private List<String> lastIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_user_info);
    }

    @Override
    protected void initViewAndData() {
        info = new PersonalParamsInfo();
        topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        tv_country_diff = (TextView) findViewById(R.id.tv_country_diff);
        topdefault_leftbutton.setVisibility(View.GONE);
        rclv_opts = (RecyclerView) findViewById(R.id.rclv_opts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_opts.setHasFixedSize(true);
        rclv_opts.setLayoutManager(layoutManager);
        optsInfoList = new ArrayList<>();
        optsInfoCommonAdapter = new CommonAdapter<PersonalInitOptsInfo>(this, R.layout.item_personal_opts, optsInfoList) {
            @Override
            protected void convert(ViewHolder holder, PersonalInitOptsInfo personalInitOptsInfo, int position) {
                TextView tv_opts = holder.getView(R.id.tv_opts);
                tv_opts.setText(personalInitOptsInfo.getName());
                if (personalInitOptsInfo.isSelected()) {
                    tv_opts.setBackgroundResource(R.drawable.shape_blue_radius);
                    tv_opts.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tv_opts.setBackgroundResource(R.drawable.bg_app_btn_radius_white_blue);
                    tv_opts.setTextColor(getResources().getColorStateList(R.color.text_selected_white));
                }
            }
        };
        rclv_opts.setAdapter(optsInfoCommonAdapter);
        getInitOpts();
    }

    @Override
    public void initEvent() {
        topdefault_leftbutton.setOnClickListener(this);
        tv_country_diff.setOnClickListener(this);
        optsInfoCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PersonalInitOptsInfo opts = optsInfoList.get(position);
                if (opts != null) {
                    initParams(nowOpt, opts.getId());
                    if (opts.isFinish()) {
                        editPersonalInfo(info);
                    } else {
                        topdefault_leftbutton.setVisibility(View.VISIBLE);
                        optsInfoList.clear();
                        lastOpts.add(nowOpt);
                        optsInfoList.addAll(getNext(opts.getNext()));
                        if ("step2".equals(nowOpt)) {
                            tv_country_diff.setVisibility(View.VISIBLE);
                        } else {
                            tv_country_diff.setVisibility(View.GONE);
                        }
                        optsInfoCommonAdapter.notifyDataSetChanged();
                        lastIds.add(opts.getId());
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                handleBack();
                break;
            case R.id.tv_country_diff:
                UApp.actionEvent(this, "4_A_compare_btn");
                startActivity(new Intent(PerfectUserInfoActivity.this, CompareCountryActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        int len = lastOpts.size();
        if (len > 0) {
            releaseParams(nowOpt);
            if ("step3a".equals(nowOpt) || "step3b".equals(nowOpt)) {
                tv_country_diff.setVisibility(View.VISIBLE);
            } else {
                tv_country_diff.setVisibility(View.GONE);
            }
            if (len == 1) {
                topdefault_leftbutton.setVisibility(View.GONE);
            }
            optsInfoList.clear();
            optsInfoList.addAll(toLastSelectedOpt(getNext(lastOpts.get(len - 1))));
            optsInfoCommonAdapter.notifyDataSetChanged();
            lastOpts.remove(len - 1);
        }
    }

    private void editPersonalInfo(final PersonalParamsInfo info) {
        PersonalInfoModel.editPersonalInfo(info, new BaseCallback() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onSuccess(Object result) {
                actionEvent(info);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void actionEvent(PersonalParamsInfo info) {
        if ("STUDENT".equals(info.getCustomerRoleId())) {
            UApp.actionEvent(this, "2_A_student_btn");
        } else if ("PARENTS".equals(info.getCustomerRoleId())) {
            UApp.actionEvent(this, "2_A_ parents_btn");
        } else if ("OTHER".equals(info.getCustomerRoleId())) {
            UApp.actionEvent(this, "2_A_ other_btn");
        }

        int nowYear = DateTimeUtils.getNowYear();
        if (String.valueOf(nowYear).equals(info.getAdmissionTime())) {
            UApp.actionEvent(this, "3_A_first_btn");
        } else if (String.valueOf(nowYear + 1).equals(info.getAdmissionTime())) {
            UApp.actionEvent(this, "3_A_second_btn");
        } else if (String.valueOf(nowYear + 2).equals(info.getAdmissionTime())) {
            UApp.actionEvent(this, "3_A_third_btn");
        } else if ("OTHER".equals(info.getAdmissionTime())) {
            UApp.actionEvent(this, "3_A_other_btn");
        }
        if ("COUNTRY_226".equals(info.getTargetCountry())) {
            UApp.actionEvent(this, "4_A_America_btn");
        } else if ("COUNTRY_225".equals(info.getTargetCountry())) {
            UApp.actionEvent(this, "4_A_England_btn");
        } else if ("COUNTRY_40".equals(info.getTargetCountry())) {
            UApp.actionEvent(this, "4_A_Canada_btn");
        } else if ("COUNTRY_16".equals(info.getTargetCountry())) {
            UApp.actionEvent(this, "4_A_Australia_btn");
        } else if ("OTHER".equals(info.getTargetCountry())) {
            UApp.actionEvent(this, "4_A_other_btn");
        }

        if ("DEGREE_MDT_SENIOR_HIGH_SCHOOL".equals(info.getTargetDegree())) {
            UApp.actionEvent(this, "6_A_highschool_btn");
        } else if ("DEGREE_MDT_BACHELOR".equals(info.getTargetDegree())) {
            UApp.actionEvent(this, "6_A_undergraduate_btn");
        } else if ("DEGREE_MDT_MASTER".equals(info.getTargetDegree())) {
            UApp.actionEvent(this, "6_A_graduate_btn");
        } else if ("DEGREE_MDT_OTHER".equals(info.getTargetDegree())) {
            UApp.actionEvent(this, "6_A_other_btn");
        }

        if ("30".equals(info.getTargetSchoolRank())) {
            UApp.actionEvent(this, "7_A_first_btn");
        } else if ("50".equals(info.getTargetSchoolRank())) {
            UApp.actionEvent(this, "7_A_second_btn");
        } else if ("100".equals(info.getTargetSchoolRank()) || "80".equals(info.getTargetSchoolRank())) {
            UApp.actionEvent(this, "7_A_third_btn");
        } else if ("OTHER".equals(info.getTargetSchoolRank())) {
            UApp.actionEvent(this, "7_A_other_btn");
        }
    }

    private void getInitOpts() {
        PersonalInfoModel.getInitOptions(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void onSuccess(String result) {
                obj_opts = JSON.parseObject(result);
                optsInfoList.clear();
                optsInfoList.addAll(getNext(nowOpt));
                optsInfoCommonAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<PersonalInitOptsInfo> getNext(String next) {
        nowOpt = next;
        return JSON.parseArray(obj_opts.getJSONObject(next).getString("options"), PersonalInitOptsInfo.class);
    }

    private List<PersonalInitOptsInfo> toLastSelectedOpt(List<PersonalInitOptsInfo> list) {
        int len = lastIds.size();
        for (PersonalInitOptsInfo initOptsInfo : list) {
            if (initOptsInfo.getId().equals(lastIds.get(len - 1))) {
                initOptsInfo.setSelected(true);
            } else {
                initOptsInfo.setSelected(false);
            }
        }
        lastIds.remove(len - 1);
        return list;
    }

    private void initParams(String opt, String param) {
        if ("start".equals(opt)) {
            info.setCustomerRoleId(param);
            UApp.actionEvent(this, "2_B_user_identity");
        } else if ("step1".equals(opt)) {
            info.setAdmissionTime(param);
            UApp.actionEvent(this, "3_B_abroad_time");
        } else if ("step2".equals(opt)) {
            UApp.actionEvent(this, "4_B_abroad_countries");
            info.setTargetCountry(param);
        } else if ("step3a".equals(opt) || "step3b".equals(opt)) {
            UApp.actionEvent(this, "6_B_programs");
            info.setTargetDegree(param);
            if (param.equals("DEGREE_MDT_SENIOR_HIGH_SCHOOL")) {
                SPCacheUtils.put("project_name", "高中");
            } else if (param.equals("DEGREE_MDT_BACHELOR")) {
                SPCacheUtils.put("project_name", "本科");
            } else if (param.equals("DEGREE_MDT_MASTER")) {
                SPCacheUtils.put("project_name", "研究生");
            } else {
                SPCacheUtils.put("project_name", "其他");
            }
        } else if ("step4a".equals(opt) || "step4b".equals(opt)) {
            UApp.actionEvent(this, "7_B_school_rank");
            info.setTargetSchoolRank(param);
        } else if ("step5".equals(opt)) {
            info.setTargetMajorDirection(param);
        } else if ("step6".equals(opt)) {
            info.setBudget(param);
        }
    }

    private void releaseParams(String opt) {
        if ("start".equals(opt)) {
            info.setCustomerRoleId(null);
        } else if ("step1".equals(opt)) {
            info.setAdmissionTime(null);
        } else if ("step2".equals(opt)) {
            info.setTargetCountry(null);
        } else if ("step3a".equals(opt) || "step3b".equals(opt)) {
            info.setTargetDegree(null);
            SPCacheUtils.put("project_name", "");
        } else if ("step4a".equals(opt) || "step4b".equals(opt)) {
            info.setTargetSchoolRank(null);
        } else if ("step5".equals(opt)) {
            info.setTargetMajorDirection(null);
        } else if ("step6".equals(opt)) {
            info.setBudget(null);
        }
    }
}
