package com.mkchx.widget.fabmenu.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author Efthimis Charitonidis
 */
public class ConvertUtil {
    public static double dpToPx(Context context, double dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        double px = dp * (metrics.densityDpi / 160f);

        return px;
    }
}
