package com.smartstudy.xxd.ui.adapter.homesearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.xxd.R;


/**
 * Created by louis on 17/5/19.
 */

public class HomeSearchContentHolder extends RecyclerView.ViewHolder {
    public LinearLayout item_school;
    public LinearLayout item_highschool;
    public LinearLayout item_course;
    public LinearLayout item_news;
    public LinearLayout item_qa;
    public TextView tv_school_name;
    public TextView tv_school_eg_name;
    public TextView tv_highschool_name;
    public TextView tv_highschool_eg_name;
    public TextView tv_course_name;
    public ImageView iv_school_logo;
    public ImageView iv_course_cover;
    public ImageView iv_news_cover;
    public ImageView iv_asker;
    public RatingBar rb_course_rate;
    public TextView tv_course_rate;
    public TextView tv_course_see;
    public TextView tv_news_title;
    public TextView tv_news_tag;
    public TextView tv_news_see;
    public TextView tv_qa;
    public TextView tv_qa_time;
    public TextView tv_qa_see;
    public TextView tv_qa_name;
    public TextView tv_qa_default_name;


    public HomeSearchContentHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        tv_school_name = (TextView) itemView.findViewById(R.id.tv_school_name);
        tv_school_eg_name = (TextView) itemView.findViewById(R.id.tv_school_eg_name);
        tv_highschool_name = (TextView) itemView.findViewById(R.id.tv_highschool_name);
        tv_highschool_eg_name = (TextView) itemView.findViewById(R.id.tv_highschool_egname);
        tv_course_rate = (TextView) itemView.findViewById(R.id.tv_course_rate);
        rb_course_rate = (RatingBar) itemView.findViewById(R.id.rb_course_rate);
        tv_course_see = (TextView) itemView.findViewById(R.id.tv_course_see);
        tv_course_name = (TextView) itemView.findViewById(R.id.tv_course_name);
        tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
        tv_news_tag = (TextView) itemView.findViewById(R.id.tv_news_tag);
        tv_news_see = (TextView) itemView.findViewById(R.id.tv_news_see);
        tv_qa = (TextView) itemView.findViewById(R.id.tv_qa);
        tv_qa_default_name = (TextView) itemView.findViewById(R.id.tv_qa_default_name);
        tv_qa_time = (TextView) itemView.findViewById(R.id.tv_qa_time);
        tv_qa_see = (TextView) itemView.findViewById(R.id.tv_qa_see);
        tv_qa_name = (TextView) itemView.findViewById(R.id.tv_qa_name);
        iv_asker = (ImageView) itemView.findViewById(R.id.iv_asker);
        iv_news_cover = (ImageView) itemView.findViewById(R.id.iv_news_cover);
        iv_school_logo = (ImageView) itemView.findViewById(R.id.iv_school_logo);
        iv_course_cover = (ImageView) itemView.findViewById(R.id.iv_course_cover);
        item_school = (LinearLayout) itemView.findViewById(R.id.item_school);
        item_highschool = (LinearLayout) itemView.findViewById(R.id.item_highschool);
        item_course = (LinearLayout) itemView.findViewById(R.id.item_course);
        item_news = (LinearLayout) itemView.findViewById(R.id.item_news);
        item_qa = (LinearLayout) itemView.findViewById(R.id.item_qa);
    }


}
