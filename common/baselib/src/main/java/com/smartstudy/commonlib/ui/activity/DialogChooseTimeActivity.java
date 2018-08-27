package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customview.wheelview.WheelMain;
import com.smartstudy.commonlib.utils.ScreenUtils;


public class DialogChooseTimeActivity extends UIActivity implements View.OnClickListener {

    private LinearLayout llyt_content;
    private LinearLayout timePicker1;
    private LinearLayout timePicker2;
    private TextView tv_select_time_title;

    private String selected_time;
    private WheelMain wheelMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusColor(R.color.transparent);//修改状态栏颜色
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_choose_time);
    }

    @Override
    protected void initViewAndData() {
        llyt_content = (LinearLayout) findViewById(R.id.llyt_content);
        timePicker1 = (LinearLayout) findViewById(R.id.timePicker1);
        timePicker2 = (LinearLayout) findViewById(R.id.timePicker2);
        tv_select_time_title = (TextView) findViewById(R.id.tv_select_time_title);
        Intent data = getIntent();
        if ("order_date".equals(data.getStringExtra("tag"))) {
            timePicker1.setVisibility(View.VISIBLE);
            wheelMain = new WheelMain(timePicker1);
            wheelMain.initDateTimePicker("order_date", data.getStringExtra("date"));
            tv_select_time_title.setText("选择预约日期");
        } else if ("order_time".equals(data.getStringExtra("tag"))) {
            timePicker2.setVisibility(View.VISIBLE);
            wheelMain = new WheelMain(timePicker2);
            wheelMain.initDateTimePicker("order_time", data.getStringExtra("time"));
            tv_select_time_title.setText("选择预约时间段");
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancle) {
            finishAnim();
        } else if (id == R.id.tv_clear) {
            Intent result_data = new Intent();
            result_data.putExtra("selected_time", "");
            setResult(RESULT_OK, result_data);
            finishAnim();
        } else if (id == R.id.tv_sure) {
            Intent data = getIntent();
            if ("order_date".equals(data.getStringExtra("tag"))) {
                selected_time = wheelMain.getDate();
            } else if ("order_time".equals(data.getStringExtra("tag"))) {
                selected_time = wheelMain.getTime();
            }
            Intent re_data = new Intent();
            re_data.putExtra("value", selected_time);
            setResult(RESULT_OK, re_data);
            finishAnim();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() < ScreenUtils.getScreenHeight() - llyt_content.getHeight()) {
            finishAnim();
        }
        return true;
    }

    private void finishAnim() {
        finish();
        this.overridePendingTransition(0, R.anim.slide_bottom_out);
    }
}

