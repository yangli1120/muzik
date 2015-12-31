package crazysheep.io.materialmusic.service;

import android.app.Service;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.media.MusicPlayer;
import crazysheep.io.materialmusic.utils.NotifyUtils;
import crazysheep.io.materialmusic.utils.Utils;
import de.greenrobot.event.EventBus;

/**
 * base music service, provide base api for music action, just focus on current playing song
 *
 * <h1>BTW, merry christmas~</h1>
 *
 * Created by crazysheep on 15/12/25.
 */
public abstract class BaseMusicService<SD extends ISong> extends Service {

    /////////////////// event bus /////////////////////////

    public static class EventCurrentSong {
        public ISong currentSong;
        /**
         * seconds
         * */
        public int progress;

        public EventCurrentSong(@NonNull ISong songDto, int progress) {
            currentSong = songDto;
            this.progress = progress;
        }
    }

    ///////////////////////////////////////////////////////

    public abstract class BaseBinder<T extends BaseMusicService> extends Binder {
         public abstract T getService();
    }

    private static final int MSG_TIK_TOK = 111;
    private static final int DURATION_TIK_TOK = 1000; // 1s

    public static final int NOTIFY_MUSIC_ID = 9527;

    private boolean isInit = false;

    private static class MyHandler<T extends Service> extends Handler {

        private WeakReference<T> mReference;

        public MyHandler(T service) {
            mReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIK_TOK: {
                    if(!Utils.checkNull(mReference.get())) {
                        BaseMusicService service = (BaseMusicService )mReference.get();
                        EventBus.getDefault().post(new EventCurrentSong(service.mCurrentSong,
                                service.getProgress() / 1000));
                    }

                    removeMessages(MSG_TIK_TOK);
                    sendEmptyMessageDelayed(MSG_TIK_TOK, DURATION_TIK_TOK);
                }break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected MyHandler mHandler = new MyHandler(this);

    private SD mCurrentSong;

    @Override
    public void onCreate() {
        super.onCreate();

        isInit = true;

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
        EventBus.getDefault().unregister(this);

        isInit = false;
        MusicPlayer.getInstance(this).release();
    }

    public boolean isStartup() {
        return isInit;
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull MusicPlayer.EventSongPlayDone event) {
        playDone();
    }

    /**
     * current song play done
     * */
    protected void playDone() {
    }

    //////////////// event action //////////////////////////////

    protected void broadcastCurrentSong() {
        EventBus.getDefault().post(new EventCurrentSong(mCurrentSong, getProgress() / 1000));
    }

    private void toggleTikTokEvent(boolean play) {
        mHandler.removeMessages(MSG_TIK_TOK);
        // toggle tik tok event
        if(play)
            mHandler.sendEmptyMessage(MSG_TIK_TOK);
    }

    //////////////// music action ////////////////////////////

    public int getProgress() {
        return MusicPlayer.getInstance(this).getProgress();
    }

    public void play(@NonNull SD song) {
        play(song, false);
    }

    /**
     * if force is true, init to play even the song is same one.
     * */
    public void play(@NonNull SD song, boolean force) {
        mCurrentSong = song;

        if(!force && MusicPlayer.getInstance(this).isPause()
                && MusicPlayer.getInstance(this).isCurrentUrl(song.getUrl()))
            MusicPlayer.getInstance(this).resume();
        else
            MusicPlayer.getInstance(this).play(song.getUrl());

        toggleTikTokEvent(true);

        // show foreground notify
        startForeground(NOTIFY_MUSIC_ID,
                NotifyUtils.buildWithSong(this, mCurrentSong, NOTIFY_MUSIC_ID, true));
    }

    public void pause() {
        MusicPlayer.getInstance(this).pause();

        toggleTikTokEvent(false);
    }

    public void resume() {
        MusicPlayer.getInstance(this).resume();

        toggleTikTokEvent(false);
    }

    public void stop() {
        MusicPlayer.getInstance(this).stop();

        toggleTikTokEvent(false);
    }

    public boolean isPlaying() {
        return MusicPlayer.getInstance(this).isPlaying();
    }

    public boolean isPause() {
        return MusicPlayer.getInstance(this).isPause();
    }

    public void notifyPlaying() {
        // TODO show ongoing notification to avoid service be killed
    }

    public boolean isVolumeOn() {
        return MusicPlayer.getInstance(this).isVolumeOn();
    }

    public void toggleVolume(boolean on) {
        MusicPlayer.getInstance(this).toggleVolume(on);
    }

    public void seekTo(int position) {
        MusicPlayer.getInstance(this).seekTo(position);
    }

}
