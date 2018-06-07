package com.carflash.user.accounts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lenovo-pc on 8/5/2017.
 */

public class TypeFaceDosisSemiBold extends TextView {

    public TypeFaceDosisSemiBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypeFaceDosisSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypeFaceDosisSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Dosis-Regular.ttf", context);
        setTypeface(customFont);
    }

}