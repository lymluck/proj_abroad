package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 2017/3/2.
 */

public class DataListInfo {

    private PaginationInfo pagination;
    private String data;
    private String meta;

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "DataListInfo{" +
                "paginationInfo=" + pagination +
                ", data='" + data + '\'' +
                ", meta='" + meta + '\'' +
                '}';
    }
}
