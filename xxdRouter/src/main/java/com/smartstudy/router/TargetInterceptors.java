package com.smartstudy.router;

import android.support.v4.util.ArrayMap;

/**
 * Target interceptorTable mapping.
 * <p>
 * Created by louis on 2017/6/29.
 */
public interface TargetInterceptors {
    /**
     * Mapping between target and interceptorTable, the target class may be an {@link android.app.Activity},
     * {@link android.app.Fragment} or {@link android.support.v4.app.Fragment}.
     *
     * @param map target -> interceptorTable.
     */
    void handle(ArrayMap<Class<?>, String[]> map);
}
