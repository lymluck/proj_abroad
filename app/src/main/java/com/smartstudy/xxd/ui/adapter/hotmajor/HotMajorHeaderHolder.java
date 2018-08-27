package com.smartstudy.xxd.ui.adapter.hotmajor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HotMajorHeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView moreView;
    public View lineTitle;

    public HotMajorHeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView = itemView.findViewById(R.id.tv_type_name);
        moreView = itemView.findViewById(R.id.tv_see_more);
        lineTitle = itemView.findViewById(R.id.line_title);
    }
}
