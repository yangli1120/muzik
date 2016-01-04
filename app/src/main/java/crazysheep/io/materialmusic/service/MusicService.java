package crazysheep.io.materialmusic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * music service
 *
 * Created by crazysheep on 15/12/30.
 */
public class MusicService extends BaseMusicService<LocalSongDto> {

    /////////////////// broadcast from notification //////////////////

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MusicConstants.ACTION_PREVIOUS: {
                    calculateAndPlayPreviousSong();
                }break;

                case MusicConstants.ACTION_NEXT: {
                    calculateAndPlayNextSong(true);
                }break;

                case MusicConstants.ACTION_PLAY: {
                    resume();
                }break;

                case MusicConstants.ACTION_PAUSE: {
                    pause();
                }break;

                case MusicConstants.ACTION_STOP: {
                    stopAndRelease();
                    stopForeground(true);
                }break;
            }
        }
    };

    ////////////////////////////////////////////////////////////////

    public class MusicBinder extends BaseBinder<MusicService> {
        @Override
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private MusicBinder mBinder = new MusicBinder();

    private List<LocalSongDto> mAllSongs; // keep original songs
    private LinkedList<LocalSongDto> mPlaylist; // current playlist

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicConstants.ACTION_NEXT);
        filter.addAction(MusicConstants.ACTION_STOP);
        filter.addAction(MusicConstants.ACTION_PREVIOUS);
        filter.addAction(MusicConstants.ACTION_PLAY);
        filter.addAction(MusicConstants.ACTION_PAUSE);
        registerReceiver(mReceiver, filter);

        // init default playlist - all songs on external storage by default
        RxDB.getAllSongs(getContentResolver(), new RxDB.OnQueryListener<LocalSongDto>() {
            @Override
            public void onResult(List<LocalSongDto> results) {
                mAllSongs = results;
                mCurPlayType = MusicConstants.PLAY_LOOP_ALL;
                makePlaylist();

                // pointer point at first song of playlist
                mCurPlayPos = 0;
            }

            @Override
            public void onError(String err) {
                // I don't want a error!
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);
    }

    ////////////////////////// music operations //////////////////////

    private int mCurPlayType = MusicConstants.PLAY_ORDER;

    private final static int INVALID_POSITION = -1;
    private int mCurPlayPos = INVALID_POSITION; // range is 0 ~ mPlaylist.size() - 1, or invalid

    public void playList(@NonNull List<LocalSongDto> songs, int type) {
        mAllSongs = songs;
        mCurPlayType = type;

        makePlaylist();
        calculateAndPlayNextSong(false);
    }

    private void makePlaylist() {
        LocalSongDto curSong = null;
        if(mCurPlayPos >= 0)
            curSong = mPlaylist.get(mCurPlayPos);

        if(mCurPlayType == MusicConstants.PLAY_SHUFFLE) {
            mPlaylist = new LinkedList<>(mAllSongs);
            Collections.shuffle(mPlaylist);
        } else if(mCurPlayType == MusicConstants.PLAY_LOOP_ONE) {
            mPlaylist = new LinkedList<>(mAllSongs.subList(0, 1));
        } else {
            mPlaylist = new LinkedList<>(mAllSongs);
        }

        // if current is playing a song, let current playing song be the first one in new playlist
        if(!Utils.checkNull(curSong)) {
            mPlaylist.remove(curSong);
            mPlaylist.addFirst(curSong);
            mCurPlayPos = 0;
        }
    }

    public void playItem(int position) {
        mCurPlayPos = position;
        play(mPlaylist.get(position));
    }

    public void playList(@NonNull List<LocalSongDto> songs) {
        playList(songs, MusicConstants.PLAY_LOOP_ALL);
    }

    public void shuffle() {
        mCurPlayType = MusicConstants.PLAY_SHUFFLE;
        makePlaylist();
    }

    public void loopOne() {
        mCurPlayType = MusicConstants.PLAY_LOOP_ONE;
        makePlaylist();
    }

    public void loopAll() {
        mCurPlayType = MusicConstants.PLAY_LOOP_ALL;
        makePlaylist();
    }

    public void order() {
        mCurPlayType = MusicConstants.PLAY_ORDER;
        makePlaylist();
    }

    public void next() {
        calculateAndPlayNextSong(true);
    }

    public void previous() {
        calculateAndPlayPreviousSong();
    }

    private void calculateAndPlayNextSong(boolean isUserAction) {
        if(!isUserAction && mCurPlayType == MusicConstants.PLAY_ORDER
                && mCurPlayPos == mPlaylist.size() - 1) {
            // order mode, stop music, do nothing
        } else {
            mCurPlayPos = ++mCurPlayPos % mPlaylist.size();
            play(mPlaylist.get(mCurPlayPos));
        }
    }

    // switch previous song must be user action
    private void calculateAndPlayPreviousSong() {
        mCurPlayPos = --mCurPlayPos < 0 ? mCurPlayPos + mPlaylist.size() : mCurPlayPos;
        play(mPlaylist.get(mCurPlayPos));
    }

    @Override
    protected void playDone() {
        calculateAndPlayNextSong(false);
    }

    /**
     * get current song of MusicService
     * */
    public ISong getCurrentSong() {
        return Utils.checkNull(mPlaylist) || mCurPlayPos == INVALID_POSITION
                ? null :mPlaylist.get(mCurPlayPos);
    }

    /**
     * start play, if current music is not pausing, start play mCurPlayPos song
     * */
    public void playOrResume() {
        if(isPause())
            resume();
        else
            playItem(mCurPlayPos);
    }

}
