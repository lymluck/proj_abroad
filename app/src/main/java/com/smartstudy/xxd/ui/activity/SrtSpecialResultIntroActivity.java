package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.RadarData;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customview.RadarView;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SrtSpecialResultIntroActivity extends UIActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_special_result_intro);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("霍兰德职业适应性测验");
        RadarView mRadarView = (RadarView) findViewById(R.id.rc_test);
        mRadarView.setEmptyHint("无数据");
        List<String> vertexText = new ArrayList<>();
        Collections.addAll(vertexText, "企业型", "传统型", "实际型", "研究型", "艺术型", "社会型");
        mRadarView.setVertexText(vertexText);
        List<Float> values = new ArrayList<>();
        Collections.addAll(values, 1.3f, 0.8f, 1.2f, 0.9f, 1.1f, 1f);
        RadarData data = new RadarData(values);
        data.setColor(getResources().getColor(R.color.app_main_color));
        mRadarView.addData(data);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }
}
