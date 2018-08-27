package com.smartstudy.commonlib.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by louis on 17/5/3.
 */

public class SerializableMap implements Serializable {
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
