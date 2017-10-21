package com.ventura.emilp.tournamentbrackets.utility;

import android.util.DisplayMetrics;

import com.ventura.emilp.tournamentbrackets.application.BracketsApplication;

/**
 * Created by Emil on 21/10/17.
 */

public class BracketsUtility {
    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = BracketsApplication.getInstance().getBaseContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
