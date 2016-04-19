package crazysheep.io.materialmusic.mvp;

import android.support.annotation.NonNull;

/**
 * base interface for V in MVP
 *
 * Created by crazysheep on 16/4/19.
 */
public interface IView {

    <P extends IPresenter> void setPresenter(@NonNull P presenter);
}
