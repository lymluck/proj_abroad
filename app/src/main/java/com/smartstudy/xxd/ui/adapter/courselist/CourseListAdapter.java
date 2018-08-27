package com.smartstudy.xxd.ui.adapter.courselist;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseGroup;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;

import java.util.List;

/**
 * Created by yqy on 2017/11/16.
 */

public class CourseListAdapter extends CommonAdapter<CourseGroup.Course> {

    public CourseListAdapter(Context context, int layoutId, List<CourseGroup.Course> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final CourseGroup.Course course, int position) {
        DisplayImageUtils.formatImgUrlNoHolder(mContext, course.getCoverUrl(),
            (ImageView) holder.getView(R.id.iv_course_cover));
        holder.setText(R.id.tv_course_name, course.getName());
        holder.setText(R.id.tv_course_see, String.format(mContext.getString(R.string.course_see),
            TextUtils.isEmpty(course.getPlayCount()) ? "0" : course.getPlayCount()));
        String count = "共 <font color='#008CF9'>" + (TextUtils.isEmpty(course.getSectionCount()) ? "0"
            : course.getSectionCount()) + "</font> 个课时";
        holder.setText(R.id.tv_course_count, Html.fromHtml(count));
        if (course.isBottom()) {
            holder.getView(R.id.v_line).setVisibility(View.GONE);
            holder.getView(R.id.v_space).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.v_line).setVisibility(View.VISIBLE);
            holder.getView(R.id.v_space).setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UApp.actionEvent(mContext, "15_A_course_cell");
                mContext.startActivity(new Intent(mContext, CourseDetailActivity.class)
                    .putExtra("id", course.getProductId())
                    .putExtra("courseCover", course.getCoverUrl()));
            }
        });
    }
}

