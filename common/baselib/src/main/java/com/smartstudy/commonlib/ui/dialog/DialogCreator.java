package com.smartstudy.commonlib.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.listener.DialogClickListener;
import com.smartstudy.commonlib.entity.CardRemarkInfo;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.utils.NoFastClickUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.router.Router;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;


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
                                                        @Nullable final DialogClickListener confirmListener, @Nullable final DialogClickListener cancleListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_base, null);
        dialog.setContentView(v);
        TextView titleTv = v.findViewById(R.id.dialog_base_title_tv);
        TextView textTv = v.findViewById(R.id.dialog_base_text_tv);
        Button confirmBtn = v.findViewById(R.id.dialog_base_confirm_btn);
        Button cancleBtn = v.findViewById(R.id.dialog_base_cancle_btn);
        titleTv.setText(title);
        textTv.setText(text);
        if (confirmListener != null) {
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmListener.onClick(dialog, v);
                }
            });
        }
        if (cancleListener != null) {
            cancleBtn.setVisibility(View.VISIBLE);
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancleListener.onClick(dialog, v);
                }
            });
        }
        dialog.setCancelable(false);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        return dialog;
    }

    public static AppBasicDialog createAppBasicDialog(final Context context, String title, String msg_tip, String txt_ok,
                                                      String txt_cancle, View.OnClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_basicdialog, null);
        dialog.setContentView(view);
        view.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
        TextView tv_title = view.findViewById(R.id.dialog_title);
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        ((TextView) view.findViewById(R.id.dialog_info)).setText(msg_tip);
        Button ok_btn = ((Button) view.findViewById(R.id.positive_btn));
        ok_btn.setText(txt_ok);
        ok_btn.setOnClickListener(onClickListener);

        Button cancle_btn = ((Button) view.findViewById(R.id.negative_btn));
        cancle_btn.setText(txt_cancle);
        cancle_btn.setOnClickListener(onClickListener);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        return dialog;
    }

    public static Dialog createAdd2SchoolDialog(Context context, String title,
                                                View.OnClickListener listener) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.dialog_add_school, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleTv = view.findViewById(R.id.dialog_title);
        LinearLayout llyt_top = view.findViewById(R.id.llyt_top);
        LinearLayout llyt_mid = view.findViewById(R.id.llyt_mid);
        LinearLayout llyt_bottom = view.findViewById(R.id.llyt_bottom);
        titleTv.setText(title);
        llyt_top.setOnClickListener(listener);
        llyt_mid.setOnClickListener(listener);
        llyt_bottom.setOnClickListener(listener);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
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
        Button cacle_btn = view.findViewById(R.id.negative_btn);
        cacle_btn.setText(R.string.cancle);
        cacle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
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


    public static Dialog createCommentDialog(Context context, final String type,
                                             View.OnClickListener listener) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.dialog_comment, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RatingBar rb_course_rate = (RatingBar) view.findViewById(R.id.rb_course_rate);
        final Button btn_commit = (Button) view.findViewById(R.id.btn_commit);
        btn_commit.setTag("");
        ImageView ivCancel = view.findViewById(R.id.iv_cancel);
        final TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        final EditText et_msg = (EditText) view.findViewById(R.id.et_msg);
        final float count = 0f;
        if (type.equals("QA")) {
            tv_msg.setVisibility(View.GONE);
            et_msg.setHint("多说点什么吧...");
        } else {
            tv_msg.setVisibility(View.VISIBLE);
        }

        rb_course_rate.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                btn_commit.setHint(ratingCount + "");
                if (!btn_commit.isEnabled()) {
                    btn_commit.setEnabled(true);
                }
                if (!type.equals("QA")) {
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

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
//        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        p.width = (WindowManager.LayoutParams.MATCH_PARENT);
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
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


    public static void createIntentionallyDialog(final Activity context, final IntentionOnclick intentionOnclick) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.dialog_intentionally, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (intentionOnclick != null) {
                    intentionOnclick.delete();
                }
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public interface IntentionOnclick {
        void delete();
    }


    public static void createRemarkDialog(final Activity context, final CardRemarkInfo cardRemarkInfo) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View showView = factory.inflate(R.layout.dialog_save_share_friend, null);
        dialog.addContentView(showView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tvSaveShare = showView.findViewById(R.id.tv_save_share);
        TextView tvExam = showView.findViewById(R.id.tv_exam);
        tvExam.setText("备考" + cardRemarkInfo.getExam());
        TextView tvCheckinCount = showView.findViewById(R.id.tv_checkin_count);
        final Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/GothamMedium.otf");
        // 应用字体
        tvCheckinCount.setTypeface(typeFace);
        tvCheckinCount.setText(cardRemarkInfo.getCheckinCount());
        TextView tvMonthDay = showView.findViewById(R.id.tv_month_day);
        tvMonthDay.setText(cardRemarkInfo.getToday().substring(5, cardRemarkInfo.getToday().length()).replace("-", "."));
        TextView tvYear = showView.findViewById(R.id.tv_year);
        tvYear.setText(cardRemarkInfo.getToday().substring(0, 4));
        View drawLineView = showView.findViewById(R.id.dlv_line);
        final ImageView iv = showView.findViewById(R.id.iv_step);
        drawLineView.setRotation(-20f);
        Window window = dialog.getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        final WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
//        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
//        p.height = p.width * 4 / 3;
        FrameLayout.LayoutParams ivParams = (FrameLayout.LayoutParams) iv.getLayoutParams();
        ivParams.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        ivParams.height = (int) (ScreenUtils.getScreenWidth() * 0.85) * 3 / 4;
        iv.setLayoutParams(ivParams);
        tvSaveShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NoFastClickUtils.isFastClick()) {
                    View shareView = LayoutInflater.from(context).inflate(R.layout.layout_share_img, null);
                    //这个一定要放在前面，否则会出一些奇怪的问题，嘿嘿
                    shareView.setDrawingCacheEnabled(true);
                    View drawLineView = shareView.findViewById(R.id.dlv_line);
                    drawLineView.setRotation(-20f);
                    TextView tvExam = shareView.findViewById(R.id.tv_exam);
                    tvExam.setText("备考" + cardRemarkInfo.getExam());
                    TextView tvCheckinCount = shareView.findViewById(R.id.tv_checkin_count);
                    tvCheckinCount.setTypeface(typeFace);
                    tvCheckinCount.setText(cardRemarkInfo.getCheckinCount());
                    TextView tvMonthDay = shareView.findViewById(R.id.tv_month_day);
                    tvMonthDay.setText(cardRemarkInfo.getToday().substring(5, cardRemarkInfo.getToday().length()).replace("-", "."));
                    TextView tvYear = shareView.findViewById(R.id.tv_year);
                    tvYear.setText(cardRemarkInfo.getToday().substring(0, 4));
                    shareView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    shareView.layout(0, 0, shareView.getMeasuredWidth(), shareView.getMeasuredHeight());
                    Bitmap bitmap = Bitmap.createBitmap(shareView.getDrawingCache());
                    shareView.setDrawingCacheEnabled(false);
                    UMShareUtils.showWeixinFriend(context, bitmap, "", new ShareListener("", ""), SHARE_MEDIA.WEIXIN_CIRCLE);
                    dialog.dismiss();
                }
            }
        });
//        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void createPrivacyDialog(final Activity context, final boolean isSetting, final PrivacyOnClick privacyOnClick) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View showView = factory.inflate(R.layout.dialog_privacy_setting, null);
        dialog.addContentView(showView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tvContent = showView.findViewById(R.id.tv_content);
        String text = "设为隐私后我的选校对其他人不可见，<font color='#FF9C08'>同时也不可查看其他人的选校。</font>";
        tvContent.setText(Html.fromHtml(text));

        ImageView ivCancel = showView.findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout llAllSe = showView.findViewById(R.id.ll_all_se);
        TextView tvSllSe = showView.findViewById(R.id.tv_all_se);
        LinearLayout llPrivacy = showView.findViewById(R.id.ll_privacy);
        TextView tvPrivacy = showView.findViewById(R.id.tv_privacy);
        if (isSetting) {
            llAllSe.setBackgroundResource(R.drawable.bg_all_person_see);
            Drawable img = context.getResources().getDrawable(R.drawable.ic_unlock_white);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvSllSe.setTextColor(Color.parseColor("#ffffff"));
            tvSllSe.setCompoundDrawables(img, null, null, null);
            llPrivacy.setBackgroundResource(R.drawable.bg_privacy_setting);
            Drawable imgPrivacy = context.getResources().getDrawable(R.drawable.ic_lock_gray);

            imgPrivacy.setBounds(0, 0, imgPrivacy.getMinimumWidth(), imgPrivacy.getMinimumHeight());
            tvPrivacy.setCompoundDrawables(imgPrivacy, null, null, null);
            tvPrivacy.setTextColor(Color.parseColor("#949BA1"));
        } else {
            llAllSe.setBackgroundResource(R.drawable.bg_privacy_setting);
            Drawable img = context.getResources().getDrawable(R.drawable.ic_unlock_gray);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            tvSllSe.setTextColor(Color.parseColor("#949BA1"));
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvSllSe.setCompoundDrawables(img, null, null, null);
            llPrivacy.setBackgroundResource(R.drawable.bg_all_person_see);
            Drawable imgPrivacy = context.getResources().getDrawable(R.drawable.ic_lock_white);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            imgPrivacy.setBounds(0, 0, imgPrivacy.getMinimumWidth(), imgPrivacy.getMinimumHeight());
            tvPrivacy.setCompoundDrawables(imgPrivacy, null, null, null);
            tvPrivacy.setTextColor(Color.parseColor("#ffffff"));
        }

        llAllSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (privacyOnClick != null) {
                    privacyOnClick.allCanseOnClick();
                    dialog.dismiss();
                }
            }
        });

        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (privacyOnClick != null) {
                    privacyOnClick.noCanseOnClick();
                    dialog.dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        final WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public interface PrivacyOnClick {
        void allCanseOnClick();

        void noCanseOnClick();
    }

}
