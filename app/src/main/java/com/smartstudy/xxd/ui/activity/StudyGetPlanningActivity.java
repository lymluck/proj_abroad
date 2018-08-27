package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.OptionsInfo;
import com.smartstudy.xxd.mvp.contract.StudyGetPlanningContract;
import com.smartstudy.xxd.mvp.presenter.StudyGetPlanningPresenter;

/**
 * Created by yqy on 2017/12/6.
 */
@Route("studyPlan")
public class StudyGetPlanningActivity extends UIActivity implements StudyGetPlanningContract.View {
    private TextView tvProject;

    private TextView tvGrade;

    private LinearLayout llGrade;

    private StudyGetPlanningContract.Presenter presenter;


    private String currentGradeId;

    private String targetDegreeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_get_planning);
    }

    @Override
    protected void initViewAndData() {
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);

        titleView.setText("获取留学规划");

        findViewById(R.id.top_line).setVisibility(View.VISIBLE);


        tvProject = findViewById(R.id.tv_project);

        tvGrade = findViewById(R.id.tv_grade);


        llGrade = findViewById(R.id.ll_grade);

        new StudyGetPlanningPresenter(this);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);

        findViewById(R.id.ll_project).setOnClickListener(this);

        findViewById(R.id.tv_get_plan).setOnClickListener(this);

        llGrade.setOnClickListener(this);

    }

    @Override
    protected void doClick(View v) {

        switch (v.getId()) {
            case R.id.ll_project:
                Intent intent = new Intent(this, StudyProjectActivity.class);
                intent.putExtra("type", "project");
                intent.putExtra("data", tvProject.getText().toString().trim());
                startActivityForResult(intent, 100);
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;


            case R.id.ll_grade:
                Intent intent1 = new Intent(this, StudyProjectActivity.class);
                intent1.putExtra("type", tvProject.getText().toString().trim());
                intent1.putExtra("data", tvGrade.getText().toString().trim());
                startActivityForResult(intent1, 99);
                break;


            case R.id.tv_get_plan:
                if (TextUtils.isEmpty(currentGradeId) || TextUtils.isEmpty(targetDegreeId)) {
                    ToastUtils.showToast(this, "信息未填完整");
                } else {
                    presenter.postStudyPlanning(currentGradeId, targetDegreeId);
                }

                break;

            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 88) {
            Bundle bundle = data.getExtras();
            OptionsInfo back = (OptionsInfo) bundle.getSerializable("back");
            if (requestCode == 100) {
                tvProject.setText(back.getName());
                targetDegreeId = back.getId();
                currentGradeId="";
                tvGrade.setText("当前年级");
                llGrade.setVisibility(View.VISIBLE);
                findViewById(R.id.v_line).setVisibility(View.VISIBLE);
            } else {
                currentGradeId = back.getId();
                tvGrade.setText(back.getName());
            }

        }
    }

    @Override
    public void setPresenter(StudyGetPlanningContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void postSuccess() {
        startActivity(new Intent(this, AbroadPlanActivity.class));
        finish();
    }
}
