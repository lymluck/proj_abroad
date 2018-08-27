package com.smartstudy.xxd.ui.adapter.courselist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.xxd.R;

/**
 * Created by yqy on 2017/11/16.
 */

public class CourseListItemHolder  extends RecyclerView.ViewHolder {
    public ImageView ivCourseCover;
    public TextView tvCourseName;
    public TextView tvCourseSee;
    public LinearLayout item;
    public View vLine;
    public TextView tvCourseCount;

    public View getItem() {
        return item;
    }

    public CourseListItemHolder(View itemView) {
        super(itemView);
        this.item = (LinearLayout) itemView;
        initView();
    }

    private void initView() {
        ivCourseCover =(ImageView) itemView.findViewById(R.id.iv_course_cover);
        tvCourseName = (TextView) itemView.findViewById(R.id.tv_course_name);
        tvCourseSee = (TextView) itemView.findViewById(R.id.tv_course_see);
        item=(LinearLayout) itemView.findViewById(R.id.ll_item);
        vLine=item.findViewById(R.id.v_line);
        tvCourseCount=(TextView)item.findViewById(R.id.tv_course_count);
    }
}
