package com.smartstudy.xxd.ui.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.List;


public class PopWindow_School_MoreType extends PopupWindow {
    private Context mContext;
    private TextView v;
    private String fee;
    private String egType = "";
    private String egValue = "";
    private List<IdNameInfo> listEgs;
    private List<IdNameInfo> totalFees;
    private boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getEgType() {
        return egType;
    }

    public void setEgType(String egType) {
        this.egType = egType;
    }

    public String getEgValue() {
        return egValue;
    }

    public void setEgValue(String egValue) {
        this.egValue = egValue;
    }

    @SuppressLint("InflateParams")
    public PopWindow_School_MoreType(final Activity context, String checkType,
                                     String egTypeValue, String totalFee) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popupwindow_school_type,
                null);
        initData(contentView, checkType, egTypeValue, totalFee);
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

    public void initData(View contentView, String checkType,
                         String egTypeValue, String totalFee) {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.lv_data) {
                    dismiss();
                }
            }
        });
        if (!TextUtils.isEmpty(totalFee)) {
            fee = totalFee;
        }
        TextView tv_tf = (TextView) contentView.findViewById(R.id.tv_tf);
        TextView tv_ys = (TextView) contentView.findViewById(R.id.tv_ys);
        TextView tv_eg_title = (TextView) contentView.findViewById(R.id.tv_eg_title);
        RecyclerView lv_eg = (RecyclerView) contentView.findViewById(R.id.lv_eg);
        RecyclerView lv_fee = (RecyclerView) contentView.findViewById(R.id.lv_fee);
        initClick(contentView, tv_tf, tv_ys, checkType, lv_eg, lv_fee, tv_eg_title);
        initEgList(lv_eg);
        initFee(lv_fee, checkType, totalFee);
        initEgType(contentView, checkType, egTypeValue, tv_tf, tv_ys, lv_eg, tv_eg_title);
    }

    private void initEgType(View contentView, String checkType, String egTypeValue, TextView tv_tf,
                            TextView tv_ys, RecyclerView lv_eg, TextView tv_eg_title) {
        if (!TextUtils.isEmpty(egTypeValue)) {
            String type = TextUtils.split(egTypeValue, ":")[0];
            String value = TextUtils.split(egTypeValue, ":")[1];
            if (!TextUtils.isEmpty(value)) {
                contentView.findViewById(R.id.llyt_eg).setVisibility(View.VISIBLE);
                contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.VISIBLE);
                if ("toefl".equals(type)) {
                    egType = "toefl";
                    egValue = value;
                    tv_eg_title.setText("托福要求");
                    tv_tf.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
                    tv_tf.setBackgroundResource(R.drawable.bg_btn_blue_raduis5);
                    tv_ys.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                    tv_ys.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                    listEgs.clear();
                    listEgs.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("scoreToefl"), IdNameInfo.class));
                    changeSelected(listEgs, value);
                    lv_eg.getAdapter().notifyDataSetChanged();
                } else if ("ielts".equals(type)) {
                    egType = "ielts";
                    egValue = value;
                    tv_eg_title.setText("雅思要求");
                    tv_ys.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
                    tv_ys.setBackgroundResource(R.drawable.bg_btn_blue_raduis5);
                    tv_tf.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                    tv_tf.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                    listEgs.clear();
                    listEgs.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("scoreIelts"), IdNameInfo.class));
                    changeSelected(listEgs, value);
                    lv_eg.getAdapter().notifyDataSetChanged();
                }
            } else {
                contentView.findViewById(R.id.llyt_eg).setVisibility(View.GONE);
                contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.GONE);
            }
        } else {
            contentView.findViewById(R.id.llyt_eg).setVisibility(View.GONE);
            contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.GONE);
        }
    }

    private void initClick(final View contentView, final TextView tv_tf, final TextView tv_ys, final String checkType,
                           final RecyclerView lv_eg, final RecyclerView lv_fee, final TextView tv_eg_title) {
        tv_tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"toefl".equals(egType)) {
                    egType = "toefl";
                    tv_eg_title.setText("托福要求");
                    tv_tf.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
                    tv_tf.setBackgroundResource(R.drawable.bg_btn_blue_raduis5);
                    tv_ys.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                    tv_ys.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                    listEgs.clear();
                    listEgs.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("scoreToefl"), IdNameInfo.class));
                    lv_eg.getAdapter().notifyDataSetChanged();
                    contentView.findViewById(R.id.llyt_eg).setVisibility(View.VISIBLE);
                    contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.VISIBLE);
                }
            }
        });
        tv_ys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"ielts".equals(egType)) {
                    egType = "ielts";
                    tv_eg_title.setText("雅思要求");
                    tv_ys.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
                    tv_ys.setBackgroundResource(R.drawable.bg_btn_blue_raduis5);
                    tv_tf.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                    tv_tf.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                    listEgs.clear();
                    listEgs.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("scoreIelts"), IdNameInfo.class));
                    lv_eg.getAdapter().notifyDataSetChanged();
                    contentView.findViewById(R.id.llyt_eg).setVisibility(View.VISIBLE);
                    contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.VISIBLE);
                }
            }
        });
        contentView.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tf.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                tv_tf.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                tv_ys.setTextColor(mContext.getResources().getColor(R.color.app_text_color1));
                tv_ys.setBackgroundResource(R.drawable.bg_btn_white_raduis5);
                listEgs.clear();
                lv_eg.getAdapter().notifyDataSetChanged();
                contentView.findViewById(R.id.llyt_eg).setVisibility(View.GONE);
                contentView.findViewById(R.id.llyt_eg_line).setVisibility(View.GONE);
                changeSelected(totalFees, "-1");
                lv_fee.getAdapter().notifyDataSetChanged();
                egType = "";
                egValue = "";
                fee = null;
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

    private void initEgList(RecyclerView lv_eg) {
        lv_eg.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 4);
        lv_eg.setLayoutManager(mLayoutManager);
        listEgs = new ArrayList<>();
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, listEgs) {
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
        lv_eg.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                egValue = listEgs.get(position).getValue();
                changeSelected(listEgs, listEgs.get(position).getValue());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initFee(RecyclerView lv_fee, String checkType, String totalFee) {
        lv_fee.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 4);
        lv_fee.setLayoutManager(mLayoutManager);
        totalFees = new ArrayList<>();
        totalFees.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("feeTotal"), IdNameInfo.class));
        changeSelected(totalFees, totalFee);
        final CommonAdapter mAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, totalFees) {
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
                fee = totalFees.get(position).getValue();
                changeSelected(totalFees, totalFees.get(position).getValue());
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
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            parent.getGlobalVisibleRect(rect);
            int h = parent.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        if (!this.isShowing()) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_gray, 0);
            v = textView;
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
            if (info.getValue().equals(nowValue)) {
                info.setIs_selected(true);
            } else {
                info.setIs_selected(false);
            }
        }
    }
}
