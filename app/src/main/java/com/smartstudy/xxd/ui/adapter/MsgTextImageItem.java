package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.R;

import java.text.ParseException;

import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

/**
 * Created by louis on 17/7/6.
 */

public class MsgTextImageItem implements ItemViewDelegate<DeviceMsgInfo> {

    private Context mContext;

    public MsgTextImageItem(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_msg_text_img;
    }

    @Override
    public boolean isForViewType(DeviceMsgInfo item, int position) {
        return MsgCenterAdapter.getViewTypes().contains(item.getType()) && MsgCenterAdapter.TYPE_TEXT_IMG.equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, final DeviceMsgInfo deviceMsgInfo, int position) {
        try {
            holder.setText(R.id.tv_msg_time, DateTimeUtils.getDetailTime(BaseApplication.appContext, deviceMsgInfo.getPushTime(), false));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_msg_content, deviceMsgInfo.getText());
        ImageView imageView = holder.getView(R.id.iv_msg_img);
        holder.setImageUrl(imageView, deviceMsgInfo.getImageUrl(), true);
        holder.getView(R.id.llyt_txt_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = deviceMsgInfo.getLinkUrl();
                if (!TextUtils.isEmpty(link)) {
                    if (link.startsWith("http") || link.startsWith("https")) {
                        //调用webview打开网址
                        Router.build("showWebView").with(WEBVIEW_URL, link).with(WEBVIEW_ACTION,
                            "get").go(mContext);
                    } else {
                        if (link.contains("course?") || link.contains("school?")
                            || link.contains("question?")) {
                            //构造一个StringBuilder对象
                            StringBuilder sb = new StringBuilder(link);
                            //在指定的位置插入指定的字符串
                            sb.insert(link.indexOf("?"), "/detail");
                            link = sb.toString();
                        }
                        Router.build(link).go(mContext);
                    }
                }
            }
        });
    }
}
