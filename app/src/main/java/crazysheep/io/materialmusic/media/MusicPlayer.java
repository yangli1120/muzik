package crazysheep.io.materialmusic.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * play music
 *
 * Created by crazysheep on 15/12/21.
 */
public class MusicPlayer {

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

    private String mUrl;

    private MediaPlayer mPlayer;

    private MediaPlayer getPlayer() {
        // init player
        if(mPlayer == null)
            mPlayer = new MediaPlayer();

        return mPlayer;
    }

    public void play(@NonNull String url) {
        if(isPlaying() || isPause() || isPreparing())
            stop();

        mCurState = STATE_PREPARING;
        mUrl = url;

        try {
            getPlayer().setDataSource(mContext, Uri.parse(url));
            getPlayer().prepareAsync();
            getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    start();
                }
            });
            getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
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
    }

    public void resume() {
        mCurState = STATE_PLAY;

        getPlayer().start();
    }

    public void stop() {
        mCurState = STATE_STOP;

        getPlayer().stop();
    }

    private void start() {
        mCurState = STATE_PLAY;

        getPlayer().start();
    }

    public void release() {
        mUrl = null;

        if(isPlaying() || isPause())
            stop();
        mPlayer.release();
        mPlayer = null;

        mCurState = STATE_IDLE;

        mContext = null;
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

    public boolean isCurrentUrl(@NonNull String url) {
        return mUrl.equalsIgnoreCase(url);
    }

}
