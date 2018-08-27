package com.smartstudy.xxd.entity;

import android.net.Uri;
import android.os.Parcel;

import io.rong.common.ParcelUtils;
import io.rong.imlib.model.UserInfo;

/**
 * Created by yqy on 2018/1/8.
 */

public class MyRYUserInfo extends UserInfo {

    private String title;
    private String yearsOfWorking;

    public MyRYUserInfo(String id, String name, Uri portraitUri, String title, String yearsOfWorking) {
        super(id, name, portraitUri);
        this.title = title;
        this.yearsOfWorking = yearsOfWorking;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearsOfWorking() {
        return yearsOfWorking;
    }

    public void setYearsOfWorking(String yearsOfWorking) {
        this.yearsOfWorking = yearsOfWorking;
    }


}
