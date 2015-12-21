package crazysheep.io.materialmusic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * date utils
 *
 * Created by crazysheep on 15/11/16.
 */
public class DateUtils {

    /**
     * format target time as pattern format
     * */
    public static String formatTime(long time, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(time));
    }

    /**
     * format target time as pattern "yyyy/MM/dd HH:mm"
     * */
    public static String formatTime(long time) {
        return formatTime(time, "yyyy/MM/dd HH:mm");
    }

}
