package com.smartstudy.xxd.ui.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.GpaDesInfo;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaDesTwoTreeItem extends TreeItem<GpaDesInfo.GpaDesInfoEntity> {
    @Override
    protected int initLayoutId() {
        return R.layout.item_gpa_two;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_item_name, data.getName());
        viewHolder.setText(R.id.tv_item_formula, data.getFormula());

        if (!TextUtils.isEmpty(data.getScoreTableImageUrl())) {
            final ImageView iv = viewHolder.getView(R.id.iv_gpa);
            iv.setAdjustViewBounds(true);
            DisplayImageUtils.displayImage(BaseApplication.appContext, data.getScoreTableImageUrl(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        iv.setImageBitmap(bitmap);
                    }
                });
            viewHolder.getView(R.id.iv_gpa).setVisibility(View.VISIBLE);
        } else {
            viewHolder.getView(R.id.iv_gpa).setVisibility(View.GONE);
        }
    }
}
