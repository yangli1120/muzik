package crazysheep.io.materialmusic.utils;

import com.orhanobut.logger.Logger;

import crazysheep.io.materialmusic.BuildConfig;

/**
 * log utils
 *
 * Created by crazysheep on 15/12/18.
 */
public class L {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String msg) {
        if(DEBUG)
            Logger.d(msg);
    }
}
