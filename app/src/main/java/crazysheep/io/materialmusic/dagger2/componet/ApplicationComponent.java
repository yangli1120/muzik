package crazysheep.io.materialmusic.dagger2.componet;

import javax.inject.Singleton;

import crazysheep.io.materialmusic.application.BaseApplication;
import crazysheep.io.materialmusic.dagger2.module.ApplicationModule;
import dagger.Component;

/**
 * component for {@link crazysheep.io.materialmusic.application.BaseApplication}
 *
 * Created by crazysheep on 16/4/19.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication application);

    BaseApplication getContext();
}
