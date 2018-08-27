/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartstudy.qrcode.qrscan.decoding;

import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.smartstudy.qrcode.CodeScanActivity;
import com.smartstudy.qrcode.R;
import com.smartstudy.qrcode.qrdecode.BarcodeFormat;
import com.smartstudy.qrcode.qrdecode.DecodeEntry;
import com.smartstudy.qrcode.qrscan.camera.CameraManager;

import java.lang.ref.WeakReference;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 */
public final class CaptureActivityHandler extends Handler {

    private WeakReference<CodeScanActivity> mCodeActiviy;
    private HandlerThread thread;
    private Handler mHandler;

    public CaptureActivityHandler(CodeScanActivity activity, final BarcodeFormat decodeFormat) {
        mCodeActiviy = new WeakReference<>(activity);
        thread = new HandlerThread("decodeThread");
        thread.start();
        mHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                if (what == R.id.decode) {
                    decode(decodeFormat, (byte[]) msg.obj, msg.arg1, msg.arg2);
                }
            }
        };
        // Start ourselves capturing previews and decoding.
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        int what = message.what;
        if (what == R.id.auto_focus) {
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
    }

    public void quitSynchronously() {
        CameraManager.get().stopPreview();

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.auto_focus);
    }

    public void restartPreviewAndDecode() {
        CameraManager.get().startPreview();
        CameraManager.get().requestPreviewFrame(mHandler, R.id.decode);
        CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(BarcodeFormat decodeFormat, byte[] data, int width, int height) {
        String result = null;
        Rect rect = CameraManager.get().getRealFramingRect();
        result = DecodeEntry.getDecodeResult(decodeFormat, data, width, height, rect.left, rect.top,
            rect.width(), rect.height());

        if (!TextUtils.isEmpty(result)) {
            CameraManager.get().stopPreview();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                thread.quitSafely();
            } else {
                thread.quit();
            }
            if (mHandler != null) {
                mHandler.removeMessages(R.id.decode);
                mHandler = null;
            }
            removeMessages(R.id.auto_focus);
            mCodeActiviy.get().handleResult(result);
        } else {
            CameraManager.get().requestPreviewFrame(mHandler, R.id.decode);
        }
    }

}
