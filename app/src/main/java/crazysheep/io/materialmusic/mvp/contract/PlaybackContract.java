package crazysheep.io.materialmusic.mvp.contract;

import crazysheep.io.materialmusic.mvp.IPresenter;
import crazysheep.io.materialmusic.mvp.IView;

/**
 * contract for {@link crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment}
 *
 * Created by crazysheep on 16/4/19.
 */
public interface PlaybackContract {

    /**
     * interface V for {@link crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment}
     * */
    interface IPlaybackView extends IView {
        void updatePlayOrPauseBtn();

        void updateUI();
    }

    /**
     * interface P for {@link crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment}
     * */
    interface IPlaybackPresenter extends IPresenter {
        void bindService();
        void unbindService();

        void seekTo(int progress);
        void previous();
        void next();
    }

}
