package com.carflash.user.samir.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 2/28/2017.
 */

public class TypeFaceGothic extends TextView {

    public TypeFaceGothic(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypeFaceGothic(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypeFaceGothic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("JLSSpaceGothicR_NC.otf", context);
        setTypeface(customFont);
    }

}

