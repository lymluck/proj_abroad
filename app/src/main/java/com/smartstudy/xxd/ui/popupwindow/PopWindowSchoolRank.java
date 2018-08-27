package com.smartstudy.xxd.ui.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;

import java.util.List;


public class PopWindowSchoolRank extends PopupWindow {
    private String value;
    private String name;
    private ColorDrawable dw;
    private LinearLayoutManager mLayoutManager;
    private CommonAdapter mAdapter;
    private Rect rect;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressLint("InflateParams")
    public PopWindowSchoolRank(View contentView, List<IdNameInfo> IdNameInfos,
                               String nowTitle) {
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        if (dw == null) {
            dw = new ColorDrawable(0x00000000);
        }
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        initData(contentView, IdNameInfos, nowTitle);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.popupwindow_city);

    }

    public void initData(View contentView, final List<IdNameInfo> IdNameInfos, String nowTitle) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        Context mContext = getContentView().getContext();
        RecyclerView lv_type = (RecyclerView) contentView.findViewById(R.id.lv_data);
        lv_type.setHasFixedSize(true);
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(mContext);
        }
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_type.setLayoutManager(mLayoutManager);
        changeSelected(IdNameInfos, nowTitle);
        if (mAdapter == null) {
            mAdapter = new CommonAdapter<IdNameInfo>(mContext, com.smartstudy.commonlib.R.layout.item_choose_list, IdNameInfos) {
                @Override
                protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                    holder.setText(com.smartstudy.commonlib.R.id.tv_list_name, idNameInfo.getName());
                    if (idNameInfo.is_selected()) {
                        holder.setTextColor(R.id.tv_list_name, mContext.getResources().getColor(R.color.app_main_color));
                        holder.getView(com.smartstudy.commonlib.R.id.iv_choose).setVisibility(View.VISIBLE);
                    } else {
                        holder.setTextColor(R.id.tv_list_name, mContext.getResources().getColor(R.color.app_text_color2));
                        holder.getView(com.smartstudy.commonlib.R.id.iv_choose).setVisibility(View.GONE);
                    }
                }
            };
        }
        lv_type.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                value = IdNameInfos.get(position).getValue();
                name = IdNameInfos.get(position).getName();
                changeSelected(IdNameInfos, IdNameInfos.get(position).getName());
                mAdapter.notifyDataSetChanged();
                dismiss();
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
            if (rect == null) {
                rect = new Rect();
            }
            parent.getGlobalVisibleRect(rect);
            int h = parent.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        if (!this.isShowing()) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_blue_arraw_up, 0);
            textView.setTextColor(Color.parseColor("#078CF1"));
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void changeSelected(List<IdNameInfo> lastList, String nowTitle) {
        for (IdNameInfo info : lastList) {
            if (nowTitle.equals(info.getName())) {
                info.setIs_selected(true);
            } else {
                info.setIs_selected(false);
            }
        }
    }
}
