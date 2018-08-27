package com.smartstudy.commonlib.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.zbar.zxing.QRCodeDecoder;

/**
 * @author louis
 * @date on 2018/3/20
 * @describe 扫描二维码
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ScanQrcodeTask extends AsyncTask<String, Integer, String> {

    private String mUrl;
    private WeakHandler mHandler;

    public ScanQrcodeTask(Context context, String url, WeakHandler handler) {
        this.mUrl = url;
        this.mHandler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {
        return QRCodeDecoder.syncDecodeQRCode(mUrl);
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
