package com.smartstudy.commonlib.mvp.base;

public interface BaseView<T> {

    //视图传入presenter
    void setPresenter(T presenter);

    //展示提示信息,可以根据errcode去做一些事情
    void showTip(String errCode, String message);

}
