package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemViewDelegate;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.router.RouteCallback;
import com.smartstudy.router.RouteResult;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import java.text.ParseException;

import cn.jpush.android.api.JPushInterface;

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
                        Router.build("showWebView").with("web_url", link).with("url_action", "get").go(mContext);
                    } else {
                        if (link.contains("course?") || link.contains("school?")) {
                            StringBuilder sb = new StringBuilder(link);//构造一个StringBuilder对象
                            sb.insert(link.indexOf("?"), "/detail");//在指定的位置插入指定的字符串
                            link = sb.toString();
                        }
                        Router.build(link).callback(new RouteCallback() {
                            @Override
                            public void callback(RouteResult state, Uri uri, String message) {
                                String url = uri.toString();
                                if (url.contains("?")) {
                                    url = url.substring(0, url.indexOf("?"));
                                }
                                switch (url) {
                                    case "xuanxiaodi://customer_service":
                                        //打开美洽
                                        SPCacheUtils.put("meiqia_unread", 0);
                                        JPushInterface.clearAllNotifications(mContext);
                                        Intent toWhere = new MQIntentBuilder(mContext).build();
                                        mContext.startActivity(toWhere);
                                        break;
                                    case "xuanxiaodi://question":
                                        Intent toQa = new Intent(mContext, ShowWebViewActivity.class);
                                        toQa.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), uri.getQueryParameter("id")));
                                        toQa.putExtra("url_action", "get");
                                        mContext.startActivity(toQa);
                                        break;
                                }
                            }
                        }).go(mContext);
                    }
                }
            }
        });
    }
}
