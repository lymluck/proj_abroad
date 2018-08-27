package com.smartstudy.xxd.ui.adapter.courselist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartstudy.commonlib.ui.adapter.base.SectionedRecyclerViewAdapter;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseGroup;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;

import java.util.List;

/**
 * Created by yqy on 2017/11/16.
 */

public class CourseListAdapter extends SectionedRecyclerViewAdapter<CourseHeaderHolder, CourseListItemHolder, RecyclerView.ViewHolder> {


    public List<CourseGroup> groupList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mScreenWidth;

    public CourseListAdapter(Context context, List<CourseGroup> groupList, LayoutInflater mInflater) {
        this.mContext = context;
        this.groupList = groupList;
        this.mInflater = mInflater;
        this.mScreenWidth = ScreenUtils.getScreenWidth();
        context = null;
        groupList = null;
        mInflater = null;
    }

    @Override
    protected int getSectionCount() {
        return groupList != null ? groupList.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return groupList.get(section).getList() != null ? groupList.get(section).getList().size() : 0;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected CourseHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new CourseHeaderHolder(mInflater.inflate(R.layout.item_course_list_title, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected CourseListItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new CourseListItemHolder(mInflater.inflate(R.layout.item_course_list, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CourseHeaderHolder holder, int section) {
        final CourseGroup info = groupList.get(section);
        holder.titleView.setText(info.getGroup());
        if (section == 0) {
            holder.vBg.setVisibility(View.GONE);
        } else {
            holder.vBg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(CourseListItemHolder holder, int section, int position) {
        final CourseGroup.Course course = groupList.get(section).getList().get(position);
        DisplayImageUtils.formatImgUrlNoHolder(mContext, course.getCoverUrl(), holder.ivCourseCover);
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseSee.setText(String.format(mContext.getString(R.string.course_see),
                TextUtils.isEmpty(course.getPlayCount()) ? "0" : course.getPlayCount()));
        String count = "共 <font color='#008CF9'>" + (TextUtils.isEmpty(course.getSectionCount()) ? "0"
                : course.getSectionCount()) + "</font> 个课时";

        holder.tvCourseCount.setText(Html.fromHtml(count));
        if (position == getItemCount() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.actionEvent(mContext, "15_A_course_cell");
                mContext.startActivity(new Intent(mContext, CourseDetailActivity.class)
                        .putExtra("id", course.getProductId())
                        .putExtra("courseCover", course.getCoverUrl()));
            }
        });

    }


    public void destroy() {
        if (groupList != null) {
            groupList.clear();
            groupList = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }
    }
}

