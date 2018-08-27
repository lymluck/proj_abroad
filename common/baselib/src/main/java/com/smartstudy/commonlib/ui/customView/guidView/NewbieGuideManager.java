package com.smartstudy.commonlib.ui.customView.guidView;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;


public class NewbieGuideManager {

    private static final String TAG = "newbie_guide";

    public static final int TYPE_SAMRT_CHOOSE = 1;//智能选校
    public static final int TYPE_SPE_RESULT = 2;//智能选专业

    private NewbieGuide mNewbieGuide;
    private int mType;

    public NewbieGuideManager(Activity activity, int type) {
        mNewbieGuide = new NewbieGuide(activity);
        mType = type;
    }

    public NewbieGuideManager addView(View view, int shape) {
        mNewbieGuide.addHighLightView(view, shape);
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int delayTime) {
        SPCacheUtils.put(TAG + mType, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_SAMRT_CHOOSE:
                        //调整位置坐标
                        mNewbieGuide.setEveryWhereTouchable(false).addIndicateImg(R.drawable.tip_smartchoose_arrow, DensityUtils.dip2px(
                                -60), DensityUtils.dip2px(165))
                                .addMessage(R.drawable.tip_smartchoose_word,
                                        DensityUtils.dip2px(-25), DensityUtils.dip2px(200))
                                .addKnowTv(R.drawable.tip_smartchoose_btn, NewbieGuide.CENTER, ScreenUtils.getScreenHeight() - DensityUtils.dip2px(200))
                                .show();
                        break;
                    case TYPE_SPE_RESULT:
                        //调整位置坐标
                        mNewbieGuide.setEveryWhereTouchable(false).addIndicateImg(R.drawable.tip_smartchoose_arrow, DensityUtils.dip2px(
                                -60), DensityUtils.dip2px(165))
                                .addMessage(R.drawable.tip_smartchoose_word,
                                        DensityUtils.dip2px(-25), DensityUtils.dip2px(200))
                                .addKnowTv(R.drawable.tip_smartchoose_btn, NewbieGuide.CENTER, ScreenUtils.getScreenHeight() - DensityUtils.dip2px(200))
                                .show();
                        break;
                }
            }
        }, delayTime);
    }

    public void showWithListener(int delayTime, NewbieGuide.OnGuideChangedListener onGuideChangedListener) {
        mNewbieGuide.setOnGuideChangedListener(onGuideChangedListener);
        show(delayTime);
    }

    /**
     * 判断新手引导也是否已经显示了
     */
    public static boolean isNeverShowed(Activity activity, int type) {
        return (boolean) SPCacheUtils.get(TAG + type, true);
    }

}
