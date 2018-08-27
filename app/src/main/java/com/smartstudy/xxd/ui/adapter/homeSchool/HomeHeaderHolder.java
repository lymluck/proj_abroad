package com.smartstudy.xxd.ui.adapter.homeSchool;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HomeHeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView moreView;

    public HomeHeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView = (TextView) itemView.findViewById(R.id.tv_type_name);
        moreView = (TextView) itemView.findViewById(R.id.tv_see_more);
    }
}
