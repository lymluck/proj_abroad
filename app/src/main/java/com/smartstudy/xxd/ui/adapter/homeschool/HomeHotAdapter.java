package com.smartstudy.xxd.ui.adapter.homeschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.ui.activity.CollegeDetailActivity;
import com.smartstudy.xxd.ui.activity.HighSchoolDetailActivity;
import com.smartstudy.xxd.ui.activity.HighSchoolLibraryActivity;
import com.smartstudy.xxd.ui.activity.HotSchoolRankActivity;
import com.smartstudy.xxd.ui.fragment.UsCollegeFragment;
import com.smartstudy.xxd.ui.fragment.UsHighSchoolFragment;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 院校热门列表适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class HomeHotAdapter extends SectionedRecyclerViewAdapter<HomeHeaderHolder, HomeContentHolder, RecyclerView.ViewHolder> {


    public List<HomeHotListInfo> hotListInfos;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mScreenWidth;
    private Class mClazz;

    public HomeHotAdapter(Context context, List<HomeHotListInfo> listInfos, LayoutInflater mInflater, Class clazz) {
        this.mContext = context;
        this.hotListInfos = listInfos;
        this.mInflater = mInflater;
        this.mScreenWidth = ScreenUtils.getScreenWidth();
        this.mClazz = clazz;
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
                if ("浏览热度选校".equals(info.getTypeName())) {
                    if (UsCollegeFragment.class.equals(mClazz)) {
                        mContext.startActivity(new Intent(mContext, HotSchoolRankActivity.class)
                            .putExtra("from", "hottest")
                            .putExtra(TITLE, info.getTypeName()));
                    }
                } else if ("选校热度选校".equals(info.getTypeName())) {
                    if (UsCollegeFragment.class.equals(mClazz)) {
                        mContext.startActivity(new Intent(mContext, HotSchoolRankActivity.class)
                            .putExtra("from", "mostSelected")
                            .putExtra(TITLE, info.getTypeName()));
                    }
                } else {
                    //跳转到学校列表页面
                    if (UsHighSchoolFragment.class.equals(mClazz)) {
                        mContext.startActivity(new Intent(mContext, HighSchoolLibraryActivity.class));
                    }
                }
            }
        });
        if ("浏览热度选校".equals(hotListInfos.get(section).getTypeName())) {
            holder.titleView.setText(Html.fromHtml("<font color='#FE5E3E'>浏览热度</font>选校"));
        } else if ("选校热度选校".equals(hotListInfos.get(section).getTypeName())) {
            holder.titleView.setText(Html.fromHtml("<font color='#FF8A00'>选校热度</font>选校"));
        } else {
            holder.titleView.setText(hotListInfos.get(section).getTypeName());
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(HomeContentHolder holder, int section, int position) {
        HomeHotListInfo data = hotListInfos.get(section);
        final HomeHotInfo info = hotListInfos.get(section).getHotInfoList().get(position);
        switch (data.getTypeName()) {
            case "浏览热度选校":
                // 美本美研
                handleSchoolItem(holder, info, position, section);
                break;
            case "选校热度选校":
                // 美本美研
                handleSchoolItem(holder, info, position, section);
                break;
            case "热门学校":
                // 美高
                handleSchoolItem(holder, info, position, section);
                break;
            default:
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
                UApp.actionEvent(mContext, "8_A_specific_school_btn");
                //跳转到学校详情页面
                Bundle data = new Bundle();
                data.putString("id", info.getId());
                Intent toMoreDetails = new Intent();
                if (UsCollegeFragment.class.equals(mClazz)) {
                    toMoreDetails.setClass(mContext, CollegeDetailActivity.class);
                } else if (UsHighSchoolFragment.class.equals(mClazz)) {
                    toMoreDetails.setClass(mContext, HighSchoolDetailActivity.class);
                }
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
