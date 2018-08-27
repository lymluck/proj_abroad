package com.smartstudy.commonlib.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.qrcode.qrdecode.BarcodeFormat;
import com.smartstudy.qrcode.qrdecode.DecodeEntry;

/**
 * @author louis
 * @date on 2018/3/20
 * @describe 扫描二维码
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ScanQrcodeTask extends AsyncTask<Bitmap, Integer, String> {

    private WeakHandler mHandler;
    private BarcodeFormat barcodeFormat;

    public ScanQrcodeTask(WeakHandler handler) {
        this.mHandler = handler;
        this.barcodeFormat = new BarcodeFormat();
        this.barcodeFormat.add(BarcodeFormat.BARCODE);
        this.barcodeFormat.add(BarcodeFormat.QRCODE);
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        return DecodeEntry.getPixelsByBitmap(bitmaps[0], barcodeFormat, false);
    }

    @Override
    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            Message msg = Message.obtain();
            msg.what = ParameterUtils.MSG_WHAT_REFRESH;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }
}
