package crazysheep.io.materialmusic.application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;

import crazysheep.io.materialmusic.dagger2.componet.ApplicationComponent;
import crazysheep.io.materialmusic.dagger2.componet.DaggerApplicationComponent;
import crazysheep.io.materialmusic.dagger2.module.ApplicationModule;

/**
 * base application
 *
 * Created by crazysheep on 15/12/18.
 */
public class BaseApplication extends Application {

    public static final String TAG = "MaterialMusic";

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mComponent.inject(this);

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

    public ApplicationComponent getComponent() {
        return mComponent;
    }

}
