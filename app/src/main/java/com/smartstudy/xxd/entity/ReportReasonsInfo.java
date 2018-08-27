package com.smartstudy.xxd.entity;

/**
 * Created by yqy on 2017/12/27.
 */

public class ReportReasonsInfo {
    private String id;
    private String name;
    private boolean isSelect=false;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

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
}
