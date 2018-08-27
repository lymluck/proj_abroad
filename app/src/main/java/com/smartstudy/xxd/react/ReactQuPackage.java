package com.smartstudy.xxd.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.smartstudy.xxd.react.ReactQuModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yqy on 2017/11/2.
 */

public class ReactQuPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> list = new ArrayList<>();
        list.add(new ReactQuModule(reactContext));
        return list;
    }


    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}