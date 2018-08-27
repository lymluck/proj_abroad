package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * Created by louis on 2017/10/12.
 */

public class HomeHotListInfo {

    private String typeName;
    private List<HomeHotInfo> hotInfoList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<HomeHotInfo> getHotInfoList() {
        return hotInfoList;
    }

    public void setHotInfoList(List<HomeHotInfo> hotInfoList) {
        this.hotInfoList = hotInfoList;
    }
}
