package com.smartstudy.router.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.smartstudy.router.RouteRequest;

/**
 * Absolutely matcher.
 * <p>
 * Created by louis on 2016/12/23.
 */
public class DirectMatcher extends AbsExplicitMatcher {

    public DirectMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {
        return !isEmpty(route) && uri.toString().equals(route);
    }


}
