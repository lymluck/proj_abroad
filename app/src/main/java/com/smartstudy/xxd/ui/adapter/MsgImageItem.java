package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.transferImage.loader.GlideImageLoader;
import com.smartstudy.commonlib.ui.customView.transferImage.style.progress.ProgressPieIndicator;
import com.smartstudy.commonlib.ui.customView.transferImage.transfer.TransferConfig;
import com.smartstudy.commonlib.ui.customView.transferImage.transfer.Transferee;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.R;

import java.text.ParseException;
import java.util.Arrays;

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
        final ImageView iv_msg = holder.getView(R.id.iv_msg_content);
        DisplayImageUtils.displayBubbleImage(BaseApplication.appContext, deviceMsgInfo.getImageUrl(), iv_msg);
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferConfig config = TransferConfig.build()
                        .setSourceImageList(Arrays.asList(deviceMsgInfo.getImageUrl()))
                        .setProgressIndicator(new ProgressPieIndicator())
                        .setImageLoader(GlideImageLoader.with(BaseApplication.appContext))
                        .create();
                config.setNowThumbnailIndex(0);
                config.setOriginImageList(Arrays.asList(iv_msg));
                Transferee.getDefault(mContext).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                    @Override
                    public void onShow() {
                        DisplayImageUtils.pauseRequest(mContext);
                    }

                    @Override
                    public void onDismiss() {
                        DisplayImageUtils.resumeRequest(mContext);
                    }
                });
            }
        });
    }
}
