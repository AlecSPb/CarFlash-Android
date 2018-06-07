package com.carflash.user.rentalmodule.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lenovo-pc on 6/22/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TypefaceMonixBold extends TextView {

    public TypefaceMonixBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypefaceMonixBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypefaceMonixBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("MonixBold-Regular.otf", context);
        setTypeface(customFont);
    }

}
