package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.ui.customview.calendar.entity.CalendarInfo;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ExamDateInfo;
import com.smartstudy.xxd.mvp.contract.ExamDateContract;
import com.smartstudy.xxd.mvp.model.ExamDateModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */
public class ExamDatePresenter implements ExamDateContract.Presenter {

    private ExamDateContract.View view;

    public ExamDatePresenter(ExamDateContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getExamDate(int cacheType, boolean plain, final String exam) {
        ExamDateModel.getExamDate(cacheType, plain, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {
                    JSONObject meta = obj.getJSONObject("meta");
                    List<String> dateList = null;
                    String myCount = "0";
                    String updateTime = "";
                    if (meta != null) {
                        dateList = JSON.parseArray(meta.getString("months"), String.class);
                        myCount = meta.getString("mySelectCount");
                        updateTime = meta.getString("updateTimeText");
                    }
                    if (dateList != null) {
                        synchronized (this) {
                            List<ExamDateInfo> datas = JSON.parseArray(obj.getString("data"), ExamDateInfo.class);
                            view.getDateSuccess(updateTime, myCount, datas);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar calendar = Calendar.getInstance();
                            int size = dateList.size();
                            List<CalendarInfo> list = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                boolean needLine = false;
                                if (i < size - 1) {
                                    needLine = true;
                                }
                                list.clear();
                                String dateStr = dateList.get(i);
                                Date date = null;
                                try {
                                    date = format.parse(dateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date != null) {
                                    for (ExamDateInfo info : datas) {
                                        Date chooseDate = null;
                                        try {
                                            chooseDate = format.parse(info.getDate());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (chooseDate != null) {
                                            calendar.setTime(chooseDate);
                                            int desYear = calendar.get(Calendar.YEAR);
                                            int desMonth = calendar.get(Calendar.MONTH) + 1;
                                            int desDay = calendar.get(Calendar.DAY_OF_MONTH);
                                            if (info.getItems().size() > 1) {
                                                ExamDateInfo.ItemsEntity selectItem = getSelectItem(info.getItems());
                                                if (selectItem != null) {
                                                    // 已经参加考试
                                                    list.add(new CalendarInfo(selectItem.getId(), desYear,
                                                            desMonth, desDay, selectItem.getExam(),
                                                            selectItem.getSelectCount(), true));
                                                } else {
                                                    ExamDateInfo.ItemsEntity filterItem = getFilterItem(exam, info.getItems());
                                                    if (filterItem != null) {
                                                        // 筛选考试
                                                        list.add(new CalendarInfo(filterItem.getId(), desYear,
                                                                desMonth, desDay, filterItem.getExam(),
                                                                filterItem.getSelectCount(), false));
                                                    } else {
                                                        String itemJson = info.getItems().toString();
                                                        list.add(new CalendarInfo(-1, desYear, desMonth, desDay,
                                                                "多种考试", itemJson, false));
                                                    }
                                                }
                                            } else {
                                                ExamDateInfo.ItemsEntity entity = info.getItems().get(0);
                                                list.add(new CalendarInfo(entity.getId(), desYear, desMonth,
                                                        desDay, entity.getExam(), entity.getSelectCount(),
                                                        entity.isSelected()));
                                            }
                                        }

                                    }
                                    calendar.setTime(date);
                                    int currYear = calendar.get(Calendar.YEAR);
                                    int currMonth = calendar.get(Calendar.MONTH);
                                    view.initDates(list, currYear, currMonth, needLine, exam);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private ExamDateInfo.ItemsEntity getSelectItem(List<ExamDateInfo.ItemsEntity> datas) {
        for (ExamDateInfo.ItemsEntity entity : datas) {
            if (entity.isSelected()) {
                return entity;
            }
        }
        return null;
    }

    private ExamDateInfo.ItemsEntity getFilterItem(String exam, List<ExamDateInfo.ItemsEntity> datas) {
        for (ExamDateInfo.ItemsEntity entity : datas) {
            if (exam.equals(entity.getExam())) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void joinExam(int id) {
        ExamDateModel.joinExam(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.joinExamSuccess();
            }
        });

    }

    @Override
    public void delExam(int id) {
        ExamDateModel.delExam(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.delExamSuccess();
            }
        });
    }

    @Override
    public List<String> getJoinDates(List<ExamDateInfo> datas, String des) {
        List<String> joinList = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (ExamDateInfo info : datas) {
            // 判断考试时间是否过期
            try {
                if (df.parse(DateTimeUtils.getTimeOnlyMd()).getTime()
                        > df.parse(info.getDate()).getTime()) {
                    continue;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateTimeUtils.getTimeOnlyMd();
            String dateStr = getDateStr(info.getDate());
            List<ExamDateInfo.ItemsEntity> items = info.getItems();
            for (ExamDateInfo.ItemsEntity item : items) {
                if (item.isSelected() && des.equals(item.getExam())) {
                    if (!TextUtils.isEmpty(dateStr)) {
                        joinList.add(dateStr);
                    }
                }
            }
        }
        return joinList;
    }

    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
            Button tv_refresh_btn = emptyView.findViewById(R.id.tv_refresh_btn);
            tv_refresh_btn.setVisibility(View.VISIBLE);
            tv_refresh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //重新加载
                    view.reload();
                }
            });
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        context = null;
        emptyView = null;
    }

    /**
     * 判断当前日期是星期几  String pTime = "2012-03-12"
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    private String getDateStr(String pTime) {
        String[] strings = pTime.split("-");
        if (strings.length != 3) {
            return "";
        }
        String dateStr = strings[0] + "年" + strings[1] + "月" + strings[2] + "日";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dateStr += " 周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            dateStr += " 周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            dateStr += " 周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            dateStr += " 周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            dateStr += " 周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            dateStr += " 周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            dateStr += " 周六";
        }
        return dateStr;
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
