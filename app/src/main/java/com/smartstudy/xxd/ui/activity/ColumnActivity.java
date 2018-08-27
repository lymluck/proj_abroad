package com.smartstudy.xxd.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.OnWebBaseListener;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.HeadZoomScrollView;
import com.smartstudy.commonlib.ui.fragment.MyWebviewFragment;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ColumnInfo;
import com.smartstudy.xxd.mvp.contract.ColumnContract;
import com.smartstudy.xxd.mvp.presenter.ColumnPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

public class ColumnActivity extends BaseActivity implements ColumnContract.View, OnWebBaseListener {

    private RelativeLayout rlTop;
    private ImageView ivCover;
    private View lineTop;
    private ImageView ivBack;
    private LinearLayout llAvatar;
    private LinearLayout llUserInfo;
    private TextView tvLike;
    private TextView tvCollect;
    private TextView tvComment;
    private LinearLayout llBottomTab;

    private int topAlpha = 0;
    private boolean isWhite = true;
    private ColumnContract.Presenter presenter;
    private String newId;
    private boolean isLike;
    private boolean isCollect;
    private int likeCount;
    private boolean isShowing = true;
    private MyWebviewFragment webViewFragment;
    private WeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rlTop.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler = null;
        }
    }

    @Override
    protected void initViewAndData() {
        this.rlTop = findViewById(R.id.rl_top);
        this.ivCover = findViewById(R.id.iv_cover);
        this.lineTop = findViewById(R.id.line_top);
        this.llAvatar = findViewById(R.id.ll_avatar);
        this.llUserInfo = findViewById(R.id.ll_user_info);
        this.ivBack = findViewById(R.id.iv_back);
        this.ivBack.setImageResource(R.drawable.ic_go_back_white);
        this.tvCollect = findViewById(R.id.tv_collect);
        this.tvComment = findViewById(R.id.tv_comment);
        this.tvLike = findViewById(R.id.tv_like);
        this.llBottomTab = findViewById(R.id.ll_bottom_tab);
        initTitleBar();
        newId = getIntent().getStringExtra("id");
        new ColumnPresenter(this);
        presenter.getColumn(newId);
    }


    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight();
            rlTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            findViewById(R.id.view_bg).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            rlTop.setPadding(rlTop.getPaddingLeft(), statusBarHeight, rlTop.getPaddingRight(), 0);
        }
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        findViewById(R.id.tv_like).setOnClickListener(this);
        findViewById(R.id.tv_collect).setOnClickListener(this);
        findViewById(R.id.tv_comment).setOnClickListener(this);
        HeadZoomScrollView hsvContent = findViewById(R.id.hsv_content);
        //标题栏滑出背景图片过程中颜色渐变
        hsvContent.setOnScrollListener(new HeadZoomScrollView.OnScrollListener() {

            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int diff = ivCover.getHeight() - rlTop.getHeight();
                if (scrollY > 0 && scrollY <= diff) {
                    topAlpha = scrollY * 255 / diff;
                    llAvatar.setVisibility(View.GONE);
                    llUserInfo.setVisibility(View.GONE);
                    lineTop.setVisibility(View.GONE);
                } else if (scrollY > diff) {
                    topAlpha = 255;
                    llAvatar.setVisibility(View.VISIBLE);
                    llUserInfo.setVisibility(View.VISIBLE);
                    lineTop.setVisibility(View.VISIBLE);
                } else {
                    topAlpha = 0;
                    llAvatar.setVisibility(View.GONE);
                    llUserInfo.setVisibility(View.GONE);
                    lineTop.setVisibility(View.GONE);
                }
                //翻转动画
                // alpha > 0.5设置绿色图标
                if (topAlpha > 128) {
                    if (isWhite) {
                        ivBack.setImageResource(R.drawable.ic_go_back);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(ivBack, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = false;
                } else { // 否则设置白色
                    if (!isWhite) {
                        ivBack.setImageResource(R.drawable.ic_go_back_white);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(ivBack, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = true;
                }
                rlTop.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
                if (oldScrollY > scrollY && (oldScrollY - scrollY > 20) && !isShowing) {
                    //向下
                    isShowing = true;
                    llBottomTab.animate().translationY(0).start();
                } else if (oldScrollY < scrollY && (scrollY - oldScrollY > 20) && isShowing) {
                    //向上
                    isShowing = false;
                    llBottomTab.animate().translationY(llBottomTab.getHeight()).start();
                }
            }

            @Override
            public void atBottom() {
                llBottomTab.animate().translationY(0).start();
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_like:
                if (isLike) {
                    presenter.disLikeThis(newId);
                } else {
                    presenter.likeThis(newId);
                }
                break;
            case R.id.tv_collect:
                if (isCollect) {
                    presenter.disCollectThis(newId);
                } else {
                    presenter.collectThis(newId);
                }
                break;
            case R.id.tv_comment:
                startActivity(new Intent(ColumnActivity.this, CommentActivity.class)
                    .putExtra("id", newId));
                break;
            default:
                break;
        }
    }

    @Override
    public void showColumn(ColumnInfo info) {
        if (mHandler == null) {
            mHandler = new WeakHandler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.llyt_loading).setVisibility(View.GONE);
            }
        }, 500);
        findViewById(R.id.ll_column).setVisibility(View.VISIBLE);
        llBottomTab.setVisibility(View.VISIBLE);
        ImageView ivUserAvatar = findViewById(R.id.iv_user_avatar);
        ivUserAvatar.setVisibility(View.VISIBLE);
        DisplayImageUtils.formatPersonImgUrl(this, info.getAuthor().getAvatar(),
            (ImageView) findViewById(R.id.iv_user_avatar_top));
        DisplayImageUtils.formatPersonImgUrl(this, info.getAuthor().getAvatar(), ivUserAvatar);
        ((TextView) findViewById(R.id.tv_user_name_top)).setText(info.getAuthor().getName());
        ((TextView) findViewById(R.id.tv_user_name)).setText(info.getAuthor().getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(info.getPublishTime());
            String time = format.format(date);
            ((TextView) findViewById(R.id.tv_time_top)).setText(time);
            ((TextView) findViewById(R.id.tv_time)).setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.tv_title)).setText(info.getTitle());
        ((TextView) findViewById(R.id.tv_see)).setText(info.getVisitCount() + " 浏览");
        DisplayImageUtils.formatImgUrl(this, info.getCoverUrl(), ivCover);

        if (webViewFragment == null) {
            webViewFragment = new MyWebviewFragment();
        }
        Bundle data = new Bundle();
        data.putString(WEBVIEW_URL, info.getContent());
        data.putString(WEBVIEW_ACTION, "html");
        webViewFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.web_content, webViewFragment);
        transaction.commit();

        isCollect = info.isCollected();
        setCollectView();
        isLike = info.isLiked();
        likeCount = info.getLikedCount();
        setLikeView();
        tvComment.setText("评论 " + info.getCommentsCount());
    }

    private void setLikeView() {
        tvLike.setText("赞 " + likeCount);
        tvLike.setTextColor(isLike ? getResources().getColor(R.color.app_main_color)
            : getResources().getColor(R.color.app_text_color));
        tvLike.setCompoundDrawablesWithIntrinsicBounds(0, isLike ? R.drawable.ic_column_like_select
            : R.drawable.ic_column_like_normal, 0, 0);
    }

    private void setCollectView() {
        tvCollect.setText(isCollect ? "已收藏" : "收藏");
        tvCollect.setTextColor(isCollect ? getResources().getColor(R.color.app_main_color)
            : getResources().getColor(R.color.app_text_color));
        tvCollect.setCompoundDrawablesWithIntrinsicBounds(0, isCollect ? R.drawable.ic_column_collect_select
            : R.drawable.ic_column_collect_normal, 0, 0);
    }

    @Override
    public void likeSuccess() {
        isLike = true;
        likeCount++;
        showTip(null, "点赞成功！");
        setLikeView();
    }

    @Override
    public void disLikesuccess() {
        isLike = false;
        likeCount--;
        showTip(null, "取消点赞！");
        setLikeView();
    }

    @Override
    public void collectSuccess() {
        isCollect = true;
        showTip(null, "收藏成功！");
        setCollectView();
    }

    @Override
    public void disCollectSuccess() {
        isCollect = false;
        showTip(null, "取消收藏！");
        setCollectView();
    }

    @Override
    public void setPresenter(ColumnContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void showWebView(String webUrl) {
        Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
        toMoreDetails.putExtra(WEBVIEW_URL, webUrl);
        toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
        startActivity(toMoreDetails);
    }
}
