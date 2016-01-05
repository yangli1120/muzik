package crazysheep.io.materialmusic.utils;

import android.os.Build;

/**
 * check api
 *
 * Created by crazysheep on 16/1/4.
 */
public class ApiHelper {

    /**
     * check current api
     * */
    public static boolean check(int apiInt) {
        return Build.VERSION.SDK_INT >= apiInt;
    }

    /**
     * check current api later than lollipop
     * */
    public static boolean checkL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * check current api later than marshmallow
     * */
    public static boolean checkM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * check current api later than kitkat
     * */
    public static boolean checkK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
