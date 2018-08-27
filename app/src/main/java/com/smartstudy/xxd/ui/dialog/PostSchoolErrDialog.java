package com.smartstudy.xxd.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.listener.PostErrDialogClickListener;

/**
 * @author louis
 * @date on 2018/5/25
 * @describe 选择考试类别对话框
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class PostSchoolErrDialog extends AppBasicDialog {

    public PostSchoolErrDialog(Context context, PostErrDialogClickListener listener) {
        super(context, R.style.appBasicDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_post_err, null);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        initView(listener);
    }

    private void initView(final PostErrDialogClickListener listener) {
        final EditText etContent = findViewById(R.id.et_content);
        findViewById(R.id.positive_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast("内容不能为空！");
                } else {
                    dismiss();
                    listener.onClick(content);
                }
            }
        });
        findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
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
}
