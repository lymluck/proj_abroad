package com.smartstudy.commonlib.ui.adapter.wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.BaseRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemManager;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by baozi on 2017/5/16.
 */

public class TreeBaseWapper<T extends BaseItem> extends BaseRecyclerAdapter<T> {

    protected BaseRecyclerAdapter<T> mAdapter;

    private ItemManager<T> mItemManager;

    public TreeBaseWapper(BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
        mAdapter.getItemManager().setAdapter(this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public T getData(int position) {
        return mAdapter.getData(position);
    }

    @Override
    public List<T> getDatas() {
        return mAdapter.getDatas();
    }

    @Override
    public void setDatas(List<T> datas) {
        mAdapter.setDatas(datas);
    }

    @Override
    public CheckItem getCheckItem() {
        return mAdapter.getCheckItem();
    }

    @Override
    public void setCheckItem(CheckItem checkItem) {
        mAdapter.setCheckItem(checkItem);
    }

    @Override
    public void setOnItemClickListener(OnItemClickLitener onItemClickListener) {
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mAdapter.setOnItemLongClickListener(onItemLongClickListener);
    }

    @Override
    public ItemManager<T> getItemManager() {
        return mAdapter.getItemManager();
    }

    @Override
    public void setItemManager(ItemManager<T> itemManager) {
        mAdapter.setItemManager(itemManager);
    }
}
