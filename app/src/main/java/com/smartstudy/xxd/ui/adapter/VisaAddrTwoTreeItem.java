package com.smartstudy.xxd.ui.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.VisaAddrInfo;

/**
 */
public class VisaAddrTwoTreeItem extends TreeItem<VisaAddrInfo> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_visa_addr_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_code, String.format(BaseApplication.appContext.getString(R.string.visa_mailcode), data.getPostcode()));
        if (TextUtils.isEmpty(data.getPhone())) {
            viewHolder.getView(R.id.tv_tel).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_tel).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_tel, Html.fromHtml(String.format(BaseApplication.appContext.getString(R.string.visa_tel), "<font color=#078CF1>" + data.getPhone() + "</font>")));
        }
        if (TextUtils.isEmpty(data.getWebsite())) {
            viewHolder.getView(R.id.tv_website).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_website).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_website, String.format(BaseApplication.appContext.getString(R.string.visa_website), data.getWebsite()));
        }
        viewHolder.setText(R.id.tv_addr, String.format(BaseApplication.appContext.getString(R.string.visa_addr), data.getAddress()));
        viewHolder.setText(R.id.tv_area, String.format(BaseApplication.appContext.getString(R.string.visa_area), data.getDistricts()));
    }
}
