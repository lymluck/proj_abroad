package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;
import com.smartstudy.qrcode.CodeScanActivity;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;

import java.util.Arrays;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

/**
 * @author louis
 * @date on 2018/5/10
 * @describe 二维码扫描
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class QrCodeScanActivity extends CodeScanActivity implements PermissionUtil.PermissionCallbacks {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        if (!PermissionUtil.hasPermissions(this, Permission.CAMERA)) {
            PermissionUtil.requestPermissions(this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
        }
        super.onResume();
        UApp.pageSessionStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UApp.pageSessionEnd(this);
    }

    @Override
    public void handleResult(final String result) {
        super.handleResult(result);
        if (result.contains("login://")) {
            goLogin(result);
        } else if (result.startsWith("http") || result.startsWith("https")) {
            Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
            toMoreDetails.putExtra(WEBVIEW_URL, result);
            toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
            startActivity(toMoreDetails);
        } else {
            String content = "<html><br/><font style='font-size:2.3em'>&nbsp;&nbsp;扫码结果：" + result + "</font></html>";
            Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
            toMoreDetails.putExtra(WEBVIEW_URL, content);
            toMoreDetails.putExtra(TITLE, "扫码结果");
            toMoreDetails.putExtra("use_title", true);
            toMoreDetails.putExtra("showMenu", false);
            toMoreDetails.putExtra(WEBVIEW_ACTION, "html");
            startActivity(toMoreDetails);
        }
        finish();
    }

    public void goLogin(String params) {
        startActivity(new Intent(this, QrLoginActivity.class)
            .putExtra("params", params));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onStart();
        findViewById(R.id.sbv_scan).postInvalidate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }
}
