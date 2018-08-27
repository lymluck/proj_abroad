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
import android.view.LayoutInflater;
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


public class PopWindow_School_Country extends PopupWindow {
    private Context mContext;
    private TextView v;
    private String id;
    private String name;
    private String from;

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
    public PopWindow_School_Country(final Activity context, List<CountryTypeInfo> countryTypeInfos,
                                 String nowTitle, String flag) {
        mContext = context;
        this.from = flag;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popupwindow_choose_data,
                null);
        initData(contentView, countryTypeInfos, nowTitle);
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
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_type.setLayoutManager(mLayoutManager);
        changeSelected(countryTypeInfos, nowTitle);
        final CommonAdapter mAdapter = new CommonAdapter<CountryTypeInfo>(mContext, com.smartstudy.commonlib.R.layout.item_choose_list, countryTypeInfos) {
            @Override
            protected void convert(ViewHolder holder, CountryTypeInfo countryTypeInfo, int position) {
                if ("school".equals(from)) {
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
        lv_type.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                id = countryTypeInfos.get(position).getCountryId();
                if ("school".equals(from)) {
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
        if (Build.VERSION.SDK_INT == 24) {
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
