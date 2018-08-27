package io.rong.imkit.plugin.image;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import io.rong.imkit.R;
import io.rong.imkit.plugin.SystemBarTintManager;
import me.kareluo.imaging.IMGEditActivity;

import static me.kareluo.imaging.IMGEditActivity.REQUEST_EDIT;


/**
 * @author louis
 * @date on 2018/3/6
 * @describe 预览图片
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ImPicPreviewActivity extends PicturePreviewActivity {

    private SystemBarTintManager tintManager;
    private ArrayList<PictureSelectorActivity.PicItem> mItemList;
    private int mCurrentIndex;
    private HackyViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        initView();
    }

    private void initView() {
        this.mViewPager = findViewById(R.id.viewpager);
        TextView tv_edit = findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditImg();
            }
        });
        this.mCurrentIndex = this.getIntent().getIntExtra("index", 0);
        if (this.mItemList == null) {
            this.mItemList = PictureSelectorActivity.PicItemHolder.itemList;
        }
    }

    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.rc_top_color);
        }
    }

    /**
     * 透明状态栏
     *
     * @param on
     */
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void toEditImg() {
        startActivityForResult(new Intent(this, IMGEditActivity.class)
                .putExtra("showDialog", false)
                .putExtra("uri", Uri.parse("file://" + PictureSelectorActivity.PicItemHolder.itemList.get(mCurrentIndex).uri)), REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT:
                PictureSelectorActivity.PicItemHolder.itemList.get(mCurrentIndex).uri = data.getStringExtra("path");
                mItemList.get(mCurrentIndex).uri = data.getStringExtra("path");
                mViewPager.setAdapter(mViewPager.getAdapter());
                mViewPager.getAdapter().notifyDataSetChanged();
                mViewPager.setCurrentItem(mCurrentIndex);
                break;
            default:
                break;
        }
    }
}
