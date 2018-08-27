package com.smartstudy.xxd.mvp.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.SpecialQaInfo;
import com.smartstudy.xxd.mvp.contract.SrtChooseSpecialContract;
import com.smartstudy.xxd.mvp.model.SrtChooseSpecialModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtChooseSpecialPresenter implements SrtChooseSpecialContract.Presenter {

    private SrtChooseSpecialContract.View view;
    private List<String> mType = Arrays.asList("a", "s", "e", "c", "r", "i"); //问题类型
    private JSONObject answers;


    public SrtChooseSpecialPresenter(SrtChooseSpecialContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void goChooseQa() {
        SrtChooseSpecialModel.goChooseQa(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    final List<SpecialQaInfo> list = JSON.parseArray(data.getString("questions"), SpecialQaInfo.class);
                    answers = data.getJSONObject("answers");
                    List<SpecialQaInfo> mDatas = new ArrayList<>(); //存放每种类型的题目
                    List<SpecialQaInfo> qa_list = new ArrayList<>(); //存放随机后的题目
                    Random random = new Random(System.currentTimeMillis() / 3600000); //带种子的随机数
                    for (String s : mType) {
                        for (SpecialQaInfo info : list) {
                            if (info.getType().equals(s)) {
                                mDatas.add(info);
                            }
                        }
                        //每种类型的题目中随机选两道
                        for (int i = 0; i < 2; i++) {
                            int id = random.nextInt(mDatas.size()); //得到随机的题目id
                            qa_list.add(mDatas.get(id));
                            mDatas.remove(id);
                        }
                        //清空当前类型
                        mDatas.clear();
                    }
                    view.showQa(qa_list, answers);
                }
            }
        });
    }

    @Override
    public void getHasTestNum() {
        SrtChooseSpecialModel.getHasTest(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    view.getHasTestNumSuccess(result);
                }
            }
        });
    }

    @Override
    public void handlerScore(List<SpecialQaInfo> list) {
        Map<String, Object> map_data = new HashMap<>(); //雷达图展示数据
        Map<String, Object> map_fit = new HashMap<>(); //适合用户的数据
        float maxScore = 17.5f; //最大值
        for (String s : mType) {
            float score = 17.5f; //初始分值17.5
            for (SpecialQaInfo info : list) {
                if (info.getType().equals(s)) {
                    if (info.getScore() == 1) {
                        score += 32.5f; //分值增值为32.5
                    }
                }
            }
            map_data.put(s, score);
            if (score > 80) {
                map_fit.put(s, score);
            } else {
                if (score >= maxScore) {
                    maxScore = score;
                }
            }
        }
        if (map_fit.isEmpty()) {
            for (Map.Entry<String, Object> entry : map_data.entrySet()) {
                float mScore = (float) entry.getValue();
                if (mScore == maxScore) {
                    map_fit.put(entry.getKey(), entry.getValue());
                }
            }
        }
        //适合用户类型的数据
        JSONArray arr_fit = getFitData(map_fit);
        //推荐院校
        JSONArray arr_rec = new JSONArray(getRecomData(arr_fit));
        view.showResult(map_data, arr_fit, arr_rec);
    }

    private JSONArray getFitData(Map<String, Object> map_fit) {
        JSONArray arr_fit = new JSONArray();
        for (Map.Entry<String, Object> entry : map_fit.entrySet()) {
            arr_fit.add(answers.getJSONObject(entry.getKey()));
        }
        return arr_fit;
    }

    private List<Object> getRecomData(JSONArray arr_fit) {
        JSONArray jsonArray = new JSONArray();
        int len = arr_fit.size();
        for (int i = 0; i < len; i++) {
            JSONObject data = arr_fit.getJSONObject(i);
            JSONArray rs_arr = data.getJSONArray("recommendSchools");
            jsonArray.addAll(rs_arr.subList(0, rs_arr.size() / len));
        }
        JSONArray arr_result = new JSONArray();
        //去重
        for (int i = 0, ll = jsonArray.size(); i < ll; i++) {
            Object obj = jsonArray.get(i);
            if (!arr_result.contains(obj)) {
                arr_result.add(obj);
            }
        }
        //取6个作为推荐院校
        return arr_result.subList(0, 6);
    }
}
