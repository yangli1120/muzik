package crazysheep.io.materialmusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import crazysheep.io.materialmusic.R;

/**
 * activity utils
 *
 * Created by crazysheep on 15/12/14.
 */
public class ActivityUtils {

    /**
     * start target activity
     * */
    public static void start(@NonNull Context context, @NonNull Class<? extends Activity> clazz) {
        context.startActivity(new Intent(context, clazz));
        if(context instanceof Activity)
            ((Activity)context).overridePendingTransition(R.anim.slide_left_in, 0);
    }

    /**
     * start activity
     * */
    public static void start(@NonNull Context context, @NonNull Intent intent) {
        context.startActivity(intent);
        if(context instanceof Activity)
            ((Activity)context).overridePendingTransition(R.anim.slide_left_in, 0);
    }

    /**
     * create a target intent
     * */
    public static Intent prepare(@NonNull Context context,
                                 @NonNull Class<? extends Activity> clazz) {
        return new Intent(context, clazz);
    }

    /**
     * start for result
     * */
    public static void startResult(@NonNull Activity activity, int requestCode,
                                   @NonNull Intent intent) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_left_in, 0);
    }

}
