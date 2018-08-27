package com.smartstudy.xxd.ui.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
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
 * @date on 2018/4/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class PopWindowHighSchoolMore extends PopupWindow {
    private Context mContext;
    private TextView v;
    private String schoolType = "";
    private String stayType = "";
    private List<IdNameInfo> listSchoolType;
    private List<IdNameInfo> listStayType;
    private boolean isDone;


    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getStayType() {
        return stayType;
    }

    public void setStayType(String stayType) {
        this.stayType = stayType;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    @SuppressLint("InflateParams")
    public PopWindowHighSchoolMore(final Activity context, HighOptionsInfo highOptionsInfo,
                                   String schoolType, String stayType) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popupwindow_high_school_type,
                null);
        initData(contentView, highOptionsInfo, schoolType, stayType);
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


    public void initData(View contentView, HighOptionsInfo highOptionsInfo,
                         String schoolType, String stayType) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        if (!TextUtils.isEmpty(schoolType)) {
            this.schoolType = schoolType;
        }

        if (!TextUtils.isEmpty(stayType)) {
            this.stayType = stayType;
        }
        RecyclerView lvSchoolTitle = (RecyclerView) contentView.findViewById(R.id.lv_school_title);
        RecyclerView lvSchoolValue = (RecyclerView) contentView.findViewById(R.id.lv_stay_type);
        initClick(contentView, lvSchoolTitle, lvSchoolValue);
        initSchoolTitle(lvSchoolTitle, highOptionsInfo, schoolType);
        initStayType(lvSchoolValue, highOptionsInfo, stayType);
    }


    private void initClick(final View contentView, final RecyclerView lvSchoolTitle, final RecyclerView lvSchoolValue) {

        contentView.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolType = "";
                stayType = "";
                changeSelected(listSchoolType, "");
                changeSelected(listStayType, "");
                lvSchoolTitle.getAdapter().notifyDataSetChanged();
                lvSchoolValue.getAdapter().notifyDataSetChanged();
            }
        });
        contentView.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDone = true;
                dismiss();
            }
        });
    }


    private void initSchoolTitle(RecyclerView lv_school_title, HighOptionsInfo highOptionsInfo, String type) {
        lv_school_title.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 4);
        lv_school_title.setLayoutManager(mLayoutManager);
        listSchoolType = new ArrayList<>();
        listSchoolType.addAll(highOptionsInfo.getSexualTypes().getOptions());
        changeSelected(listSchoolType, type);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, listSchoolType) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                holder.setText(R.id.tv_name, idNameInfo.getName());
                if (idNameInfo.is_selected()) {
                    holder.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.app_main_color));
                    holder.getView(R.id.tv_name).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_blue_raduis5));
                } else {
                    holder.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.app_text_color1));
                    holder.getView(R.id.tv_name).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_white_raduis5));
                }
            }
        };
        lv_school_title.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                schoolType = listSchoolType.get(position).getId() == null ? "" : listSchoolType.get(position).getId();
                changeSelected(listSchoolType, listSchoolType.get(position).getId() == null ? "" : listSchoolType.get(position).getId());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initStayType(RecyclerView lv_fee, HighOptionsInfo highOptionsInfo, String type) {
        lv_fee.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 4);
        lv_fee.setLayoutManager(mLayoutManager);
        listStayType = new ArrayList<>();
        listStayType.addAll(highOptionsInfo.getBoarderTypes().getOptions());
        changeSelected(listStayType, type);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, listStayType) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                holder.setText(R.id.tv_name, idNameInfo.getName());
                if (idNameInfo.is_selected()) {
                    holder.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.app_main_color));
                    holder.getView(R.id.tv_name).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_blue_raduis5));
                } else {
                    holder.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.app_text_color1));
                    holder.getView(R.id.tv_name).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_white_raduis5));
                }
            }
        };
        lv_fee.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                stayType = listStayType.get(position).getId() == null ? "" : listStayType.get(position).getId();
                changeSelected(listStayType, listStayType.get(position).getId() == null ? "" : listStayType.get(position).getId());
                mAdapter.notifyDataSetChanged();
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
            if ((info.getId() == null ? "" : info.getId()).equals(nowValue)) {
                info.setIs_selected(true);
            } else {
                info.setIs_selected(false);
            }
        }
    }
}
