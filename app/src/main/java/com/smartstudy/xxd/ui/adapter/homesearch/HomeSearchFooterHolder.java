package com.smartstudy.xxd.ui.adapter.homesearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HomeSearchFooterHolder extends RecyclerView.ViewHolder {
    public TextView tv_type_more;

    public HomeSearchFooterHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        tv_type_more = (TextView) itemView.findViewById(R.id.tv_type_more);
    }
}
