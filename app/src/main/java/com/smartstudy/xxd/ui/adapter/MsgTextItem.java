package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.xxd.R;

import java.text.ParseException;

/**
 * Created by louis on 17/7/6.
 */

public class MsgTextItem implements ItemViewDelegate<DeviceMsgInfo> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_text;
    }

    @Override
    public boolean isForViewType(DeviceMsgInfo item, int position) {
        return MsgCenterAdapter.getViewTypes().contains(item.getType()) && MsgCenterAdapter.TYPE_TEXT.equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, DeviceMsgInfo deviceMsgInfo, int position) {
        try {
            holder.setText(R.id.tv_msg_time, DateTimeUtils.getDetailTime(BaseApplication.appContext, deviceMsgInfo.getPushTime(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_msg_content, deviceMsgInfo.getText());
    }
}
