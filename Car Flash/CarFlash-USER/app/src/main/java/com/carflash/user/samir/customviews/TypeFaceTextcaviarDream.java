package com.carflash.user.samir.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by samir on 19/01/17.
 */

public class TypeFaceTextcaviarDream  extends TextView {

    public TypeFaceTextcaviarDream(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypeFaceTextcaviarDream(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypeFaceTextcaviarDream(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("CaviarDreams.ttf", context);
        setTypeface(customFont);
    }

}

