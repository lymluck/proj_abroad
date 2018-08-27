package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;

/**
 * Created by louis on 2017/3/3.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    private LoadMoreWrapper loadMoreWrapper;


    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(null);
    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(new LoadMoreListener(listener));
    }

    public interface OnLoadMoreListener {
        void OnLoad();
    }

    OnLoadMoreListener mOnloadmoreLister = null;

    public void SetOnLoadMoreLister(OnLoadMoreListener listener) {
        mOnloadmoreLister = listener;
    }

    private class LoadMoreListener extends OnScrollListener {
        private OnScrollListener listener;
        private int lastVisibleItem;

        public LoadMoreListener(OnScrollListener listener) {
            this.listener = listener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView view, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == getAdapter().getItemCount()) {
                if (mOnloadmoreLister != null && haveData) {
                    loadMoreWrapper.setState(LoadMoreWrapper.STATE_LOADING);
                    mOnloadmoreLister.OnLoad();
                }
            }
            if (listener != null) {
                listener.onScrollStateChanged(view, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //注意：因为findLastVisibleItemPosition方法目前没有在LayoutManger中声明为抽象方法，因此不同直接通过LayoutManager调用。
            if (getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager lm = (LinearLayoutManager) getLayoutManager();
                lastVisibleItem = lm.findLastVisibleItemPosition();
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gm = (GridLayoutManager) getLayoutManager();
                lastVisibleItem = gm.findLastVisibleItemPosition();
            }

            if (listener != null) {
                listener.onScrolled(recyclerView, dx, dy);
            }
        }

    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof LoadMoreWrapper)) {
            throw new IllegalArgumentException("请使用LoadMoreWrapper");
        }
        loadMoreWrapper = (LoadMoreWrapper) adapter;
        super.setAdapter(loadMoreWrapper);
    }

    private boolean haveData = true;

    public void loadComplete(final boolean haveData) {
        this.haveData = haveData;
        if (!haveData) {
            loadMoreWrapper.setState(LoadMoreWrapper.STATE_NODATA);
        } else {
            loadMoreWrapper.setState(LoadMoreWrapper.STATE_LOAD);
        }

    }
}
