package com.smartstudy.commonlib.ui.customview;

/**
 * 自定义右边带清除按钮的edittext
 */

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.R;


public class EditTextWithClear extends LinearLayout {
    private EditText edt_input;

    private ImageButton btn_delete;

    private boolean is_hiddenDeleteBtn = false;


    public boolean isIs_hiddenDeleteBtn() {
        return is_hiddenDeleteBtn;
    }

    public void setIs_hiddenDeleteBtn(boolean is_hiddenDeleteBtn) {
        this.is_hiddenDeleteBtn = is_hiddenDeleteBtn;
    }

    public EditText getEdt_input() {
        return edt_input;
    }

    public ImageButton getBtn_delete() {
        return btn_delete;
    }

    public EditTextWithClear(Context context) {
        super(context);
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_clear, this, true);
        edt_input = (EditText) view.findViewById(R.id.edt_input);
        btn_delete = (ImageButton) view.findViewById(R.id.btn_delete);
        edt_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!is_hiddenDeleteBtn) {
                    btn_delete.setVisibility(edt_input.isFocused() && s.length() > 0 ? View.VISIBLE : View.GONE);
                }

            }
        });
        edt_input.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // get Soft Keyborder;
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                // 控制键盘的关闭
                if (!hasFocus) {
                    imm.hideSoftInputFromWindow(edt_input.getWindowToken(), 0);
                }

                if (!is_hiddenDeleteBtn) {
                    btn_delete.setVisibility(edt_input.getText().length() <= 0 || !hasFocus ? GONE : VISIBLE);
                }
            }
        });
        btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edt_input.setText("");
            }
        });
    }

    /**
     * @param str 设置输入框显示的默认值
     */
    public void setHint(String str) {
        edt_input.setHint(str);
    }

    /**
     * @param id 字符id
     */
    public void setHint(int id) {
        edt_input.setHint(id);
    }

    /**
     * @return 获取输入框中的内容
     */
    public String getText() {
        return edt_input.getText().toString().trim();
    }

    /**
     * @param str 设置输入框内容
     */
    public void setText(String str) {
        edt_input.setText(str);
    }

    /**
     * @param id 字符id
     */
    public void setText(int id) {
        edt_input.setText(id);
    }

    /**
     * @param id 设置输入框字符类型id
     */
    public void setInputType(int id) {

        edt_input.setInputType(id);

    }

    public void setLength(int max) {

        edt_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});

    }

    // 得到EditText的引用
    public EditText getMyEditText() {
        return edt_input;
    }

    public void setSingleLine(boolean b) {
        edt_input.setSingleLine(b);
    }

    public void setHorizontallyScrolling(boolean b) {
        edt_input.setHorizontallyScrolling(false);

    }

    public void setImeOptions(int type) {
        edt_input.setImeOptions(type);
    }
}
