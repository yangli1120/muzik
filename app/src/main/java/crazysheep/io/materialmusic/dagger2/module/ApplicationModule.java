package crazysheep.io.materialmusic.dagger2.module;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import crazysheep.io.materialmusic.application.BaseApplication;
import dagger.Module;
import dagger.Provides;

/**
 * module for {@link crazysheep.io.materialmusic.application.BaseApplication}
 *
 * Created by crazysheep on 16/4/19.
 */
@Module
public class ApplicationModule {

    private BaseApplication mContext;

    public ApplicationModule(@NonNull BaseApplication application) {
        mContext = application;
    }

    @Provides
    @Singleton
    public BaseApplication provideApplication() {
        return mContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

}
