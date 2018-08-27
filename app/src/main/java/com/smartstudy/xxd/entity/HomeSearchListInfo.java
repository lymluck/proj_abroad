package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * Created by louis on 17/5/19.
 */

public class HomeSearchListInfo {
    private String typeName;
    private String typeTotal;
    private String keyword;
    private List<HomeSearchInfo> datas;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeTotal() {
        return typeTotal;
    }

    public void setTypeTotal(String typeTotal) {
        this.typeTotal = typeTotal;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<HomeSearchInfo> getDatas() {
        return datas;
    }

    public void setDatas(List<HomeSearchInfo> datas) {
        this.datas = datas;
    }
}
