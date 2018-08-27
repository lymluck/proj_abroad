package com.smartstudy.xxd.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * @author louis
 * @date on 2018/5/25
 * @describe 选择考试类别对话框
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ExamDateDialog extends AppBasicDialog {

    public ExamDateDialog(Context context, List<String> joinDateStr, String title, String dateStr,
                          String countStr, View.OnClickListener listener) {
        super(context, R.style.appBasicDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_exam_date, null);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        initView(context, joinDateStr, title, dateStr, countStr, listener);
    }

    private void initView(Context context, List<String> joinDateStr, String title, String dateStr,
                          String countStr, final View.OnClickListener listener) {
        int size = joinDateStr.size();
        ((TextView) findViewById(R.id.dialog_title)).setText(title);
        ((TextView) findViewById(R.id.tv_select_date)).setText(dateStr);
        TextView tvSelectDount = findViewById(R.id.tv_select_count);
        final LinearLayout llJoinDates = findViewById(R.id.ll_join_dates);
        if (size > 0) {
            tvSelectDount.setText(R.string.exam_plan_tip);
            findViewById(R.id.line).setVisibility(View.VISIBLE);
            TextView tvJoinTitle = findViewById(R.id.tv_has_join_title);
            tvJoinTitle.setText("已添加的" + title);
            tvJoinTitle.setVisibility(View.VISIBLE);
            findViewById(R.id.msv_join_dates).setVisibility(View.VISIBLE);
            initHasJoinDates(context, llJoinDates, joinDateStr);
        } else {
            tvSelectDount.setText(countStr);
            findViewById(R.id.line).setVisibility(View.GONE);
            findViewById(R.id.tv_has_join_title).setVisibility(View.GONE);
            findViewById(R.id.msv_join_dates).setVisibility(View.GONE);
        }
        Button ok_btn = findViewById(R.id.positive_btn);
        ok_btn.setText(R.string.join_exam);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onClick(v);
            }
        });
        Button cacle_btn = findViewById(R.id.negative_btn);
        cacle_btn.setText(R.string.cancle);
        cacle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        getWindow().setAttributes(p);
        show();
    }

    private void initHasJoinDates(Context context, LinearLayout llJoinDates, List<String> joinDateStr) {
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String dateStr : joinDateStr) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(context.getResources().getColor(R.color.exam_date_color));
            textView.setTextSize(13f);
            textView.setText(dateStr);
            textView.setLayoutParams(tvParams);
            llJoinDates.addView(textView, -1);
        }
    }
}
