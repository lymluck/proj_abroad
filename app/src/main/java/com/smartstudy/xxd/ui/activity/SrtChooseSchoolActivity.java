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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.tools.IeltsScoreFilter;
import com.smartstudy.commonlib.base.tools.ToeflScoreFilter;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtChooseSchoolContract;
import com.smartstudy.xxd.mvp.presenter.SrtChooseSchoolPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

@Route("SrtChooseSchoolActivity")
public class SrtChooseSchoolActivity extends BaseActivity implements SrtChooseSchoolContract.View {

    private TextView tvtargetcountry;
    private LinearLayout llyttarget;
    private TextView tvproject;
    private LinearLayout llytproject;
    private TextView tvschool;
    private TextView tv_has_test;
    private LinearLayout llytschool;
    private EditText et_school_score;
    private EditText et_eg;
    private EditText et_test;
    private ProgressDialog mProgressDialog;
    private Spinner spinner_eg;
    private Spinner spinner_test;

    private String country_id;
    private String degreeId;
    private String topId;
    private ArrayList<SmartSchoolRstInfo> schoolInfo;
    private SrtChooseSchoolContract.Presenter smartP;
    private int nowEgPos;
    private int nowTestPos;
    private ArrayAdapter adapterEg;
    private ArrayAdapter adapterTest;
    private List<String> testList;
    private String lastProj;
    private InputFilter[] toeflFilter;
    private InputFilter[] ieltsFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_choose_school);
        addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(CommonSearchActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        toeflFilter = new InputFilter[]{new ToeflScoreFilter()};
        ieltsFilter = new InputFilter[]{new IeltsScoreFilter()};
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("智能选校");
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
        this.llytschool = (LinearLayout) findViewById(R.id.llyt_school);
        this.tvschool = (TextView) findViewById(R.id.tv_school);
        this.llytproject = (LinearLayout) findViewById(R.id.llyt_project);
        this.tvproject = (TextView) findViewById(R.id.tv_project);
        this.llyttarget = (LinearLayout) findViewById(R.id.llyt_target);
        this.tvtargetcountry = (TextView) findViewById(R.id.tv_target_country);
        this.tv_has_test = (TextView) findViewById(R.id.tv_has_test);
        tv_has_test.setText(String.format(getString(R.string.has_test), "0"));
        schoolInfo = new ArrayList<>();
        new SrtChooseSchoolPresenter(this);
        smartP.getHasTestNum();
    }

    @Override
    public void initEvent() {
        llytproject.setOnClickListener(this);
        llytschool.setOnClickListener(this);
        llyttarget.setOnClickListener(this);
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
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                KeyBoardUtils.closeKeybord(et_school_score, this);
                finish();
                break;
            case R.id.llyt_target:
                Intent toChooseIntent = new Intent(this, ChooseListActivity.class);
                toChooseIntent.putExtra("value", tvtargetcountry.getText());
                toChooseIntent.putExtra("ischange", false);
                toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_SCHOOL);
                toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                toChooseIntent.putExtra(TITLE, "选择意向国家");
                startActivityForResult(toChooseIntent, ParameterUtils.REQUEST_CODE_SMART_CHOOSE);
                break;
            case R.id.llyt_project:
                Intent toChooseProj = new Intent(this, ChooseListActivity.class);
                toChooseProj.putExtra("value", tvproject.getText());
                toChooseProj.putExtra("ischange", false);
                toChooseProj.putExtra("from", ParameterUtils.TYPE_OPTIONS_SCHOOL);
                toChooseProj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.CHOOSE_APPLY_PROJ);
                toChooseProj.putExtra(TITLE, "选择申请项目");
                startActivityForResult(toChooseProj, ParameterUtils.REQUEST_CODE_SMART_CHOOSE);
                break;
            case R.id.llyt_school:
                Intent toChooseSchool = new Intent(this, ChooseListActivity.class);
                toChooseSchool.putExtra("value", tvproject.getText());
                toChooseSchool.putExtra("ischange", false);
                toChooseSchool.putExtra("from", ParameterUtils.TYPE_OPTIONS_SCHOOL);
                toChooseSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_TOP_SCHOOL);
                toChooseSchool.putExtra(TITLE, "选择择校倾向");
                startActivityForResult(toChooseSchool, ParameterUtils.REQUEST_CODE_SMART_CHOOSE);
                break;
            case R.id.btn_choose_school:
                if (country_id == null) {
                    ToastUtils.showToast("请选择意向国家！");
                    break;
                }
                if (degreeId == null) {
                    ToastUtils.showToast("请选择申请项目！");
                    break;
                }
                if ((TextUtils.isEmpty(et_test.getText()) || "0".equals(et_test.getText()))
                    && (TextUtils.isEmpty(et_eg.getText().toString()) || "0".equals(et_eg.getText().toString()) || ".".equals(et_eg.getText().toString()))) {
                    if (findViewById(R.id.llyt_test).getVisibility() == View.VISIBLE) {
                        ToastUtils.showToast( "请至少提供语言成绩或标准化成绩中的一项！");
                    } else {
                        ToastUtils.showToast( "请输入语言成绩！");
                    }
                    break;
                }
                SmartChooseInfo info = new SmartChooseInfo();
                info.setCountryId(country_id);
                info.setProjId(degreeId);
                info.setRankTop(topId);
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
                        ToastUtils.showToast(text + "需大于0！");
                        return;
                    }
                }
                if (!TextUtils.isEmpty(et_school_score.getText())) {
                    int schoolScore = Integer.parseInt(et_school_score.getText().toString(), 10);
                    if (schoolScore <= 0 || schoolScore > 100) {
                        ToastUtils.showToast("输入有误，在校成绩需大于0，为百分制！");
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
                                ToastUtils.showToast("输入有误，SAT成绩需大于0，小于1600！");
                                return;
                            }
                            info.setSatScore(score);
                            info.setActScore(-1);
                            info.setGreScore(-1);
                            info.setGmatScore(-1);
                            break;
                        case "ACT成绩":
                            if (score <= 0 || score > 36) {
                                ToastUtils.showToast( "输入有误，ACT成绩需大于0，小于36！");
                                return;
                            }
                            info.setSatScore(-1);
                            info.setActScore(score);
                            info.setGreScore(-1);
                            info.setGmatScore(-1);
                            break;
                        case "GRE成绩":
                            if (score <= 0 || score > 340) {
                                ToastUtils.showToast("输入有误，GRE成绩需大于0，小于340！");
                                return;
                            }
                            info.setSatScore(-1);
                            info.setActScore(-1);
                            info.setGreScore(score);
                            info.setGmatScore(-1);
                            break;
                        case "GMAT成绩":
                            if (score <= 0 || score > 800) {
                                ToastUtils.showToast("输入有误，GMAT成绩需大于0，小于800！");
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
                // 显示进度条
                mProgressDialog = ProgressDialog.show(this, null, getString(R.string.analyzing));
                mProgressDialog.show();
                smartP.goChoose(info, schoolInfo);
                info = null;
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
                if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                    tvtargetcountry.setText(info.getName());
                    country_id = info.getId();
                    if ("美国".equals(info.getName())) {
                        findViewById(R.id.llyt_test).setVisibility(View.VISIBLE);
                        findViewById(R.id.view_test_line).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.llyt_test).setVisibility(View.GONE);
                        findViewById(R.id.view_test_line).setVisibility(View.GONE);
                    }
                } else if (ParameterUtils.CHOOSE_APPLY_PROJ.equals(flag)) {
                    tvproject.setText(info.getName());
                    degreeId = info.getId();
                    if (!info.getName().equals(lastProj)) {
                        lastProj = info.getName();
                        spinner_test.setSelection(0);
                        if (!"本科".equals(info.getName())) {
                            testList.clear();
                            testList.addAll(Arrays.asList(getResources().getStringArray(R.array.shscore)));
                            adapterTest.notifyDataSetChanged();
                        } else {
                            testList.clear();
                            testList.addAll(Arrays.asList(getResources().getStringArray(R.array.bkscore)));
                            adapterTest.notifyDataSetChanged();
                        }
                    }
                } else if (ParameterUtils.EDIT_TOP_SCHOOL.equals(flag)) {
                    tvschool.setText(info.getName());
                    topId = info.getId();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SrtChooseSchoolContract.Presenter presenter) {
        if (presenter != null) {
            smartP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        dismissDialog();
        ToastUtils.showToast( message);
    }

    @Override
    public void doChooseSuccess(ArrayList<SmartSchoolRstInfo> schoolInfo) {
        dismissDialog();
        Intent toResult = new Intent(this, SrtSchoolResultActivity.class);
        toResult.putParcelableArrayListExtra("choose_result", schoolInfo);
        toResult.putExtra("country_id", country_id);
        toResult.putExtra("degreeId", degreeId);
        toResult.putExtra("topId", topId);
        toResult.putExtra("schoolScore", et_school_score.getText().toString());
        toResult.putExtra("egScore", et_eg.getText().toString());
        String text = spinner_test.getSelectedItem().toString();
        switch (text) {
            case "SAT成绩":
                toResult.putExtra("sat", et_test.getText().toString());
                break;
            case "ACT成绩":
                toResult.putExtra("act", et_test.getText().toString());
                break;
            case "GRE成绩":
                toResult.putExtra("gre", et_test.getText().toString());
                break;
            case "GMAT成绩":
                toResult.putExtra("gmat", et_test.getText().toString());
                break;
            default:
                break;
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            toResult.putExtras(getIntent().getExtras());
        }
        startActivity(toResult);
    }

    @Override
    public void getHasTestNumSuccess(String num) {
        tv_has_test.setText(String.format(getString(R.string.has_test), num));
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
