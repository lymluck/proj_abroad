package com.smartstudy.xxd.ui.popupwindow;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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


public class PopWindowSchoolMoreType extends PopupWindow {
    private String fee = "";
    private String egType = "";
    private String egValue = "";
    private List<IdNameInfo> listEgs;
    private List<IdNameInfo> totalFees;
    private boolean isDone;
    private ColorDrawable dw;
    private GridLayoutManager mEgLayoutManager;
    private GridLayoutManager mFeeLayoutManager;
    private CommonAdapter mEgAdapter;
    private CommonAdapter mFeeAdapter;
    private Rect rect;

    public boolean isDone() {
        return isDone;
    }

    public String getFee() {
        return fee;
    }

    public String getEgType() {
        return egType;
    }

    public String getEgValue() {
        return egValue;
    }

    public PopWindowSchoolMoreType(View contentView, String checkType,
                                   String egTypeValue, String totalFee) {
        isDone = false;
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
        initData(getContentView().getContext(), checkType, egTypeValue, totalFee);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.popupwindow_city);

    }

    public void initData(Context mContext, String checkType,
                         String egTypeValue, String totalFee) {
        if (!TextUtils.isEmpty(totalFee)) {
            fee = totalFee;
        }
        View contentView = getContentView();
        TextView tv_tf = (TextView) contentView.findViewById(R.id.tv_tf);
        TextView tv_ys = (TextView) contentView.findViewById(R.id.tv_ys);
        TextView tv_eg_title = (TextView) contentView.findViewById(R.id.tv_eg_title);
        RecyclerView lv_eg = (RecyclerView) contentView.findViewById(R.id.lv_eg);
        RecyclerView lv_fee = (RecyclerView) contentView.findViewById(R.id.lv_fee);
        initClick(mContext, tv_tf, tv_ys, checkType, lv_eg, lv_fee, tv_eg_title);
        initEgList(mContext, lv_eg);
        initFee(mContext, lv_fee, checkType, totalFee);
        initEgType(mContext, checkType, egTypeValue, tv_tf, tv_ys, lv_eg, tv_eg_title);
    }

    private void initEgType(Context mContext, String checkType, String egTypeValue, TextView tv_tf,
                            TextView tv_ys, RecyclerView lv_eg, TextView tv_eg_title) {
        View contentView = getContentView();
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

    private void initClick(final Context mContext, final TextView tv_tf, final TextView tv_ys, final String checkType,
                           final RecyclerView lv_eg, final RecyclerView lv_fee, final TextView tv_eg_title) {
        final View contentView = getContentView();
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
                fee = "";
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

    private void initEgList(Context mContext, RecyclerView lv_eg) {
        lv_eg.setHasFixedSize(true);
        if (mEgLayoutManager == null) {
            mEgLayoutManager = new GridLayoutManager(mContext, 4);
        }
        lv_eg.setLayoutManager(mEgLayoutManager);
        if (listEgs == null) {
            listEgs = new ArrayList<>();
        } else {
            listEgs.clear();
        }
        if (mEgAdapter == null) {
            mEgAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, listEgs) {
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
        }
        lv_eg.setAdapter(mEgAdapter);
        mEgAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                egValue = listEgs.get(position).getValue();
                changeSelected(listEgs, listEgs.get(position).getValue());
                mEgAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initFee(Context mContext, RecyclerView lv_fee, String checkType, String totalFee) {
        lv_fee.setHasFixedSize(true);
        if (mFeeLayoutManager == null) {
            mFeeLayoutManager = new GridLayoutManager(mContext, 4);
        }
        lv_fee.setLayoutManager(mFeeLayoutManager);
        if (totalFees == null) {
            totalFees = new ArrayList<>();
        } else {
            totalFees.clear();
        }
        totalFees.addAll(JSON.parseArray(JSON.parseObject(checkType).getString("feeTotal"), IdNameInfo.class));
        changeSelected(totalFees, totalFee);
        if (mFeeAdapter == null) {
            mFeeAdapter = new CommonAdapter<IdNameInfo>(mContext, R.layout.item_school_type, totalFees) {
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
        }
        lv_fee.setAdapter(mFeeAdapter);
        mFeeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                fee = totalFees.get(position).getValue();
                changeSelected(totalFees, totalFees.get(position).getValue());
                mFeeAdapter.notifyDataSetChanged();
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
            textView.setTextColor(getContentView().getContext().getResources().getColor(R.color.app_main_color));
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
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
