package com.smartstudy.commonlib.mvp.presenter;

import android.graphics.Bitmap;

import com.smartstudy.commonlib.app.BaseApplication;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.mvp.contract.BrowerPictureContract;
import com.smartstudy.commonlib.utils.BitmapUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;

import java.io.File;

/**
 * Created by louis on 2017/3/4.
 */

public class BrowerPicturePresenter implements BrowerPictureContract.Presenter {
    private BrowerPictureContract.View view;

    public BrowerPicturePresenter(BrowerPictureContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void savePic(Bitmap btp) {
        final String filePath = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures").getAbsolutePath();
        String fileName = System.currentTimeMillis() + ".png";
        if (BitmapUtils.saveBitmap(btp, fileName, filePath, BaseApplication.appContext)) {
            view.showTip(null, BaseApplication.appContext.getString(R.string.file_save_to) + filePath);

        } else {
            view.showTip(null, BaseApplication.appContext.getString(R.string.save_failed));
        }
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}
