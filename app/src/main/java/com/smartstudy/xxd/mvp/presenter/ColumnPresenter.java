package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.ColumnInfo;
import com.smartstudy.xxd.mvp.contract.ColumnContract;
import com.smartstudy.xxd.mvp.model.CollectionModel;
import com.smartstudy.xxd.mvp.model.ColumnModel;
import com.smartstudy.xxd.mvp.model.FavoriteModel;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ColumnPresenter implements ColumnContract.Presenter {

    private ColumnContract.View view;

    public ColumnPresenter(ColumnContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getColumn(String Id) {
        ColumnModel.getColumn(Id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                ColumnInfo info = JSON.parseObject(result, ColumnInfo.class);
                if (info != null) {
                    view.showColumn(info);
                }
            }
        });

    }

    @Override
    public void likeThis(String id) {
        FavoriteModel.favorite("columnNews", id, new BaseCallback() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(Object result) {
                view.likeSuccess();
            }
        });
    }

    @Override
    public void disLikeThis(String id) {
        FavoriteModel.disFavorite("columnNews", id, new BaseCallback() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(Object result) {
                view.disLikesuccess();
            }
        });
    }

    @Override
    public void collectThis(String id) {
        CollectionModel.collect("columnNews", id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.collectSuccess();
            }
        });
    }

    @Override
    public void disCollectThis(String id) {
        CollectionModel.disCollect("columnNews", id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.disCollectSuccess();
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }

}

