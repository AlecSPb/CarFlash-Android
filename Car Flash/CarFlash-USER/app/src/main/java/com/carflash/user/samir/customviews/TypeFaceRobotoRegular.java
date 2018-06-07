package com.carflash.user.samir.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 3/18/2017.
 */

public class TypeFaceRobotoRegular extends TextView {

    public TypeFaceRobotoRegular(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypeFaceRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypeFaceRobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("roboto_regular.ttf", context);
        setTypeface(customFont);
    }

}
