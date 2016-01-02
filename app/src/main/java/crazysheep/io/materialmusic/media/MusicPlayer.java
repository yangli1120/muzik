package crazysheep.io.materialmusic.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;

import crazysheep.io.materialmusic.bean.ISong;
import de.greenrobot.event.EventBus;

/**
 * playList music
 *
 * Created by crazysheep on 15/12/21.
 */
public class MusicPlayer {

    /////////////////////// event bus /////////////////////////////////

    /**
     * send by {@link MusicPlayer} when current song play complete, not stop by user
     * */
    public static class EventMusicPlayDone {
        public ISong song;

        public EventMusicPlayDone(@NonNull ISong song) {
            this.song = song;
        }
    }

    /**
     * send by {@link MusicPlayer} when music is pause
     */
    public static class EventMusicPause {
        public ISong song;

        public EventMusicPause(@NonNull ISong song) {
            this.song = song;
        }
    }

    /**
     * send by {@link MusicPlayer} when music is resume
     * */
    public static class EventMusicResume {
        public ISong song;

        public EventMusicResume(@NonNull ISong song) {
            this.song = song;
        }
    }

    /**
     * send by {@link MusicPlayer} when music is stop
     * */
    public static class EventMusicStop {
        public ISong song;

        public EventMusicStop(@NonNull ISong song) {
            this.song = song;
        }
    }

    ///////////////////////////////////////////////////////////////////

    private static MusicPlayer mMusicPlayer;
    private Context mContext;

    private MusicPlayer(Context context) {
        mContext = context;
    }

    public static MusicPlayer getInstance(Context context) {
        if(mMusicPlayer == null)
            mMusicPlayer = new MusicPlayer(context);

        return mMusicPlayer;
    }

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAY = 2;
    private static final int STATE_PAUSE = 3;
    private static final int STATE_STOP = 4;
    private int mCurState = STATE_IDLE;

    private ISong mSong;

    private MediaPlayer mPlayer;
    private boolean isVolumeOn = true;

    private MediaPlayer getPlayer() {
        // init player
        if(mPlayer == null)
            mPlayer = new MediaPlayer();

        return mPlayer;
    }

    public void play(@NonNull ISong song) {
        if(isPlaying() || isPause() || isPreparing())
            getPlayer().reset();

        mCurState = STATE_PREPARING;
        mSong = song;

        try {
            getPlayer().setDataSource(mContext, Uri.parse(mSong.getUrl()));
            getPlayer().prepareAsync();
            getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    resume();
                }
            });
            getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();

                    // notify current song playList done
                    EventBus.getDefault().post(new EventMusicPlayDone(mSong));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

            release();
        }
    }

    public void pause() {
        mCurState = STATE_PAUSE;

        getPlayer().pause();

        EventBus.getDefault().post(new EventMusicPause(mSong));
    }

    public void resume() {
        mCurState = STATE_PLAY;

        getPlayer().start();

        EventBus.getDefault().post(new EventMusicResume(mSong));
    }

    public void stop() {
        mCurState = STATE_STOP;

        getPlayer().stop();
        getPlayer().reset();

        EventBus.getDefault().post(new EventMusicStop(mSong));
    }

    public void release() {
        mSong = null;

        if(isPlaying() || isPause())
            stop();
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        mCurState = STATE_IDLE;

        mContext = null;
        mMusicPlayer = null;
    }

    public void toggleVolume(boolean on) {
        isVolumeOn = on;
        int volume = on ? 1 : 0;
        getPlayer().setVolume(volume, volume);
    }

    public boolean isVolumeOn() {
        return isVolumeOn;
    }

    public void seekTo(int position) {
        if(isPlaying() || isPause())
            getPlayer().seekTo(position * 1000);
    }

    public boolean isPreparing() {
        return mCurState == STATE_PREPARING;
    }

    public boolean isPlaying() {
        return mCurState == STATE_PLAY;
    }

    public boolean isPause() {
        return mCurState == STATE_PAUSE;
    }

    public boolean isStop() {
        return mCurState == STATE_STOP;
    }

    public boolean isIdle() {
        return mCurState == STATE_IDLE;
    }

    public boolean isCurrentUrl(@NonNull String url) {
        return mSong.getUrl().equalsIgnoreCase(url);
    }

    public int getProgress() {
        if(isPause() || isPlaying())
            return getPlayer().getCurrentPosition();

        return 0;
    }

}
