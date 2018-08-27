package com.smartstudy.xxd.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.CommentInfo;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.xxd.entity.CommentChoiceSchoolInfo;
import com.smartstudy.xxd.mvp.contract.ChoiceShoolContract;
import com.smartstudy.xxd.mvp.model.ChoiceSchoolModel;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceShoolPresenter implements ChoiceShoolContract.Presenter {

    private ChoiceShoolContract.View view;

    public ChoiceShoolPresenter(ChoiceShoolContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void postComment(String userId, String toUserId, String content) {
        ChoiceSchoolModel.postComment(userId, toUserId, content, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.postCommentSuccess();
            }
        });
    }

    @Override
    public void getComment(String userId, int page, final int request_state) {
        ChoiceSchoolModel.getComment(userId, page + "", new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<CommentChoiceSchoolInfo> data = JSON.parseArray(dataListInfo.getData(), CommentChoiceSchoolInfo.class);
                view.getCommentSuccess(data, request_state);
            }
        });
    }

    @Override
    public void addGood(String userId) {
        ChoiceSchoolModel.addGood(userId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.addGoodSuccess();
            }
        });
    }
}
