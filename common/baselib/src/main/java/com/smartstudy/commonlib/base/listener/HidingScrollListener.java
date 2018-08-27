package com.smartstudy.commonlib.base.listener;

import android.support.v7.widget.RecyclerView;

import com.smartstudy.commonlib.utils.DensityUtils;

/**
 * Created by louis on 17/4/10.
 */

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = DensityUtils.dip2px(30);
    private static final int SHOW_THRESHOLD = 15;
    private int scrolledDistance = 0;
    private boolean controlsVisible = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -SHOW_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();
}