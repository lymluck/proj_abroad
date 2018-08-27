package com.smartstudy.xxd.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.tools.IeltsScoreFilter;
import com.smartstudy.commonlib.base.tools.ToeflScoreFilter;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtTestRateContract;
import com.smartstudy.xxd.mvp.presenter.SrtTestRatePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("rateTest")
public class SrtRateTestActivity extends UIActivity implements SrtTestRateContract.View {

    private TextView tvapplyproject;
    private LinearLayout llytapplyproject;
    private TextView tvgrade;
    private LinearLayout llytgrade;
    private EditText et_school_score;
    private EditText et_eg;
    private EditText et_test;
    private TextView tvhastest;
    private TextView tv_chineseName;
    private TextView tv_englishName;
    private ImageView iv_logo;
    private Spinner spinner_eg;
    private Spinner spinner_test;

    private String schoolId;
    private String degreeId;
    private String gradeId;
    private SrtTestRateContract.Presenter testP;
    private ProgressDialog mProgressDialog;
    private Bundle bundle;
    private InputFilter[] toeflFilter;
    private InputFilter[] ieltsFilter;
    private ArrayAdapter adapterEg;
    private ArrayAdapter adapterTest;
    private List<String> testList;
    private String lastProj;
    private int nowEgPos;
    private int nowTestPos;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusColor(R.color.main_bg);//修改状态栏颜色
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_rate_test);
    }

    @Override
    protected void initViewAndData() {
        toeflFilter = new InputFilter[]{new ToeflScoreFilter()};
        ieltsFilter = new InputFilter[]{new IeltsScoreFilter()};
        //修改标题栏颜色
        findViewById(R.id.top_rate_test).setBackgroundColor(getResources().getColor(R.color.main_bg));
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("录取率测试");
        this.tvhastest = (TextView) findViewById(R.id.tv_has_test);
        this.et_test = (EditText) findViewById(R.id.et_test);
        et_test.setEnabled(false);
        this.et_eg = (EditText) findViewById(R.id.et_eg);
        et_eg.setEnabled(false);
        this.spinner_eg = (Spinner) findViewById(R.id.spinner_eg);
        this.spinner_test = (Spinner) findViewById(R.id.spinner_test);
        //将可选内容与ArrayAdapter连接起来
        adapterEg = ArrayAdapter.createFromResource(this, R.array.egscore, R.layout.spinner_item);
        testList = new ArrayList<>();
        testList.addAll(Arrays.asList(getResources().getStringArray(R.array.bkscore)));
        adapterTest = new ArrayAdapter(this, R.layout.spinner_item, testList);
        //设置下拉列表的风格
        adapterEg.setDropDownViewResource(R.layout.dropdown_stytle);
        adapterTest.setDropDownViewResource(R.layout.dropdown_stytle);
        //将adapter 添加到spinner中
        spinner_eg.setAdapter(adapterEg);
        spinner_test.setAdapter(adapterTest);
        this.et_school_score = (EditText) findViewById(R.id.et_school_score);
        this.llytgrade = (LinearLayout) findViewById(R.id.llyt_grade);
        this.tvgrade = (TextView) findViewById(R.id.tv_grade);
        this.llytapplyproject = (LinearLayout) findViewById(R.id.llyt_apply_project);
        this.tvapplyproject = (TextView) findViewById(R.id.tv_apply_project);
        iv_logo = (ImageView) findViewById(R.id.iv_school_logo);
        tv_chineseName = (TextView) findViewById(R.id.tv_chineseName);
        tv_englishName = (TextView) findViewById(R.id.tv_englishName);
        TextView tv_btn_reset = (TextView) findViewById(R.id.tv_btn_reset);
        tv_btn_reset.setOnClickListener(this);
        tvhastest.setText(String.format(getString(R.string.has_test), "0"));
        new SrtTestRatePresenter(this);
        bundle = new Bundle();
        Bundle data = getIntent().getExtras();
        if (data != null) {
            schoolId = data.getString("schoolId");
            String countryId = data.getString("countryId");
            if ("COUNTRY_226".equals(countryId)) {
                findViewById(R.id.llyt_test).setVisibility(View.VISIBLE);
                findViewById(R.id.view_test_line).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.llyt_test).setVisibility(View.GONE);
                findViewById(R.id.view_test_line).setVisibility(View.GONE);
            }
            flag = data.getString("flag");
            if (ParameterUtils.HOME_RATE_FLAG.equals(flag)) {
                addActivity(this);
                tv_btn_reset.setVisibility(View.VISIBLE);
            }
        }
        testP.getSchoolInfo(schoolId);
        testP.getHasTestNum();
    }

    @Override
    public void initEvent() {
        llytapplyproject.setOnClickListener(this);
        llytgrade.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_choose_school).setOnClickListener(this);
        spinner_eg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (nowEgPos != position) {
                    String text = (String) adapterEg.getItem(position);
                    nowEgPos = position;
                    et_eg.setText("");
                    if (position != 0) {
                        et_eg.setEnabled(true);
                        switch (text) {
                            case "TOEFL成绩":
                                et_eg.setHint(getString(R.string.hint_toefl));
                                et_eg.setFilters(toeflFilter);
                                break;
                            case "IELTS成绩":
                                et_eg.setHint(getString(R.string.hint_ielts));
                                et_eg.setFilters(ieltsFilter);
                                break;
                            default:
                                break;
                        }
                    } else {
                        et_eg.setEnabled(false);
                        et_eg.setHint(getString(R.string.choose_eg));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (nowTestPos != position) {
                    String text = (String) adapterTest.getItem(position);
                    nowTestPos = position;
                    et_test.setText("");
                    if (position != 0) {
                        et_test.setEnabled(true);
                        switch (text) {
                            case "SAT成绩":
                                et_test.setHint(getString(R.string.hint_sat));
                                break;
                            case "ACT成绩":
                                et_test.setHint(getString(R.string.hint_act));
                                break;
                            case "GRE成绩":
                                et_test.setHint(getString(R.string.hint_gre));
                                break;
                            case "GMAT成绩":
                                et_test.setHint(getString(R.string.hint_gmat));
                                break;
                            default:
                                break;
                        }
                    } else {
                        et_test.setEnabled(false);
                        et_test.setHint(getString(R.string.choose_test));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (ParameterUtils.HOME_RATE_FLAG.equals(flag)) {
            finishAll();
        } else {
            finish();
        }
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                if (ParameterUtils.HOME_RATE_FLAG.equals(flag)) {
                    finishAll();
                } else {
                    finish();
                }
                break;
            case R.id.tv_btn_reset:
                finish();
                break;
            case R.id.llyt_apply_project:
                Intent toChooseProj = new Intent(this, ChooseListActivity.class);
                toChooseProj.putExtra("value", tvapplyproject.getText());
                toChooseProj.putExtra("ischange", false);
                toChooseProj.putExtra("from", ParameterUtils.TYPE_OPTIONS_RATE);
                toChooseProj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.CHOOSE_APPLY_PROJ);
                toChooseProj.putExtra("title", "选择申请项目");
                startActivityForResult(toChooseProj, ParameterUtils.REQUEST_CODE_SMART_CHOOSE);
                break;
            case R.id.llyt_grade:
                Intent toChooseGrade = new Intent(this, ChooseListActivity.class);
                toChooseGrade.putExtra("value", tvgrade.getText());
                toChooseGrade.putExtra("ischange", false);
                toChooseGrade.putExtra("from", ParameterUtils.TYPE_OPTIONS_RATE);
                toChooseGrade.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GRADE);
                toChooseGrade.putExtra("title", "选择当前年级");
                startActivityForResult(toChooseGrade, ParameterUtils.REQUEST_CODE_SMART_CHOOSE);
                break;
            case R.id.btn_choose_school:
                if (degreeId == null) {
                    ToastUtils.showToast(this, "请选择申请项目！");
                    break;
                }
                if ((TextUtils.isEmpty(et_test.getText()) || "0".equals(et_test.getText()))
                        && (TextUtils.isEmpty(et_eg.getText().toString()) || "0".equals(et_eg.getText().toString()) || ".".equals(et_eg.getText().toString()))) {
                    if (findViewById(R.id.llyt_test).getVisibility() == View.VISIBLE) {
                        ToastUtils.showToast(this, "请至少提供语言成绩或标准化成绩中的一项！");
                    } else {
                        ToastUtils.showToast(this, "请输入语言成绩！");
                    }
                    break;
                }

                SmartChooseInfo info = new SmartChooseInfo();
                info.setSchoolId(schoolId);
                info.setProjId(degreeId);
                info.setGradeId(gradeId);
                if (!TextUtils.isEmpty(et_eg.getText())) {
                    double egScore = Double.parseDouble(et_eg.getText().toString());
                    String text = spinner_eg.getSelectedItem().toString();
                    if (egScore > 0.0) {
                        switch (text) {
                            case "TOEFL成绩":
                                info.setToeflScore((int) egScore);
                                info.setIeltsScore(-1);
                                break;
                            case "IELTS成绩":
                                info.setToeflScore(-1);
                                info.setIeltsScore(egScore);
                                break;
                            default:
                                break;
                        }
                    } else {
                        ToastUtils.showToast(this, text + "需大于0！");
                        return;
                    }
                }
                if (!TextUtils.isEmpty(et_school_score.getText())) {
                    int schoolScore = Integer.parseInt(et_school_score.getText().toString(), 10);
                    if (schoolScore <= 0 || schoolScore > 100) {
                        ToastUtils.showToast(this, "输入有误，在校成绩需大于0，为百分制！");
                        return;
                    }
                    info.setSchoolScore(schoolScore);
                }
                if (!TextUtils.isEmpty(et_test.getText())) {
                    int score = Integer.parseInt(et_test.getText().toString(), 10);
                    String text = spinner_test.getSelectedItem().toString();
                    switch (text) {
                        case "SAT成绩":
                            if (score <= 0 || score > 1600) {
                                ToastUtils.showToast(this, "输入有误，SAT成绩需大于0，小于1600！");
                                return;
                            }
                            info.setSatScore(score);
                            info.setActScore(-1);
                            info.setGreScore(-1);
                            info.setGmatScore(-1);
                            break;
                        case "ACT成绩":
                            if (score <= 0 || score > 36) {
                                ToastUtils.showToast(this, "输入有误，ACT成绩需大于0，小于36！");
                                return;
                            }
                            info.setSatScore(-1);
                            info.setActScore(score);
                            info.setGreScore(-1);
                            info.setGmatScore(-1);
                            break;
                        case "GRE成绩":
                            if (score <= 0 || score > 346) {
                                ToastUtils.showToast(this, "输入有误，GRE成绩需大于0，小于346！");
                                return;
                            }
                            info.setSatScore(-1);
                            info.setActScore(-1);
                            info.setGreScore(score);
                            info.setGmatScore(-1);
                            break;
                        case "GMAT成绩":
                            if (score <= 0 || score > 806) {
                                ToastUtils.showToast(this, "输入有误，GMAT成绩需大于0，小于806！");
                                return;
                            }
                            info.setSatScore(-1);
                            info.setActScore(-1);
                            info.setGreScore(-1);
                            info.setGmatScore(score);
                            break;
                        default:
                            break;
                    }
                }
                String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    // 显示进度条
                    mProgressDialog = ProgressDialog.show(this, null, getString(R.string.analyzing));
                    mProgressDialog.show();
                    testP.goTest(info);
                    info = null;
                } else {
                    DialogCreator.createLoginDialog(this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_SMART_CHOOSE:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                String value = data.getStringExtra("new_value");
                IdNameInfo info = JSON.parseObject(value, IdNameInfo.class);
                if (ParameterUtils.CHOOSE_APPLY_PROJ.equals(flag)) {
                    tvapplyproject.setText(info.getName());
                    degreeId = info.getId();
                    if (!info.getName().equals(lastProj)) {
                        lastProj = info.getName();
                        spinner_test.setSelection(0);
                        if (!("本科".equals(info.getName()) || "高中".equals(info.getName()))) {
                            testList.clear();
                            testList.addAll(Arrays.asList(getResources().getStringArray(R.array.shscore)));
                            adapterTest.notifyDataSetChanged();
                        } else {
                            testList.clear();
                            testList.addAll(Arrays.asList(getResources().getStringArray(R.array.bkscore)));
                            adapterTest.notifyDataSetChanged();
                        }
                    }
                } else if (ParameterUtils.EDIT_GRADE.equals(flag)) {
                    tvgrade.setText(info.getName());
                    gradeId = info.getId();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SrtTestRateContract.Presenter presenter) {
        if (presenter != null) {
            testP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        dismissDialog();
        ToastUtils.showToast(this, message);
    }

    @Override
    public void doTestSuccess(String rate_id, SmartChooseInfo info) {
        dismissDialog();
        bundle.putParcelable("params", info);
        bundle.putString("id", rate_id);
        startActivity(new Intent(this, SrtRateResultActivity.class)
                .putExtras(bundle));
        info = null;
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void getHasTestNumSuccess(String num) {
        tvhastest.setText(String.format(getString(R.string.has_test), num));
    }

    @Override
    public void showSchoolInfo(String logoUrl, String chineseName, String egName) {
        DisplayImageUtils.formatCircleImgUrl(this, logoUrl, iv_logo);
        tv_chineseName.setText(chineseName);
        tv_englishName.setText(egName);
        bundle.putString("logoUrl", logoUrl);
        bundle.putString("chineseName", chineseName);
        bundle.putString("englishName", egName);
    }
}
