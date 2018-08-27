package com.smartstudy.router.matcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Base mather for implicit intent.
 * <p>
 * Created by louis on 2017/3/12.
 */
public abstract class AbsImplicitMatcher extends AbsMatcher {

    public AbsImplicitMatcher(int priority) {
        super(priority);
    }

    @Override
    public Object generate(Context context, Uri uri, @Nullable Class<?> target) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

}
