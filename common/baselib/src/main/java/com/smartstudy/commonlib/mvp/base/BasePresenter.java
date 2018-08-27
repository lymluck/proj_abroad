package com.smartstudy.commonlib.mvp.base;

public interface BasePresenter {
    //视图分离时的操作，如可以释放资源
    void onDetachView();
}
