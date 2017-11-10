package com.palprotech.heylaapp.utils;

/**
 * Created by Narendar on 09/11/17.
 */

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class DayViewSliderUtil {
    public static Drawable setDrawableBackgroundColor(Drawable drawable, int color){
        GradientDrawable gradientDrawable = (GradientDrawable) drawable.mutate();
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }
}