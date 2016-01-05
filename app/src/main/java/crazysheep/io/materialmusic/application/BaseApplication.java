package crazysheep.io.materialmusic.application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;

/**
 * base application
 *
 * Created by crazysheep on 15/12/18.
 */
public class BaseApplication extends Application {

    public static final String TAG = "MaterialMusic";

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(TAG)
                .methodCount(5);
        // init activeandroid
        ActiveAndroid.initialize(this);
        // init stetho
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ActiveAndroid.dispose();
    }
}
