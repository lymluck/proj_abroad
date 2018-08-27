package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.mvp.contract.BannerFragmentContract;
import com.smartstudy.xxd.mvp.model.BannerFragmentModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */

public class BannerFragmentPresenter implements BannerFragmentContract.Presenter {

    private BannerFragmentContract.View view;

    public BannerFragmentPresenter(BannerFragmentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getBanners(int cacheType, final List<BannerInfo> infos) {
        BannerFragmentModel.getBanners(cacheType, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<BannerInfo> datas = JSON.parseArray(result, BannerInfo.class);
                if (datas != null) {
                    infos.clear();
                    infos.addAll(datas);
                    view.showBanner();
                    datas = null;
                }
            }
        });
    }
}
