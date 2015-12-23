package crazysheep.io.materialmusic.utils;

/**
 * string utils
 *
 * Created by crazysheep on 15/12/23.
 */
public class StringUtils {

    /**
     * format duration as "mm:ss"
     *
     * @param d duration in seconds
     * */
    public static String formatDuration(int d) {
        return String.format("%02d:%02d", d / 60, (d % 60));
    }
}
