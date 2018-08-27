package com.smartstudy.xxd.ui.adapter.homeSearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HomeSearchHeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;

    public HomeSearchHeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView = (TextView) itemView.findViewById(R.id.tv_type_name);
    }
}
