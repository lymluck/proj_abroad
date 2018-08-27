package com.smartstudy.xxd.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.utils.AppContants;

/**
 * @author louis
 * @date on 2018/5/25
 * @describe 选择考试类别对话框
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class HasOptExamDialog extends AppBasicDialog {

    public HasOptExamDialog(Context context, String title, String dateStr, String countStr,
                            OnClickListener listener) {
        super(context, R.style.appBasicDialog);
        initView(context, title, dateStr, countStr, listener);
    }

    private void initView(Context context, String title, String dateStr, String countStr,
                          final OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_has_opt_exam, null);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.dialog_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_select_date)).setText(dateStr);
        ((TextView) view.findViewById(R.id.tv_select_count)).setText(countStr);
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_delete_exam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onClick(HasOptExamDialog.this, AppContants.DIALOG_WHICH_DEL_EXAM);
            }
        });
        view.findViewById(R.id.btn_watch_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onClick(HasOptExamDialog.this, AppContants.DIALOG_WHICH_EXAM_OTHER);
            }
        });
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        getWindow().setAttributes(p);
    }
}

