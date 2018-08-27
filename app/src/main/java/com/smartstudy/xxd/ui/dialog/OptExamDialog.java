package com.smartstudy.xxd.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ExamDateInfo;
import com.smartstudy.xxd.listener.ExamDialogClickListener;

import java.util.List;

/**
 * @author louis
 * @date on 2018/5/25
 * @describe 选择考试类别对话框
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class OptExamDialog extends AppBasicDialog {

    public OptExamDialog(Context context, List<ExamDateInfo.ItemsEntity> items, ExamDialogClickListener listener) {
        super(context, R.style.appBasicDialog);
        initView(context, items, listener);
    }

    private void initView(Context context, List<ExamDateInfo.ItemsEntity> items, final ExamDialogClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_opt_exm, null);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.dialog_title)).setText("选择考试类别");
        LinearLayout llExam = view.findViewById(R.id.ll_exam);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(52f));
        int len = items.size();
        for (int i = 0; i < len; i++) {
            final ExamDateInfo.ItemsEntity entity = items.get(i);
            LinearLayout llItem = new LinearLayout(context);
            llItem.setGravity(Gravity.CENTER_VERTICAL);
            llItem.setLayoutParams(params);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(entity);
                    dismiss();
                }
            });
            if (i < len - 1) {
                llItem.setBackgroundResource(R.drawable.simple_item_bg);
            } else {
                llItem.setBackgroundResource(R.drawable.bg_btn_w_lrb_radius);
            }
            // 圆点
            TextView tvDot = new TextView(context);
            tvDot.setBackgroundResource(dotbg(entity.getExam()));
            int value = DensityUtils.dip2px(7f);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(value, value);
            dotParams.leftMargin = DensityUtils.dip2px(24f);
            dotParams.gravity = Gravity.CENTER;
            tvDot.setLayoutParams(dotParams);
            llItem.addView(tvDot);
            // 考试类别
            TextView tvExam = new TextView(context);
            tvExam.setTextColor(desColor(entity.getExam()));
            tvExam.setText(entity.getExam());
            LinearLayout.LayoutParams examParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            examParams.leftMargin = DensityUtils.dip2px(12f);
            examParams.rightMargin = DensityUtils.dip2px(24f);
            tvExam.setLayoutParams(examParams);
            llItem.addView(tvExam);
            llExam.addView(llItem, -1);
        }
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        getWindow().setAttributes(p);
    }

    private int desColor(String des) {
        if ("托福".equals(des)) {
            return Color.parseColor("#FFB02F");
        } else if ("雅思".equals(des)) {
            return Color.parseColor("#FF5A52");
        } else if ("GMAT".equals(des)) {
            return Color.parseColor("#4FC069");
        } else if ("GRE".equals(des)) {
            return Color.parseColor("#4B67BF");
        } else if ("SAT".equals(des)) {
            return Color.parseColor("#5FACFF");
        } else {
            return Color.parseColor("#5FACFF");
        }
    }

    private int dotbg(String des) {
        if ("托福".equals(des)) {
            return R.drawable.bg_oval_tolft_size;
        } else if ("雅思".equals(des)) {
            return R.drawable.bg_oval_islet_size;
        } else if ("GMAT".equals(des)) {
            return R.drawable.bg_oval_gmat_size;
        } else if ("GRE".equals(des)) {
            return R.drawable.bg_oval_gre_size;
        } else if ("SAT".equals(des)) {
            return R.drawable.bg_oval_sat_size;
        } else {
            return R.drawable.bg_oval_sat_size;
        }
    }

}
