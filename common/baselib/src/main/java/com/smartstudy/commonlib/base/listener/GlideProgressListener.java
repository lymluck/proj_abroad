package com.smartstudy.commonlib.base.listener;

/**
 * @author louis
 * @date on 2018/3/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public interface GlideProgressListener {
    void onProgress(long bytesRead, long expectedLength);

    /**
     * Control how often the listener needs an update. 0% and 100% will always be dispatched.
     *
     * @return in percentage (0.2 = call {@link #onProgress} around every 0.2 percent of progress)
     */
    float getGranularityPercentage();
}
