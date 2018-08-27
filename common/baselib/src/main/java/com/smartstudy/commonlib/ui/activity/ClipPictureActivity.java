package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customView.clipImageLayout.ClipImageLayout;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;

import java.io.File;

import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.smartstudy.commonlib.ui.customView.clipImageLayout.ClipImageLayout.CIRCLE;
import static com.smartstudy.commonlib.ui.customView.clipImageLayout.ClipImageLayout.SQUARE;

public class ClipPictureActivity extends UIActivity {

    private TextView topdefault_centertitle;

    private ClipImageLayout mClipImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(FEATURE_NO_TITLE);// 去掉标题栏
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_clip_picture);
    }

    @Override
    protected void initViewAndData() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_centertitle.setText(getString(R.string.move_and_zoom));
        Intent intent = getIntent();
        String type = intent.getStringExtra("clipType");
        if (type.equals(CIRCLE)) {
            mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout_circle);
        } else if (type.equals(SQUARE)) {
            mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout_square);
        }
        mClipImageLayout.setVisibility(VISIBLE);
        String path = intent.getStringExtra("path");
        if (isEmpty(path) || !(new File(path).exists())) {
            ToastUtils.showToast(this, getString(R.string.picture_load_failure));
            return;
        }
        DisplayImageUtils.displayImage(ClipPictureActivity.this, path, new SimpleTarget<Bitmap>(600, 600) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource == null) {
                    ToastUtils.showToast(ClipPictureActivity.this, getString(R.string.picture_load_failure));
                    return;
                }
                mClipImageLayout.setBitmap(resource);
            }
        });
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.id_action_clip).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.id_action_clip) {
            Bitmap bitmap = mClipImageLayout.clip();
            DisplayImageUtils.displayImageFile(ClipPictureActivity.this, bitmap, new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    Intent intent = new Intent();
                    intent.putExtra("path", resource.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}
