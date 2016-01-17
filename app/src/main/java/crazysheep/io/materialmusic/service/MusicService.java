package crazysheep.io.materialmusic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.PlaylistModel;
import crazysheep.io.materialmusic.bean.PlaylistSongModel;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.prefs.PlaylistPrefs;
import crazysheep.io.materialmusic.utils.L;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * music service
 *
 * Created by crazysheep on 15/12/30.
 */
public class MusicService extends BaseMusicService<ISong> {

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

    private PlaylistModel mPlaylistModel;
    private List<ISong> mAllSongs; // keep original songs
    private LinkedList<ISong> mPlaylist; // current playlist

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);

        // save last playlist model for next startup app
        PlaylistPrefs prefs = new PlaylistPrefs(this);
        prefs.setLastPlaylist(mPlaylistModel.playlist_name);
        prefs.setLastPlayMode(mCurPlayType);
        SongModel songModel = (SongModel) mPlaylist.get(mCurPlayPos);
        prefs.setLastPlaySong(songModel.songId);
    }

    ////////////////////////// music operations //////////////////////

    private int mCurPlayType = MusicConstants.PLAY_ORDER;

    private final static int INVALID_POSITION = -1;
    private int mCurPlayPos = INVALID_POSITION; // range is 0 ~ mPlaylist.size() - 1, or invalid

    /**
     * @param model The playlist model
     * @param type The play type, see{@link MusicConstants}
     * @param songId The song id of which should be played in current playlist
     * @param autoPlay Play immediately or just ready to play
     * */
    public void playList(@NonNull PlaylistModel model, int type, final long songId,
                         final boolean autoPlay) {
        mPlaylistModel = model;

        mCurPlayType = type;
        RxDB.query(PlaylistSongModel.class,
                PlaylistSongModel.PLAYLIST + "=?", String.valueOf(model.getId()), null,
                new RxDB.OnQueryListener<PlaylistSongModel>() {
                    @Override
                    public void onResult(List<PlaylistSongModel> results) {
                        ArrayList<ISong> songs = new ArrayList<>();
                        for(PlaylistSongModel songModel : results)
                            songs.add(songModel.song);
                        mAllSongs = songs;

                        makePlaylist();
                        // calculate current song
                        mCurPlayPos = 0;
                        for(int index = 0; index < mPlaylist.size(); index++)
                            if(((SongModel)mPlaylist.get(index)).songId == songId)
                                mCurPlayPos = index;
                        if(autoPlay)
                            playOrResume();
                    }

                    @Override
                    public void onError(String err) {
                        L.d(err);
                    }
                });
    }

    private void makePlaylist() {
        ISong curSong = null;
        if(mCurPlayPos >= 0)
            curSong = mPlaylist.get(mCurPlayPos);

        if(mCurPlayType == MusicConstants.PLAY_SHUFFLE) {
            mPlaylist = new LinkedList<>(mAllSongs);
            Collections.shuffle(mPlaylist);
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

    /**
     * find target song's index of playlist, not all song list
     * */
    public int findIndexOfPlaylist(@NonNull String songUrl) {
        for(int i = 0; i < mPlaylist.size(); i++)
            if(songUrl.equals(mPlaylist.get(i).getUrl()))
                return i;

        return -1;
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

    public List<ISong> getAllSongs() {
        return mAllSongs;
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

    public boolean isLoopAll() {
        return mCurPlayType == MusicConstants.PLAY_LOOP_ALL;
    }

    public boolean isLoopOne() {
        return mCurPlayType == MusicConstants.PLAY_LOOP_ONE;
    }

    public boolean isShuffle() {
        return mCurPlayType == MusicConstants.PLAY_SHUFFLE;
    }

    public void loopOne() {
        mCurPlayType = MusicConstants.PLAY_LOOP_ONE;
        makePlaylist();
    }

    public void loopAll() {
        mCurPlayType = MusicConstants.PLAY_LOOP_ALL;
        makePlaylist();
    }

    public void shuffle() {
        mCurPlayType = MusicConstants.PLAY_SHUFFLE;
        makePlaylist();
    }

}
