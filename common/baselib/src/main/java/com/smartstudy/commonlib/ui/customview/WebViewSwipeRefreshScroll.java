package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebView;

/**
 * Created by louis on 17/3/29.
 */

public class WebViewSwipeRefreshScroll extends WebView {
    private SwipeRefreshLayout swipeRefreshLayout;

    public WebViewSwipeRefreshScroll(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        super(context);
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.getScrollY() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}
