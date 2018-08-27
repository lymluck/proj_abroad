package com.smartstudy.xxd.ui.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HighOptionsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/8
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class PopWindowHighSchoolRank extends PopupWindow {

    private Context mContext;
    private TextView v;
    private String rankCategoryId;
    private String rankCategoryValue;
    private String schoolRankValue;
    private List<IdNameInfo> schoolRankNameList;
    private List<IdNameInfo> schoolRankValueList;

    public String getRankCategoryValue() {
        return rankCategoryValue;
    }

    public void setRankCategoryValue(String rankCategoryValue) {
        this.rankCategoryValue = rankCategoryValue;
    }

    public String getRankCategoryId() {
        return rankCategoryId;
    }

    public void setRankCategoryId(String rankCategoryId) {
        this.rankCategoryId = rankCategoryId;
    }

    public String getSchoolRankValue() {
        return schoolRankValue;
    }

    public void setSchoolRankValue(String schoolRankValue) {
        this.schoolRankValue = schoolRankValue;
    }

    @SuppressLint("InflateParams")
    public PopWindowHighSchoolRank(final Activity context, HighOptionsInfo highOptionsInfo, String schoolRankName,
                                   String schoolRankValue) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popwindow_high_school_rank, null);
        initData(contentView, highOptionsInfo, schoolRankName, schoolRankValue);
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.popupwindow_city);

    }

    public void initData(View contentView, HighOptionsInfo highOptionsInfo, String rankCategoryId, String schoolRankValue) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        if (!TextUtils.isEmpty(rankCategoryId)) {
            this.rankCategoryId = rankCategoryId;
        }
        if (!TextUtils.isEmpty(schoolRankValue)) {
            this.schoolRankValue = schoolRankValue;
        }
        RecyclerView lvSchoolTitle = (RecyclerView) contentView.findViewById(R.id.lv_school_title);
        RecyclerView llytRankRype = (RecyclerView) contentView.findViewById(R.id.lv_school_value);
        initClick(contentView);
        initSchoolTitle(lvSchoolTitle, llytRankRype, highOptionsInfo, rankCategoryId, schoolRankValue);
        if (!TextUtils.isEmpty(rankCategoryId)) {
            for (IdNameInfo info : highOptionsInfo.getRanks().getOptions()) {
                if ((info.getId() == null ? "" : info.getId()).equals(rankCategoryId)) {
                    initStayType(llytRankRype, info.getSubOptions().getRankRange(), schoolRankValue);
                }
            }
        }
    }


    private void initClick(final View contentView) {

    }


    private void initSchoolTitle(final RecyclerView lvSchoolTitle, final RecyclerView llytRankType,
                                 final HighOptionsInfo highOptionsInfo, String rankId, final String schoolRankValue) {
        lvSchoolTitle.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        lvSchoolTitle.setLayoutManager(mLayoutManager);
        schoolRankNameList = new ArrayList<>();
        schoolRankNameList.addAll(highOptionsInfo.getRanks().getOptions());
        changeSelected(schoolRankNameList, rankId);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.layout_high_school_rank_item, schoolRankNameList) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                holder.setText(R.id.tv_high_name, idNameInfo.getName());
                if (idNameInfo.is_selected()) {
                    holder.setTextColor(R.id.tv_high_name, Color.parseColor("#26343F"));
                    holder.getView(R.id.tv_high_name).setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    holder.setTextColor(R.id.tv_high_name, Color.parseColor("#949BA1"));
                    holder.getView(R.id.tv_high_name).setBackgroundColor(Color.parseColor("#F5F6F7"));
                }
            }
        };
        lvSchoolTitle.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                rankCategoryId = schoolRankNameList.get(position).getId();
                rankCategoryValue = schoolRankNameList.get(position).getName();
                changeSelected(schoolRankNameList, schoolRankNameList.get(position).getId());
                mAdapter.notifyDataSetChanged();
                initStayType(llytRankType, schoolRankNameList.get(position).getSubOptions().getRankRange(), schoolRankValue);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initStayType(RecyclerView llytRankType, IdNameInfo.SubOptions.RankRange rankRange, String type) {
        llytRankType.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        llytRankType.setLayoutManager(mLayoutManager);
        if (schoolRankValueList == null) {
            schoolRankValueList = new ArrayList<>();
        } else {
            schoolRankValueList.clear();
        }
        schoolRankValueList.addAll(rankRange.getOptions());
//        changeSelected(schoolRankValueList, type);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_high_school_rank, schoolRankValueList) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                holder.setText(R.id.tv_list_name, idNameInfo.getName());
                if (idNameInfo.is_selected()) {
                    holder.setTextColor(R.id.tv_list_name, mContext.getResources().getColor(R.color.app_main_color));
                    holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                } else {
                    holder.setTextColor(R.id.tv_list_name, mContext.getResources().getColor(R.color.app_text_color2));
                    holder.getView(com.smartstudy.commonlib.R.id.iv_choose).setVisibility(View.GONE);
                }
            }
        };
        llytRankType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                schoolRankValue = schoolRankValueList.get(position).getId() == null ? "" : schoolRankValueList.get(position).getId();
                changeSelected(schoolRankValueList, schoolRankValueList.get(position).getId());
                mAdapter.notifyDataSetChanged();
                if (PopWindowHighSchoolRank.this.isShowing()) {
                    PopWindowHighSchoolRank.this.dismiss();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    @SuppressWarnings("deprecation")
    public void showPopupWindow(View parent, TextView textView) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            parent.getGlobalVisibleRect(rect);
            int h = parent.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        if (!this.isShowing()) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_blue_arraw_up, 0);
            v = textView;
            v.setTextColor(Color.parseColor("#078CF1"));
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        v.setTextColor(mContext.getResources().getColor(R.color.app_text_color2));
        v.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_gray, 0);
    }

    public void changeSelected(List<IdNameInfo> lastList, String nowValue) {
        for (IdNameInfo info : lastList) {
            if ((info.getId() == null ? "" : info.getId()).equals(nowValue==null?"":nowValue)) {
                info.setIs_selected(true);
            } else {
                info.setIs_selected(false);
            }
        }
    }
}
