package com.smartstudy.router;

import android.net.Uri;

/**
 * <p>
 * Created by louis on 2016/12/20.
 */
public interface RouteCallback {
    /**
     * Callback
     *
     * @param state   {@link RouteResult}
     * @param uri     Uri
     * @param message notice msg
     */
    void callback(RouteResult state, Uri uri, String message);
}
