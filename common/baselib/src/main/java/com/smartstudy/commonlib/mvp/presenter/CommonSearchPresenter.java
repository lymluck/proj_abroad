package com.smartstudy.commonlib.mvp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.CourseInfo;
import com.smartstudy.commonlib.entity.DataListInfo;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.mvp.contract.CommonSearchContract;
import com.smartstudy.commonlib.mvp.model.CommonSearchModel;
import com.smartstudy.commonlib.mvp.model.QuestionsModel;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;

import java.util.List;

/**
 * Created by louis on 2017/3/4.
 */

public class CommonSearchPresenter implements CommonSearchContract.Presenter {
    private CommonSearchContract.View view;

    public CommonSearchPresenter(CommonSearchContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getSchools(int cacheType, String countryId, final String keyword, final int page,
                           final int request_state, final String flag) {
        CommonSearchModel.getSchools(cacheType, countryId, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<SchoolInfo> data = JSON.parseArray(dataListInfo.getData(), SchoolInfo.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getRanks(int cacheType, final String categoryId, final String keyword, final int page, final int request_state, final String flag) {
        CommonSearchModel.getRanks(cacheType, categoryId, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<SchooolRankInfo> data = JSON.parseArray(dataListInfo.getData(), SchooolRankInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.showResult(data, request_state, flag);
                    data = null;
                }
            }
        });
    }

    @Override
    public void getNews(int cacheType, String keyword, int page, final int request_state, final String flag) {
        CommonSearchModel.getNews(cacheType, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<NewsInfo> data = JSON.parseArray(dataListInfo.getData(), NewsInfo.class);
                if (data != null) {
                    for (NewsInfo info : data) {
                        JSONArray arr = JSON.parseArray(info.getTags());
                        if (arr != null) {
                            int len = arr.size();
                            if (len > 0) {
                                for (int i = 0; i < len; i++) {
                                    info.setTags(null);
                                    JSONObject obj = arr.getJSONObject(i);
                                    if ("stage".equals(obj.getString("type"))) {
                                        info.setTags(obj.getString("name"));
                                        break;
                                    }
                                }
                            } else {
                                info.setTags(null);
                            }
                        }
                    }
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getCourses(int cacheType, String keyword, int page, final int request_state, final String flag) {
        CommonSearchModel.getCourses(cacheType, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<CourseInfo> data = JSON.parseArray(result, CourseInfo.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getSpecialData(int cacheType, String keyword, int page, final int request_state, final String flag) {
        CommonSearchModel.getSpeData(cacheType, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<IdNameInfo> data = JSON.parseArray(dataListInfo.getData(), IdNameInfo.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getProgram(int cacheType, String typeId, String keyword, int page, final int request_state, final String flag) {
        CommonSearchModel.getProgram(cacheType, typeId, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<IdNameInfo> data = JSON.parseArray(dataListInfo.getData(), IdNameInfo.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getRankType(int cacheType, String keyword, int page, final int request_state, final String flag) {
        CommonSearchModel.getRankType(cacheType, keyword, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<RankTypeInfo.RankingsEntity> data = JSON.parseArray(dataListInfo.getData(), RankTypeInfo.RankingsEntity.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void getQa(int cacheType, String keyword, int page, final int request_state, final String flag) {
        QuestionsModel.getQuestions(cacheType, keyword, true, page, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                if (data != null) {
                    view.showResult(data, request_state, flag);
                }
            }
        });
    }

    @Override
    public void setEmptyView(LayoutInflater mInflater, final Context context, ViewGroup parent, String from) {
        View emptyView = mInflater.inflate(R.layout.layout_empty, parent, false);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        Button tv_qa_btn = (Button) emptyView.findViewById(R.id.tv_qa_btn);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        tv_err_tip.setText(context.getString(R.string.no_search_tip));
        if (ParameterUtils.QA_FLAG.equals(from)) {
            tv_qa_btn.setVisibility(View.VISIBLE);
            tv_err_tip.setText(context.getString(R.string.no_search_qa_tip));
            tv_qa_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Router.build("AddQuestionActivity").go(context);
                }
            });
        }
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            tv_qa_btn.setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        }
        view.showEmptyView(emptyView);
        mInflater = null;
        parent = null;
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
