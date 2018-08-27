package com.smartstudy.xxd.ui.adapter.hotmajor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.ui.adapter.base.SectionedRecyclerViewAdapter;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HotMajorInfo;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;
import com.smartstudy.xxd.ui.activity.SpecialListActivity;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 院校热门列表适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class FindMajorAdapter extends SectionedRecyclerViewAdapter<HotMajorHeaderHolder, HotMajorContentHolder, RecyclerView.ViewHolder> {


    public List<HotMajorInfo> hotListInfos;
    private Context mContext;
    private LayoutInflater mInflater;
    private int margin4;
    private int margin5;
    private int margin16;

    public FindMajorAdapter(Context context, List<HotMajorInfo> listInfos, LayoutInflater mInflater) {
        this.mContext = context;
        this.hotListInfos = listInfos;
        this.mInflater = mInflater;
        margin4 = DensityUtils.dip2px(4f);
        margin5 = DensityUtils.dip2px(5f);
        margin16 = DensityUtils.dip2px(16f);
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
        return hotListInfos.get(section).getMajors() != null ? hotListInfos.get(section).getMajors().size() : 0;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HotMajorHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HotMajorHeaderHolder(mInflater.inflate(R.layout.item_find_major_title, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HotMajorContentHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new HotMajorContentHolder(mInflater.inflate(R.layout.item_find_major_list, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HotMajorHeaderHolder holder, int section) {
        final HotMajorInfo info = hotListInfos.get(section);
        LinearLayout.LayoutParams line = (LinearLayout.LayoutParams) holder.lineTitle.getLayoutParams();
        if (section == 0) {
            holder.lineTitle.setVisibility(View.GONE);
            line.topMargin = 0;
        } else {
            holder.lineTitle.setVisibility(View.VISIBLE);
            line.topMargin = margin4;
        }
        holder.lineTitle.setLayoutParams(line);
        holder.titleView.setText(info.getName());
        holder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到专业库页面
                UApp.actionEvent(mContext, "8_A_professional_library_btn");
                mContext.startActivity(new Intent(mContext, SpecialListActivity.class)
                    .putExtra("flag", "home")
                    .putExtra(TITLE, info.getName()));
            }
        });
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(HotMajorContentHolder holder, int section, int position) {
        HotMajorInfo.MajorsInfo info = hotListInfos.get(section).getMajors().get(position);
        if (info != null) {
            handleMajorItem(holder, info, position);
        }
    }

    private void handleMajorItem(HotMajorContentHolder holder, final HotMajorInfo.MajorsInfo info, int position) {
        //调整布局,3为列数
        int index = position % 3;
        if (index == 0) {
            holder.llTab.setPadding(margin16, 0, margin5, margin16);
        } else if (index == 2) {
            holder.llTab.setPadding(margin5, 0, margin16, margin16);
        } else {
            holder.llTab.setPadding(margin5, 0, margin5, margin16);
        }
        holder.tvMajorName.setText(info.getName());
        holder.tvMajorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UApp.actionEvent(mContext, "13_A_professional_cell");
                Intent toMoreDetails = new Intent(mContext, ShowWebViewActivity.class);
                toMoreDetails.putExtra(WEBVIEW_URL, String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_SPE_INTRO), info.getId() + ""));
                toMoreDetails.putExtra(TITLE, info.getName());
                toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
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
