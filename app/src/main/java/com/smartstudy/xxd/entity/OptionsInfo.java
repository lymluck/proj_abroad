package com.smartstudy.xxd.entity;

import java.io.Serializable;

/**
 * Created by yqy on 2017/12/7.
 */

public class OptionsInfo implements Serializable {
    private String id;

    private String name;

    private String targetDegreeId;

    private boolean isSelected;

    public OptionsInfo(String id, String name, String targetDegreeId) {
        this.id = id;
        this.name = name;
        this.targetDegreeId = targetDegreeId;
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
