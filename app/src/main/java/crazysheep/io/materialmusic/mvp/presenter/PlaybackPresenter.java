package crazysheep.io.materialmusic.mvp.presenter;

import android.support.annotation.NonNull;

import crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment;
import crazysheep.io.materialmusic.mvp.contract.PlaybackContract;

/**
 * P for {@link crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment}
 *
 * Created by crazysheep on 16/4/19.
 */
public class PlaybackPresenter implements PlaybackContract.IPlaybackPresenter {

    public PlaybackPresenter(@NonNull PlaybackFragment fragment,
                             @NonNull PlaybackContract.IPlaybackView view) {
    }

    @Override
    public void bindService() {

    }

    @Override
    public void unbindService() {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void previous() {

    }

    @Override
    public void next() {

    }

    @Override
    public void start() {

    }
}
