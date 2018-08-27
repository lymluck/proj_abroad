package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.utils.DensityUtils;

/**
 * Created by yqy on 2018/1/3.
 */

public class ViewLine extends View {

    private Bitmap mBitmap;

    private Matrix mMatrix = new Matrix();

    private Context context;

    public ViewLine(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public ViewLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }


    private void initialize() {
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.v_line)).getBitmap();
        WindowManager wm = (WindowManager) context
            .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        float atanValue = (float) (Math.round(75 * 10000) / (DensityUtils.px2dip(width) / 2 * 10000.0000) - 0.005);
        mMatrix.postRotate((float) (Math.atan(atanValue) / Math.PI * 180));


    }

    @Override
    protected void onDraw(Canvas canvas) {

        //      super.onDraw(canvas);  //当然，如果界面上还有其他元素需要绘制，只需要将这句话写上就行了。

        canvas.drawBitmap(mBitmap, mMatrix, null);

    }

}

