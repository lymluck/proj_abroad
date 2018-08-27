package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.clipimagelayout.ClipImageLayout;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;

import java.io.File;

import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.smartstudy.commonlib.ui.customview.clipimagelayout.ClipImageLayout.CIRCLE;
import static com.smartstudy.commonlib.ui.customview.clipimagelayout.ClipImageLayout.SQUARE;

public class ClipPictureActivity extends BaseActivity {

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
            ToastUtils.showToast(getString(R.string.picture_load_failure));
            return;
        }
        DisplayImageUtils.displayImage(ClipPictureActivity.this, path, new SimpleTarget<Bitmap>(480, 640) {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if (bitmap == null) {
                    ToastUtils.showToast(getString(R.string.picture_load_failure));
                    return;
                }
                mClipImageLayout.setBitmap(bitmap);
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
                public void onResourceReady(@NonNull File file, @Nullable Transition<? super File> transition) {
                    Intent intent = new Intent();
                    intent.putExtra("path", file.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

}
