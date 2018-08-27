package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.CommentChoiceSchoolInfo;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ChoiceShoolContract {
    interface View extends BaseView<ChoiceShoolContract.Presenter> {
        void postCommentSuccess();

        void getCommentSuccess(List<CommentChoiceSchoolInfo> commentChoiceSchoolInfos, int request_state);

        void addGoodSuccess();
    }

    interface Presenter extends BasePresenter {

        void postComment(String userId, String toUserId, String content);

        void getComment(String userId, int page, int request_state);

        void addGood(String userId);

    }

}
