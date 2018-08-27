package com.smartstudy.xxd.ui.adapter.hotmajor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HotMajorContentHolder extends RecyclerView.ViewHolder {
    public TextView tvMajorName;
    public LinearLayout llTab;

    public HotMajorContentHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        tvMajorName = itemView.findViewById(R.id.tv_major_name);
        llTab = itemView.findViewById(R.id.ll_tab);
    }


}
