package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.mvp.contract.ProgramInfoContract;
import com.smartstudy.xxd.mvp.model.MajorModel;

/**
 * Created by louis on 2017/3/2.
 */

public class ProgramInfoPresenter implements ProgramInfoContract.Presenter {

    private ProgramInfoContract.View view;

    public ProgramInfoPresenter(ProgramInfoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getProgramInfo(String id) {
        MajorModel.getProgramInfo(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    String logo = data.getJSONObject("school").getString("logo");
                    String schoolName = data.getJSONObject("school").getString("chineseName");
                    view.showTop(logo, data.getString("chineseName"), data.getString("englishName"),
                        schoolName, data.getString("categoryRank"));
                    view.showFeature(data.getString("introduction"));
                    view.showRequest(data.getString("requirements"));
                    view.showCourse(data.getString("courses"));
                }
            }
        });

    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
