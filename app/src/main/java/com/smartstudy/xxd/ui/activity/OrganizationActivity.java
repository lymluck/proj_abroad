package com.smartstudy.xxd.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.commonlib.entity.ConsultantsInfo;

/**
 * Created by yqy on 2017/12/28.
 */

public class OrganizationActivity extends UIActivity {

    private ConsultantsInfo teacherInfo;
    private SystemBarTintManager tintManager;
    private RelativeLayout topmyinfo;
    private int top_alpha = 0;
    private View view_bg;
    private ImageView topdefault_leftbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizatin);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //复位标题栏和状态栏
        initSysBar();
        topmyinfo.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
    }

    /**
     * 初始化状态栏
     */
    private void initSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            setTranslucentStatus(true);
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_top_color);
            tintManager.setStatusBarAlpha(top_alpha);
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            topmyinfo.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight(getApplicationContext());
            topmyinfo.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            view_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            topmyinfo.setPadding(topmyinfo.getPaddingLeft(), statusBarHeight, topmyinfo.getPaddingRight(), 0);
        }
    }

    @Override
    protected void initViewAndData() {
        teacherInfo = (ConsultantsInfo) getIntent().getSerializableExtra("teacherInfo");
        DisplayImageUtils.formatCircleImgUrl(this, teacherInfo.getOrganization().getLogo(), (ImageView) this.findViewById(R.id.iv_teacher_avatar));
        ((TextView) findViewById(R.id.tv_company)).setText(teacherInfo.getOrganization().getName());
        ((TextView) findViewById(R.id.tv_subtitle)).setText(teacherInfo.getOrganization().getSubtitle());
        ((TextView) findViewById(R.id.tv_intro)).setText(teacherInfo.getOrganization().getIntroduction());
        topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        this.topmyinfo = (RelativeLayout) findViewById(R.id.rlyt_top);
        this.view_bg = findViewById(R.id.view_bg);
        this.topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
        initSysBar();
        initTitleBar();
    }

    @Override
    public void initEvent() {
        topdefault_leftbutton.setOnClickListener(this);

//        //标题栏滑出背景图片过程中颜色渐变
//        rslmyinfo.setScrollViewListener(new ReboundScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(ReboundScrollView scrollView, int x, int y, int oldx, int oldy) {
//                int diff = kbv_user.getHeight() - topmyinfo.getHeight();
//                if (y > 0 && y <= diff) {
//                    top_alpha = y * 255 / diff;
//                    topdefault_centertitle.setVisibility(View.GONE);
//                    top_line.setVisibility(View.GONE);
//                } else if (y > diff) {
//                    top_alpha = 255;
//                    topdefault_centertitle.setVisibility(View.VISIBLE);
//                    top_line.setVisibility(View.VISIBLE);
//                } else {
//                    top_alpha = 0;
//                    topdefault_centertitle.setVisibility(View.GONE);
//                    top_line.setVisibility(View.GONE);
//                }
//                //翻转动画
//                if (top_alpha > 128) { // alpha > 0.5设置绿色图标
//                    if (isWhite) {
//                        topdefault_leftbutton.setImageResource(R.drawable.ic_go_back);
//                        ObjectAnimator animator = ObjectAnimator.ofFloat(topdefault_leftbutton, "alpha", 0.5f, 1);
//                        animator.setDuration(1000);
//                        animator.start();
//                    }
//                    isWhite = false;
//                } else { // 否则设置白色
//                    if (!isWhite) {
//                        topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
//                        ObjectAnimator animator = ObjectAnimator.ofFloat(topdefault_leftbutton, "alpha", 0.5f, 1);
//                        animator.setDuration(1000);
//                        animator.start();
//                    }
//                    isWhite = true;
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    tintManager.setStatusBarAlpha(top_alpha);
//                }
//                topmyinfo.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
//            }
//        });
//
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
        }
    }
}
