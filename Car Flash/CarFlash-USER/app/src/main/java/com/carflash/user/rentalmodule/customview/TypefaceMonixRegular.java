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
public class TypefaceMonixRegular extends TextView {

    public TypefaceMonixRegular(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypefaceMonixRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypefaceMonixRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Monix-Regular.otf", context);
        setTypeface(customFont);
    }

}
