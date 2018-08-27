package com.smartstudy.commonlib.ui.adapter.base;


/**
 * Created by louis on 2017/3/2.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
