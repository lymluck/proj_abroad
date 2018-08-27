package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.activity.BrowserPictureActivity;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.photoview.PhotoInfo;
import com.smartstudy.commonlib.ui.customview.photoview.PhotoView;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.R;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by louis on 17/7/6.
 */
public class MsgImageItem implements ItemViewDelegate<DeviceMsgInfo> {

    private Context mContext;

    public MsgImageItem(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_img;
    }

    @Override
    public boolean isForViewType(DeviceMsgInfo item, int position) {
        return MsgCenterAdapter.getViewTypes().contains(item.getType()) && MsgCenterAdapter.TYPE_IMG.equals(item.getType());
    }

    @Override
    public void convert(final ViewHolder holder, final DeviceMsgInfo deviceMsgInfo, int position) {
        try {
            holder.setText(R.id.tv_msg_time, DateTimeUtils.getDetailTime(BaseApplication.appContext, deviceMsgInfo.getPushTime(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ImageView ivMsg = holder.getView(R.id.iv_msg_content);
        DisplayImageUtils.displayBubbleImage(BaseApplication.appContext, deviceMsgInfo.getImageUrl(), ivMsg);
        ivMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> urlList = new ArrayList();
                urlList.add(deviceMsgInfo.getImageUrl());
                PhotoInfo info = PhotoView.getImageViewInfo(ivMsg);
                ArrayList<PhotoInfo> infoList = new ArrayList();
                infoList.add(info);
                Intent toPreview = new Intent(mContext, BrowserPictureActivity.class);
                toPreview.putStringArrayListExtra("pathList", urlList);
                toPreview.putExtra("index", 0);
                toPreview.putParcelableArrayListExtra("info", infoList);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.fade_in, 0);
                ActivityCompat.startActivity(mContext, toPreview, compat.toBundle());
            }
        });
    }
}
