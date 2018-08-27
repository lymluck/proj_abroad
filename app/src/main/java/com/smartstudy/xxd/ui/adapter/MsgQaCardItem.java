package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.R;

import java.text.ParseException;

/**
 * Created by louis on 17/7/6.
 */

public class MsgQaCardItem implements ItemViewDelegate<DeviceMsgInfo> {

    private Context mContext;

    public MsgQaCardItem(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_card;
    }

    @Override
    public boolean isForViewType(DeviceMsgInfo item, int position) {
        return MsgCenterAdapter.getViewTypes().contains(item.getType()) && MsgCenterAdapter.TYPE_CARD.equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, final DeviceMsgInfo deviceMsgInfo, int position) {
        try {
            holder.setText(R.id.tv_msg_time, DateTimeUtils.getDetailTime(BaseApplication.appContext, deviceMsgInfo.getPushTime(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setRoundImageUrl(R.id.iv_card, deviceMsgInfo.getImageUrl(), 4, true);
        final JSONObject data = JSON.parseObject(deviceMsgInfo.getData());
        holder.getView(R.id.iv_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.getBooleanValue("expired")) {
                    String cardId = data.getString("cardId");
                    Bundle data = new Bundle();
                    data.putString("cardId", cardId);
                    Router.build("OrderQaActivity").with(data).requestCode(ParameterUtils.REQUEST_CODE_CARD_QA).go(mContext);
                }
            }
        });
    }
}
