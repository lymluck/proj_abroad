package com.smartstudy.commonlib.ui.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * recyclerView通用适配器
 * Created by louis on 2017/3/2.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        addaddItemViewDelegate(layoutId);
        context = null;
        datas = null;
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas, LayoutInflater inflater) {
        super(context, datas);
        mContext = context;
        mInflater = inflater;
        mLayoutId = layoutId;
        addaddItemViewDelegate(layoutId);
        context = null;
        datas = null;
        inflater = null;
    }

    private void addaddItemViewDelegate(final int layoutId) {
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size() - position);
    }

    @Override
    public void destroy() {
        if (mContext != null) {
            mContext = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }
        super.destroy();
    }
}
