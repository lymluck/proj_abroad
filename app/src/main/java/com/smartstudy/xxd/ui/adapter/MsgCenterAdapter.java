package com.smartstudy.xxd.ui.adapter;

import android.content.Context;

import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by louis on 17/7/6.
 */
public class MsgCenterAdapter extends MultiItemTypeAdapter<DeviceMsgInfo> {

    public static String TYPE_TEXT = "text";
    public static String TYPE_TEXT_IMG = "feed";
    public static String TYPE_IMG = "image";
    public static String TYPE_CARD = "appointment_card";
    public static String TYPE_TEXT_QA = "question-notify";

    public static List<String> getViewTypes() {
        String[] types = new String[]{TYPE_TEXT, TYPE_TEXT_IMG, TYPE_IMG, TYPE_CARD, TYPE_TEXT_QA};
        List<String> viewTypes = Arrays.asList(types);
        return viewTypes;
    }

    public MsgCenterAdapter(Context context, List<DeviceMsgInfo> datas) {
        super(context, datas);
        addItemViewDelegate(new MsgDefaultItem());
        addItemViewDelegate(new MsgTextItem());
        addItemViewDelegate(new MsgImageItem(context));
        addItemViewDelegate(new MsgTextImageItem(context));
        addItemViewDelegate(new MsgQaCardItem(context));
        addItemViewDelegate(new MsgQaItem(context));
    }

}
