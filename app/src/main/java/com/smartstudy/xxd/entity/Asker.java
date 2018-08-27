package com.smartstudy.xxd.entity;

import android.widget.ScrollView;

import java.io.Serializable;

/**
 * Created by yqy on 2017/12/4.
 */

public class Asker implements Serializable{
    private String id;

    private String name;

    private String avatar;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}