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

import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;

import java.util.List;


public class PopWindowSchoolCountry extends PopupWindow {
    private String id;
    private String name;
    private String from;
    private ColorDrawable dw;
    private LinearLayoutManager mLayoutManager;
    private CommonAdapter mAdapter;
    private Rect rect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressLint("InflateParams")
    public PopWindowSchoolCountry(View contentView, List<CountryTypeInfo> countryTypeInfos,
                                  String nowTitle, String flag) {
        this.from = flag;
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
        initData(contentView, countryTypeInfos, nowTitle);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.popupwindow_city);

    }

    public void initData(View contentView, final List<CountryTypeInfo> countryTypeInfos, String nowTitle) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        RecyclerView lv_type = (RecyclerView) contentView.findViewById(R.id.lv_data);
        lv_type.setHasFixedSize(true);
        Context mContext = getContentView().getContext();
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(mContext);
        }
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_type.setLayoutManager(mLayoutManager);
        changeSelected(countryTypeInfos, nowTitle);
        if (mAdapter == null) {
            mAdapter = new CommonAdapter<CountryTypeInfo>(mContext, com.smartstudy.commonlib.R.layout.item_choose_list, countryTypeInfos) {
                @Override
                protected void convert(ViewHolder holder, CountryTypeInfo countryTypeInfo, int position) {
                    if ("school".equals(from) || "rank".equals(from)) {
                        holder.setText(com.smartstudy.commonlib.R.id.tv_list_name, countryTypeInfo.getCountryName());
                    } else if ("visa".equals(from)) {
                        holder.setText(com.smartstudy.commonlib.R.id.tv_list_name, countryTypeInfo.getVisaName());
                    }
                    if (countryTypeInfo.isSelected()) {
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
                id = countryTypeInfos.get(position).getCountryId();
                if ("school".equals(from) || "rank".equals(from)) {
                    name = countryTypeInfos.get(position).getCountryName();
                    changeSelected(countryTypeInfos, countryTypeInfos.get(position).getCountryName());
                } else if ("visa".equals(from)) {
                    name = countryTypeInfos.get(position).getVisaName();
                    changeSelected(countryTypeInfos, countryTypeInfos.get(position).getVisaName());
                }
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

    public void changeSelected(List<CountryTypeInfo> lastList, String nowTitle) {
        for (CountryTypeInfo info : lastList) {
            if (nowTitle.equals(info.getCountryName()) || nowTitle.equals(info.getVisaName())) {
                info.setSelected(true);
            } else {
                info.setSelected(false);
            }
        }
    }
}
