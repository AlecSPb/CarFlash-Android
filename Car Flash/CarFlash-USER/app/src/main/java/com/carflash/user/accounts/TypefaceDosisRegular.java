package com.carflash.user.accounts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by samir on 19/01/17.
 */

public class TypefaceDosisRegular extends TextView {

    public TypefaceDosisRegular(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypefaceDosisRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypefaceDosisRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Dosis-Regular.ttf", context);
        setTypeface(customFont);
    }

}

