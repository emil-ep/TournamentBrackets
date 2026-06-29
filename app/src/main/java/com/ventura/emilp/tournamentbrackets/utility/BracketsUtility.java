package com.ventura.emilp.tournamentbrackets.utility;

import android.content.res.Resources;

/**
 * Created by Emil on 21/10/17.
 */

public class BracketsUtility {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
