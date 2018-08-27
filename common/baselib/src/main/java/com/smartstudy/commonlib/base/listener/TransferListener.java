package com.smartstudy.commonlib.base.listener;

import com.smartstudy.commonlib.ui.customview.photoview.PhotoView;

/**
 * Created by louis on 17/7/20.
 */

public interface TransferListener {

    void startTransOut(PhotoView view);

    void animEnd();
}
