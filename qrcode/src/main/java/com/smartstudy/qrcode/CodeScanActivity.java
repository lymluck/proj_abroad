package com.smartstudy.qrcode;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.qrcode.qrdecode.BarcodeFormat;
import com.smartstudy.qrcode.qrscan.camera.CameraManager;
import com.smartstudy.qrcode.qrscan.decoding.CaptureActivityHandler;
import com.smartstudy.qrcode.tools.StatusBarUtils;

import java.io.IOException;

/**
 * @author luoyongming
 * @date on 2018/5/8
 * @describe 扫一扫
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class CodeScanActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private SurfaceHolder myHolder;
    private BarcodeFormat barcodeFormat;
    private SoundPool mSoundPool;
    private int mFocusSoundId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        CameraManager.init(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan);
        initViewAndData();
        initSystemBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasSurface) {
            initCamera(myHolder);
        } else {
            myHolder.addCallback(this);
            myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        if (handler != null && hasSurface) {
            handler.restartPreviewAndDecode();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        handler = null;
    }

    private void stopCamera() {
        if (handler != null) {
            handler.quitSynchronously();
        }
        CameraManager.get().closeDriver();
    }

    private void initViewAndData() {
        hasSurface = false;
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        myHolder = surfaceView.getHolder();
        TextView topTitle = findViewById(R.id.top_title);
        topTitle.setText("扫一扫");
        barcodeFormat = new BarcodeFormat();
        barcodeFormat.add(BarcodeFormat.BARCODE);
        barcodeFormat.add(BarcodeFormat.QRCODE);
        findViewById(R.id.top_leftbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        mFocusSoundId = mSoundPool.load(this, com.smartstudy.qrcode.R.raw.beep, 1);
        if (handler == null) {
            handler = new CaptureActivityHandler(this, barcodeFormat);
        }
    }

    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        int result = StatusBarUtils.setLightMode(this);
        if (result == 3) {
            // 6.0以上沉浸式
            StatusBarUtils.setColor(this, getResources().getColor(R.color.top_bg_color), 0);
        } else if (result == 4) {
            // 其它半透明效果
            StatusBarUtils.setColor(this, getResources().getColor(R.color.top_bg_color));
        } else {
            // miui、flyme沉浸式
            StatusBarUtils.setColor(this, getResources().getColor(R.color.top_bg_color), 0);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
        }
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
        if (holder == null) {
            return;
        }
        stopCamera();
        if (handler != null) {
            initCamera(holder);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handler.restartPreviewAndDecode();
                }
            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                CameraManager.get().zoomIn();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                CameraManager.get().zoomOut();
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * @param result
     */
    public void handleResult(String result) {
        mSoundPool.play(mFocusSoundId, 1f, 1f, 0, 0, 1f);
    }
}

