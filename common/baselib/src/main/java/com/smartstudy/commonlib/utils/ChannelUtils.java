package com.smartstudy.commonlib.utils;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.entity.ChannelInfo;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author louis
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ChannelUtils {

    public static ChannelInfo getChannelObject(String channel) throws Exception {
        ChannelInfo info = null;
        XmlPullParser parser = BaseApplication.appContext.getResources().getXml(R.xml.channel);
        int event = parser.getEventType();
        boolean match = false;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if ("channel".equals(parser.getName())) {
                        String name = parser.getAttributeValue(null, "name");
                        if (channel.equals(name)) {
                            match = true;
                            info = new ChannelInfo();
                            info.setId(parser.getAttributeValue(null, "id"));
                            info.setName(name);
                            info.setMedium(parser.getAttributeValue(null, "medium"));
                            info.setTerm(parser.getAttributeValue(null, "term"));
                            info.setContent(parser.getAttributeValue(null, "content"));
                            info.setCampaign(parser.getAttributeValue(null, "campaign"));
                        }
                    }
                    break;
                default:
                    break;
            }
            if (match) {
                break;
            }
            event = parser.next();
        }

        return info;
    }

}
