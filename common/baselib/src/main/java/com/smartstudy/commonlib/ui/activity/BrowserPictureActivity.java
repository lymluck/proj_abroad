package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.mvp.contract.BrowerPictureContract;
import com.smartstudy.commonlib.mvp.presenter.BrowerPicturePresenter;
import com.smartstudy.commonlib.task.ScanQrcodeTask;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.ImgPreviewAdapter;
import com.smartstudy.commonlib.ui.customview.PhotoViewPager;
import com.smartstudy.commonlib.ui.dialog.OptionsPopupDialog;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.router.Router;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BrowserPictureActivity extends UIActivity implements BrowerPictureContract.View {

    /**
     * 存放所有图片的路径
     */
    private List<String> mPathList;
    private int mNowIndex;

    private PhotoViewPager mVp;
    private OptionsPopupDialog mDialog;
    private String qrCodeUrl;
    private BrowerPictureContract.Presenter presenter;
    private WeakHandler mHandler;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_picture);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        mPathList = data.getStringArrayListExtra("pathList");
        mNowIndex = data.getIntExtra("index", 0);
        new BrowerPicturePresenter(this);
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        qrCodeUrl = (String) msg.obj;
                        mDialog.addItem("识别图中二维码");
                        mDialog.refreshItem();
                        break;
                    case ParameterUtils.EMPTY_WHAT:
                        final String url = (String) msg.obj;
                        //识别图片中是否有二维码
                        scanQrCode(url);
                        //弹出菜单
                        if (items == null) {
                            items = new ArrayList<>();
                        }
                        items.clear();
                        items.add("分享给朋友");
                        items.add("保存图片");
                        mDialog = OptionsPopupDialog.newInstance(BrowserPictureActivity.this, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                            @Override
                            public void onOptionsItemClicked(int which) {
                                onItemLongClick(which, url);
                            }
                        });
                        mDialog.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mVp = findViewById(R.id.vp_img);
        mVp.setAdapter(new ImgPreviewAdapter(this, mVp, mHandler, mPathList));
        mVp.setCurrentItem(mNowIndex);
        mVp.setOffscreenPageLimit(1);
    }

    @Override
    protected void doClick(View v) {
    }

    private void onItemLongClick(int which, String url) {
        switch (which) {
            case 0:
                //分享图片
                ShareUtils.showShare(this, "", "", "", url, null);
                break;
            case 1:
                //保存图片
                DisplayImageUtils.displayImage(BrowserPictureActivity.this, url, new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        presenter.savePic(resource);
                    }

                    @Override
                    public void onStart() {
                        DisplayImageUtils.resumeRequest(BrowserPictureActivity.this);
                        super.onStart();
                    }

                    @Override
                    public void onStop() {
                        DisplayImageUtils.pauseRequest(BrowserPictureActivity.this);
                        super.onStop();
                    }
                });
                break;
            case 2:
                //识别二维码
                Router.build("showWebView").with("web_url", qrCodeUrl).with("url_action", "get").go(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(BrowerPictureContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    private void scanQrCode(String url) {
        DisplayImageUtils.downloadImageFile(this, url, new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                new ScanQrcodeTask(BrowserPictureActivity.this, resource.getPath(), mHandler).execute();
            }

            @Override
            public void onStart() {
                DisplayImageUtils.resumeRequest(BrowserPictureActivity.this);
                super.onStart();
            }

            @Override
            public void onStop() {
                DisplayImageUtils.pauseRequest(BrowserPictureActivity.this);
                super.onStop();
            }
        });
    }
}
