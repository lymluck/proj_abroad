package com.smartstudy.xxd.ui.adapter.homesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.adapter.base.SectionedRecyclerViewAdapter;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeSearchInfo;
import com.smartstudy.xxd.entity.HomeSearchListInfo;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;
import com.smartstudy.xxd.ui.activity.QaDetailActivity;
import com.smartstudy.xxd.ui.activity.SchoolDetailActivity;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import java.util.List;

/**
 * Created by louis on 17/5/19.
 */

public class HomeSearchAdapter extends SectionedRecyclerViewAdapter<HomeSearchHeaderHolder, HomeSearchContentHolder, HomeSearchFooterHolder> {


    public List<HomeSearchListInfo> mDatas;
    private Activity mContext;
    private LayoutInflater mInflater;

    public HomeSearchAdapter(Activity context, List<HomeSearchListInfo> listInfos) {
        mContext = context;
        this.mDatas = listInfos;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected int getSectionCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<HomeSearchInfo> datas = mDatas.get(section).getDatas();
        return datas != null ? datas.size() : 0;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        HomeSearchListInfo info = mDatas.get(section);
        int total = Integer.parseInt(info.getTypeTotal(), 10);
        if (total > 5) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected HomeSearchHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HomeSearchHeaderHolder(mInflater.inflate(R.layout.item_home_search_title, parent, false));
    }


    @Override
    protected HomeSearchFooterHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new HomeSearchFooterHolder(mInflater.inflate(R.layout.item_home_search_footer, parent, false));
    }

    @Override
    protected HomeSearchContentHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new HomeSearchContentHolder(mInflater.inflate(R.layout.item_home_search_list, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HomeSearchHeaderHolder holder, final int section) {
        holder.titleView.setText(mDatas.get(section).getTypeName());
    }

    @Override
    protected void onBindSectionFooterViewHolder(final HomeSearchFooterHolder holder, int section) {
        final HomeSearchListInfo info = mDatas.get(section);
        holder.tv_type_more.setText(String.format(mContext.getString(R.string.home_search_foot), info.getTypeName(), info.getTypeTotal()));
        holder.itemView.findViewById(R.id.llyt_search_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearch = new Intent(mContext, CommonSearchActivity.class);
                switch (info.getTypeName()) {
                    case "院校":
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.SCHOOLS_FLAG);
                        break;
                    case "课程":
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.COURSE_FLAG);
                        break;
                    case "问答":
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.HOME_QA_FLAG);
                        break;
                    case "资讯":
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.NEWS_FLAG);
                        break;
                    default:
                        break;
                }
                toSearch.putExtra("keyword", info.getKeyword());
                toSearch.putExtra("typeName", info.getTypeName());
                mContext.startActivity(toSearch);
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(HomeSearchContentHolder holder, final int section, final int position) {
        HomeSearchListInfo info = mDatas.get(section);
        HomeSearchInfo data = info.getDatas().get(position);
        switch (info.getTypeName()) {
            case "院校":
                holder.item_school.setVisibility(View.VISIBLE);
                holder.item_course.setVisibility(View.GONE);
                holder.item_news.setVisibility(View.GONE);
                holder.item_qa.setVisibility(View.GONE);
                DisplayImageUtils.formatCircleImgUrl(mContext, data.getLogo(), holder.iv_school_logo);
                holder.tv_school_name.setText(data.getChineseName());
                holder.tv_school_eg_name.setText(data.getEnglishName());
                clickSchoolItem(holder, data);
                break;
            case "课程":
                holder.item_school.setVisibility(View.GONE);
                holder.item_course.setVisibility(View.VISIBLE);
                holder.item_news.setVisibility(View.GONE);
                holder.item_qa.setVisibility(View.GONE);
                DisplayImageUtils.formatImgUrl(mContext, data.getCoverUrl(), holder.iv_course_cover);
                holder.tv_course_name.setText(data.getName());
                holder.rb_course_rate.setStar(Float.parseFloat(data.getRate()));
                holder.tv_course_rate.setText(data.getRate());
                holder.tv_course_see.setText(String.format(mContext.getString(R.string.visit_count), data.getVisitCount()));
                clickCourseItem(holder, data);
                break;
            case "问答":
                holder.item_school.setVisibility(View.GONE);
                holder.item_course.setVisibility(View.GONE);
                holder.item_news.setVisibility(View.GONE);
                holder.item_qa.setVisibility(View.VISIBLE);
                String avatar = data.getAsker().getAvatar();
                DisplayImageUtils.formatPersonImgUrl(mContext, avatar, holder.iv_asker);

                holder.tv_qa_default_name.setVisibility(View.GONE);

                holder.tv_qa_name.setText(data.getAsker().getName());
                holder.tv_qa_see.setText(String.format(mContext.getString(R.string.visit_count), data.getVisitCount()));
                holder.tv_qa.setText(data.getContent());
                holder.tv_qa_time.setText(data.getCreateTimeText());
                clickQaItem(holder, data);
                break;
            case "资讯":
                holder.item_school.setVisibility(View.GONE);
                holder.item_course.setVisibility(View.GONE);
                holder.item_news.setVisibility(View.VISIBLE);
                holder.item_qa.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(data.getCoverUrl())) {
                    holder.iv_news_cover.setVisibility(View.VISIBLE);
                    DisplayImageUtils.formatImgUrl(mContext, data.getCoverUrl(), holder.iv_news_cover);
                } else {
                    holder.iv_news_cover.setVisibility(View.GONE);
                }
                holder.tv_news_title.setText(data.getTitle());
                if (!TextUtils.isEmpty(data.getTagName())) {
                    holder.tv_news_tag.setVisibility(View.VISIBLE);
                    holder.tv_news_tag.setText(data.getTagName());
                } else {
                    holder.tv_news_tag.setVisibility(View.GONE);
                }
                holder.tv_news_see.setText(String.format(mContext.getString(R.string.visa_see), data.getVisitCount()));
                clickNewsItem(holder, data);
                break;
            default:
                break;
        }
    }

    private void clickSchoolItem(HomeSearchContentHolder holder, final HomeSearchInfo info) {
        holder.itemView.findViewById(R.id.item_school).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到学校详情页面
                Bundle data = new Bundle();
                data.putString("id", info.getId());
                Intent toMoreDetails = new Intent(mContext, SchoolDetailActivity.class);
                toMoreDetails.putExtras(data);
                mContext.startActivity(toMoreDetails);
            }
        });
    }

    private void clickCourseItem(HomeSearchContentHolder holder, final HomeSearchInfo info) {
        holder.itemView.findViewById(R.id.item_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CourseDetailActivity.class)
                        .putExtra("id", info.getProductId())
                        .putExtra("courseCover", info.getCoverUrl()));
            }
        });
    }

    private void clickNewsItem(HomeSearchContentHolder holder, final HomeSearchInfo info) {
        holder.itemView.findViewById(R.id.item_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMoreDetails = new Intent(mContext, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_NEWS_DETAIL), info.getId()));
                toMoreDetails.putExtra("title", info.getTitle());
                toMoreDetails.putExtra("url_action", "get");
                mContext.startActivity(toMoreDetails);
            }
        });
    }

    private void clickQaItem(HomeSearchContentHolder holder, final HomeSearchInfo info) {
        holder.itemView.findViewById(R.id.item_qa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMoreDetails = new Intent(mContext, QaDetailActivity.class);
                toMoreDetails.putExtra("id", info.getId() + "");
                mContext.startActivity(toMoreDetails);
            }
        });
    }

    public void destroy() {
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }
}
