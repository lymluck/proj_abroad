package com.smartstudy.commonlib.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.listener.OnSendMsgDialogClickListener;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.router.Router;


public class DialogCreator {

    public static AppBasicDialog createLoadingDialog(Context context, String msg) {
        LayoutInflater factory = LayoutInflater.from(context);
        AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        View view = factory.inflate(R.layout.dialog_loading_view, null);
        dialog.setContentView(view);
        TextView mLoadText = (TextView) view.findViewById(R.id.loading_txt);
        mLoadText.setText(msg);
        dialog.setCancelable(true);
        return dialog;
    }

    public static AppBasicDialog createBaseCustomDialog(Context context, String title, String text,
                                                        View.OnClickListener onClickListener) {
        AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_base, null);
        dialog.setContentView(v);
        TextView titleTv = (TextView) v.findViewById(R.id.dialog_base_title_tv);
        TextView textTv = (TextView) v.findViewById(R.id.dialog_base_text_tv);
        Button confirmBtn = (Button) v.findViewById(R.id.dialog_base_confirm_btn);
        titleTv.setText(title);
        textTv.setText(text);
        confirmBtn.setOnClickListener(onClickListener);
        dialog.setCancelable(false);
        return dialog;
    }

    public static AppBasicDialog createAppBasicDialog(final Context context, String title, String msg_tip, String txt_ok,
                                                      String txt_cancle, View.OnClickListener onClickListener) {
        AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_basicdialog, null);
        dialog.setContentView(view);
        view.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
        TextView tv_title = (TextView) view.findViewById(R.id.dialog_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        ((TextView) view.findViewById(R.id.dialog_info)).setText(msg_tip);
        Button ok_btn = ((Button) view.findViewById(R.id.positive_btn));
        ok_btn.setText(txt_ok);
        ok_btn.setOnClickListener(onClickListener);

        Button cancle_btn = ((Button) view.findViewById(R.id.negative_btn));
        cancle_btn.setText(txt_cancle);
        cancle_btn.setOnClickListener(onClickListener);
        return dialog;
    }

    public static Dialog createAdd2SchoolDialog(Context context, String title,
                                                View.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(
                R.layout.dialog_add_school, null);
        builder.setView(v);
        TextView titleTv = (TextView) v.findViewById(R.id.dialog_title);
        LinearLayout llyt_top = (LinearLayout) v.findViewById(R.id.llyt_top);
        LinearLayout llyt_mid = (LinearLayout) v.findViewById(R.id.llyt_mid);
        LinearLayout llyt_bottom = (LinearLayout) v.findViewById(R.id.llyt_bottom);
        titleTv.setText(title);
        Dialog dialog = builder.create();
        llyt_top.setOnClickListener(listener);
        llyt_mid.setOnClickListener(listener);
        llyt_bottom.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * 是否登录dialog
     *
     * @param context
     */
    public static void createLoginDialog(final Activity context) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.app_basicdialog, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(R.string.login);
        ((TextView) view.findViewById(R.id.dialog_info)).setText(R.string.login_now);
        Button ok_btn = ((Button) view.findViewById(R.id.positive_btn));
        ok_btn.setText(R.string.sure);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登录页面
                context.startActivityForResult(new Intent(context, LoginActivity.class), ParameterUtils.REQUEST_CODE_LOGIN);
                dialog.dismiss();
            }
        });
        Button cacle_btn = ((Button) view.findViewById(R.id.negative_btn));
        cacle_btn.setText(R.string.cancle);
        cacle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void createWebviewDialog(WebView wv, final Context mContext) {
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // 构建一个Builder来显示网页中的alert对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("title");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }
        });
    }


    public static Dialog createCommentDialog(Context context,
                                             View.OnClickListener listener) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.dialog_comment, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RatingBar rb_course_rate = (RatingBar) view.findViewById(R.id.rb_course_rate);
        final Button btn_commit = (Button) view.findViewById(R.id.btn_commit);
        btn_commit.setTag("");
        final TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        final EditText et_msg = (EditText) view.findViewById(R.id.et_msg);
        final float count = 0f;
        rb_course_rate.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                btn_commit.setHint(ratingCount + "");
                if (!btn_commit.isEnabled()) {
                    btn_commit.setEnabled(true);
                }
                if (ratingCount == 1f) {
                    tv_msg.setText("很槽糕");
                } else if (ratingCount == 2f) {
                    tv_msg.setText("不太好");
                } else if (ratingCount == 3f) {
                    tv_msg.setText("一般般");
                } else if (ratingCount == 4f) {
                    tv_msg.setText("很不错");
                } else if (ratingCount == 5f) {
                    tv_msg.setText("超级好");
                }
                btn_commit.setTag(tv_msg.getText().toString());
            }
        });
        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_commit.setTag("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btn_commit.setTag(tv_msg.getText().toString());
                } else {
                    btn_commit.setTag(s.toString());
                }
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        dialog.getWindow().setAttributes(p);
        btn_commit.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    /**
     * 首页选校帝约答卡dialog
     *
     * @param context
     */
    public static void createHomeQaDialog(final Activity context) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.dialog_home_qa_card, null);
        String key = SPCacheUtils.get("user_id", "").toString() + "QA";
        SPCacheUtils.put(key, true);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.findViewById(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.ib_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.build("studyPlan").go(context);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void createSendTextMsgDialog(final Context context, String avatar, String name,
                                               String content, final OnSendMsgDialogClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_send_text, null);
        dialog.setContentView(view);
        DisplayImageUtils.displayPersonImage(context, avatar, (ImageView) view.findViewById(R.id.iv_avatar));
        ((TextView) view.findViewById(R.id.tv_name)).setText(name);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        Button positive_btn = view.findViewById(R.id.positive_btn);
        final EditText et_word = view.findViewById(R.id.dialog_edit);
        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_word,context);
                onClickListener.onPositive(et_word.getText().toString());
            }
        });
        view.findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onNegative();
                KeyBoardUtils.closeKeybord(et_word,context);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void createSendImgMsgDialog(final Context context, String avatar, String name, String url,
                                              final OnSendMsgDialogClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_send_img, null);
        dialog.setContentView(view);
        DisplayImageUtils.displayPersonImage(context, avatar, (ImageView) view.findViewById(R.id.iv_avatar));
        ((TextView) view.findViewById(R.id.tv_name)).setText(name);
        DisplayImageUtils.displayImage(context, url, (ImageView) view.findViewById(R.id.iv_content));
        Button positive_btn = view.findViewById(R.id.positive_btn);
        final EditText et_word = view.findViewById(R.id.dialog_edit);
        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_word,context);
                onClickListener.onPositive(et_word.getText().toString());
            }
        });
        view.findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onNegative();
                KeyBoardUtils.closeKeybord(et_word,context);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
