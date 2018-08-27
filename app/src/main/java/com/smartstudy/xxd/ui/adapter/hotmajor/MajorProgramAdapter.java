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
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.MajorProgramInfo;
import com.smartstudy.xxd.ui.activity.MajorInfoActivity;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 热门研究生专业列表适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MajorProgramAdapter extends SectionedRecyclerViewAdapter<HotMajorHeaderHolder, HotMajorContentHolder, RecyclerView.ViewHolder> {


    private List<MajorProgramInfo.DirectionsInfo> datas;
    private Context mContext;
    private LayoutInflater mInflater;
    private int margin4;
    private int margin5;
    private int margin16;

    public MajorProgramAdapter(Context context, List<MajorProgramInfo.DirectionsInfo> datas, LayoutInflater mInflater) {
        this.mContext = context;
        this.datas = datas;
        this.mInflater = mInflater;
        margin4 = DensityUtils.dip2px(4f);
        margin5 = DensityUtils.dip2px(5f);
        margin16 = DensityUtils.dip2px(16f);
        context = null;
        datas = null;
        mInflater = null;
    }

    @Override
    protected int getSectionCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return datas.get(section).getCategories() != null ? datas.get(section).getCategories().size() : 0;
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
        final MajorProgramInfo.DirectionsInfo info = datas.get(section);
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
        holder.moreView.setVisibility(View.GONE);
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(HotMajorContentHolder holder, int section, int position) {
        MajorProgramInfo.DirectionsInfo.CategoriesInfo info = datas.get(section).getCategories().get(position);
        if (info != null) {
            handleMajorItem(holder, info, position);
        }
    }

    private void handleMajorItem(HotMajorContentHolder holder, final MajorProgramInfo.DirectionsInfo.CategoriesInfo info,
                                 int position) {
        //调整布局,3为列数
        int index = position % 3;
        if (index == 0) {
            holder.llTab.setPadding(margin16, 0, margin5, margin16);
        } else if (index == 2) {
            holder.llTab.setPadding(margin5, 0, margin16, margin16);
        } else {
            holder.llTab.setPadding(margin5, 0, margin5, margin16);
        }
        holder.tvMajorName.setText(info.getChineseName());
        holder.tvMajorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMajorInfo = new Intent(mContext, MajorInfoActivity.class);
                toMajorInfo.putExtra("majorId", info.getId() + "");
                toMajorInfo.putExtra(TITLE, info.getChineseName());
                mContext.startActivity(toMajorInfo);
            }
        });
    }

    public void destroy() {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }
    }
}
