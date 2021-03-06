package com.smartstudy.router.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.smartstudy.router.RouteRequest;

/**
 * This matcher will generate an intent with an {@link android.content.Intent#ACTION_VIEW} action
 * and open a browser.
 * <p>
 * Created by louis on 2017/1/5.
 */
public class BrowserMatcher extends AbsImplicitMatcher {
    public BrowserMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {
        return (uri.toString().toLowerCase().startsWith("http://")
            || uri.toString().toLowerCase().startsWith("https://"));
    }
}
