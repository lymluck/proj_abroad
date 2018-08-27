package com.smartstudy.xxd.ui.adapter.homeschool;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.customview.SquareLinearLayout;
import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HomeContentHolder extends RecyclerView.ViewHolder {
    public TextView schoolName;
    public ImageView schoolLogo;
    public SquareLinearLayout sllyt_content;
    public LinearLayout item;

    public View getItem() {
        return item;
    }

    public HomeContentHolder(View itemView) {
        super(itemView);
        this.item = (LinearLayout) itemView;
        initView();
    }

    private void initView() {
        sllyt_content = (SquareLinearLayout) itemView.findViewById(R.id.sllyt_content);
        schoolName = (TextView) itemView.findViewById(R.id.tv_school_name);
        schoolLogo = (ImageView) itemView.findViewById(R.id.iv_school_logo);
    }


}
