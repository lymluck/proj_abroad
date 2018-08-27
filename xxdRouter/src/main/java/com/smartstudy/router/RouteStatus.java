package com.smartstudy.router;

/**
 * Result for each route.
 * <p>
 * Created by louis on 2017/3/9.
 */
public enum RouteStatus {
    PROCESSING,
    SUCCEED,
    INTERCEPTED,
    NOT_FOUND,
    FAILED;

    public boolean isSuccessful() {
        return this == SUCCEED;
    }
}
