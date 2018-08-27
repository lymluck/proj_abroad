package com.smartstudy.xxd.ui.adapter.courseList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.xxd.R;

/**
 * Created by yqy on 2017/11/16.
 */

public class CourseHeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public View vBg;


    public CourseHeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView = (TextView) itemView.findViewById(R.id.tv_title);
        vBg=itemView.findViewById(R.id.view_bg);
    }
}
