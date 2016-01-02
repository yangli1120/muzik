package crazysheep.io.materialmusic.utils;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * Created by crazysheep on 15/12/22.
 */
public class Utils {

    public static boolean checkNull(Object object) {
        return object == null;
    }

    /**
     * change dp to px
     * {@Link http://stackoverflow.com/questions/4605527/converting-pixels-to-dp}
     * */
    public static float dp2px(@NonNull Resources res, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

}
