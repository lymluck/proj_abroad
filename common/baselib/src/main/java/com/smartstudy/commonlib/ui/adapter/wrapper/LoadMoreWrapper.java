package com.smartstudy.commonlib.ui.adapter.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.WrapperUtils;


/**
 * Created by louis on 2017/3/2.
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int STATE_LOAD = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADCOMPLE = 2;
    public static final int STATE_NODATA = 3;
    private int mState = STATE_LOAD;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private ProgressBar pb;
    private TextView tv;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }


    private boolean isShowLoadMore(int position) {
        return position >= mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), parent, R.layout.pull_loading_view);
            mLoadMoreView = holder.getConvertView();
            tv = holder.getView(R.id.loading_text);
            pb = holder.getView(R.id.pb);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            tv.setText(getTextByState());
        } else {
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + 1;
    }

    private String getTextByState() {
        String result = "上拉加载更多";
        pb.setVisibility(View.GONE);
        mLoadMoreView.setVisibility(View.VISIBLE);
        switch (mState) {
            case STATE_LOAD:
                mLoadMoreView.setVisibility(View.GONE);
                break;
            case STATE_LOADCOMPLE:
                result = "加载完成";
                break;
            case STATE_LOADING:
                result = "正在加载更多....";
                pb.setVisibility(View.VISIBLE);
                break;
            case STATE_NODATA:
                result = "没有更多了";
                break;
        }
        return result;
    }

    public void setState(int state) {
        if (state != STATE_LOAD && state != STATE_LOADCOMPLE && state != STATE_LOADING && state != STATE_NODATA) {
            throw new RuntimeException("状态有误！");
        }
        mState = state;
        notifyDataSetChanged();
    }
}
