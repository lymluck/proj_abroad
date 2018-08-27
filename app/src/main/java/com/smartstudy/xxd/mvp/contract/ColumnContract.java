package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ColumnInfo;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ColumnContract {
    interface View extends BaseView<ColumnContract.Presenter> {

        void showColumn(ColumnInfo info);

        void likeSuccess();

        void disLikesuccess();

        void collectSuccess();

        void disCollectSuccess();

    }

    interface Presenter extends BasePresenter {

        void getColumn(String Id);

        void likeThis(String id);

        void disLikeThis(String id);

        void collectThis(String id);

        void disCollectThis(String id);
    }
}
