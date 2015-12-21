package crazysheep.io.materialmusic.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * helper class for system ui
 *
 * Created by crazysheep on 15/12/21.
 */
public class SystemUIHelper {

    /**
     * get toolbar(actionbar) size
     * */
    public static int getToolbarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }

        return 0;
    }

    /**
     * get statusbar size
     * */
    public static int getStatusBarSize(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

}
