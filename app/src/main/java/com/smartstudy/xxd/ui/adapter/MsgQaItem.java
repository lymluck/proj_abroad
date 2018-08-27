package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import java.text.ParseException;

/**
 * Created by louis on 17/7/6.
 */

public class MsgQaItem implements ItemViewDelegate<DeviceMsgInfo> {

    private Context mContext;

    public MsgQaItem(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_text_qa;
    }

    @Override
    public boolean isForViewType(DeviceMsgInfo item, int position) {
        return MsgCenterAdapter.getViewTypes().contains(item.getType()) && MsgCenterAdapter.TYPE_TEXT_QA.equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, final DeviceMsgInfo deviceMsgInfo, int position) {
        try {
            holder.setText(R.id.tv_msg_time, DateTimeUtils.getDetailTime(BaseApplication.appContext, deviceMsgInfo.getPushTime(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_msg_qa, Html.fromHtml(String.format(mContext.getString(R.string.msg_qa_pre), deviceMsgInfo.getText()) + "<font color=#078CF1>"
                + mContext.getString(R.string.click_to_see) + "</font>"));
        holder.getView(R.id.tv_msg_qa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = deviceMsgInfo.getLinkUrl();
                String id = link.substring(link.lastIndexOf("=") + 1, link.length());
                Intent toQa = new Intent(mContext, ShowWebViewActivity.class);
                toQa.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), id));
                toQa.putExtra("url_action", "get");
                toQa.putExtra("title", "问答详情");
                mContext.startActivity(toQa);
            }
        });
    }
}
