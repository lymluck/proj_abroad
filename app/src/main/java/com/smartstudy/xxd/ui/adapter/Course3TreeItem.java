package com.smartstudy.xxd.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.ProgressPieView;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseContentInfo;

/**
 * Created by baozi on 2016/12/8.
 */
public class Course3TreeItem extends TreeItem<CourseContentInfo.ChaptersEntity.SectionsEntity> {

    @Override
    public int initLayoutId() {
        return R.layout.item_course_three;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder) {
        final TextView tv_type_name = holder.getView(R.id.tv_type_name);
        tv_type_name.setText(data.getName());
        holder.setText(R.id.tv_time, data.getDuration());
        if (getItemManager() != null && getItemManager().getItemPosition(this)
                == getItemManager().getItemPosition(getParentItem()) + getParentItem().getChildCount()) {
            holder.getView(R.id.line_course).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.line_course).setVisibility(View.GONE);
        }
        ProgressPieView ppv_course = holder.getView(R.id.ppv_course);
        ppv_course.setProgress(data.getProgress());
        ImageView iv_status = holder.getView(R.id.iv_status);
        if (data.getProgress() == 100) {
            iv_status.setVisibility(View.VISIBLE);
            ppv_course.setVisibility(View.GONE);
            iv_status.setImageResource(R.drawable.ic_done);
        } else if (data.getProgress() == 0) {
            iv_status.setVisibility(View.VISIBLE);
            ppv_course.setVisibility(View.GONE);
            iv_status.setImageResource(R.drawable.ic_video_0);
        } else {
            iv_status.setVisibility(View.GONE);
            ppv_course.setVisibility(View.VISIBLE);
        }

        if (data.getId().equals(SPCacheUtils.get("nowSectionId", ""))) {
            tv_type_name.setTextColor(BaseApplication.appContext.getResources().getColor(R.color.app_main_color));
        } else {
            tv_type_name.setTextColor(BaseApplication.appContext.getResources().getColor(R.color.app_text_color1));
        }
    }

}
