package com.smartstudy.xxd.ui.adapter.homeSchool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.SectionedRecyclerViewAdapter;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.ui.activity.SchoolDetailActivity;
import com.smartstudy.xxd.ui.activity.SchoolListActivity;

import java.util.List;

/**
 * Created by louis on 17/5/19.
 */

public class HomeHotAdapter extends SectionedRecyclerViewAdapter<HomeHeaderHolder, HomeContentHolder, RecyclerView.ViewHolder> {


    public List<HomeHotListInfo> hotListInfos;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mScreenWidth;

    public HomeHotAdapter(Context context, List<HomeHotListInfo> listInfos, LayoutInflater mInflater) {
        this.mContext = context;
        this.hotListInfos = listInfos;
        this.mInflater = mInflater;
        this.mScreenWidth = ScreenUtils.getScreenWidth();
        context = null;
        listInfos = null;
        mInflater = null;
    }

    @Override
    protected int getSectionCount() {
        return hotListInfos != null ? hotListInfos.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return hotListInfos.get(section).getHotInfoList() != null ? hotListInfos.get(section).getHotInfoList().size() : 0;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HomeHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HomeHeaderHolder(mInflater.inflate(R.layout.item_home_school_title, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HomeContentHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new HomeContentHolder(mInflater.inflate(R.layout.item_home_school_list, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HomeHeaderHolder holder, final int section) {
        final HomeHotListInfo info = hotListInfos.get(section);
        holder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("热门学校".equals(info.getTypeName())) {
                    //跳转到学校列表页面
                    mContext.startActivity(new Intent(mContext, SchoolListActivity.class));
                }
            }
        });
        holder.titleView.setText(hotListInfos.get(section).getTypeName());
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(HomeContentHolder holder, int section, int position) {
        HomeHotListInfo data = hotListInfos.get(section);
        final HomeHotInfo info = hotListInfos.get(section).getHotInfoList().get(position);
        switch (data.getTypeName()) {
            case "热门学校":
                handleSchoolItem(holder, info, position, section);
                break;
        }
    }

    private void handleSchoolItem(HomeContentHolder holder, final HomeHotInfo info, int position, int section) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.sllyt_content.getLayoutParams();
        params.width = (mScreenWidth - DensityUtils.dip2px(16) * 2 - DensityUtils.dip2px(10) * 2) / 3;
        params.height = (mScreenWidth - DensityUtils.dip2px(16) * 2 - DensityUtils.dip2px(10) * 2) / 3;
        //调整布局,3为列数
        int index = position % 3;
        if (index == 0) {
            params.leftMargin = DensityUtils.dip2px(16);
            params.rightMargin = DensityUtils.dip2px(5);
            params.gravity = Gravity.LEFT;
            holder.sllyt_content.setLayoutParams(params);
            holder.schoolName.setPadding(DensityUtils.dip2px(16), 0, DensityUtils.dip2px(5), 0);
        } else if (index == 2 || position == getItemCountForSection(section) - 1) {
            params.rightMargin = DensityUtils.dip2px(16);
            params.leftMargin = DensityUtils.dip2px(5);
            params.gravity = Gravity.RIGHT;
            holder.sllyt_content.setLayoutParams(params);
            holder.schoolName.setPadding(DensityUtils.dip2px(5), 0, DensityUtils.dip2px(16), 0);
        } else {
            params.rightMargin = DensityUtils.dip2px(5);
            params.leftMargin = DensityUtils.dip2px(5);
            params.gravity = Gravity.CENTER;
            holder.sllyt_content.setLayoutParams(params);
            holder.schoolName.setPadding(DensityUtils.dip2px(5), 0, DensityUtils.dip2px(5), 0);
        }
        holder.schoolName.setText(info.getChineseName());
        DisplayImageUtils.formatCircleImgUrl(BaseApplication.appContext, info.getLogo(), holder.schoolLogo);
        holder.schoolLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到学校详情页面
                Bundle data = new Bundle();
                data.putString("id", info.getId());
                Intent toMoreDetails = new Intent(mContext, SchoolDetailActivity.class);
                toMoreDetails.putExtras(data);
                mContext.startActivity(toMoreDetails);
            }
        });
    }

    public void destroy() {
        if (hotListInfos != null) {
            hotListInfos.clear();
            hotListInfos = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }
    }
}
