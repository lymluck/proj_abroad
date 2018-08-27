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
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class PopWindowHighSchoolFee extends PopupWindow {

    private Context mContext;
    private TextView v;
    private String id;
    private String name;

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
    public PopWindowHighSchoolFee(final Activity context, List<IdNameInfo> IdNameInfos,
                                  String nowTitle) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popupwindow_choose_data,
                null);
        initData(contentView, IdNameInfos, nowTitle);
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

    public void initData(View contentView, final List<IdNameInfo> IdNameInfos, String nowTitle) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        RecyclerView lvType = (RecyclerView) contentView.findViewById(R.id.lv_data);
        lvType.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvType.setLayoutManager(mLayoutManager);
        changeSelected(IdNameInfos, nowTitle);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, com.smartstudy.commonlib.R.layout.item_choose_list, IdNameInfos) {
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
        lvType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                id = IdNameInfos.get(position).getId() == null ? "" : IdNameInfos.get(position).getId();
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
