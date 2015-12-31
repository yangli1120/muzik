package crazysheep.io.materialmusic.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * music service
 *
 * Created by crazysheep on 15/12/30.
 */
public class MusicService extends BaseMusicService<LocalSongDto> {

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

    ////////////////////////// music operations //////////////////////

    private static final int PLAY_ORDER = 0;
    private static final int PLAY_LOOP_ONE = 1;
    private static final int PLAY_LOOP_ALL = 2;
    private static final int PLAY_SHUFFLE = 3;

    private int mCurPlayType = PLAY_ORDER;

    private final static int INVALID_POSITION = -1;
    private int mCurPlayPos = INVALID_POSITION; // range is 0 ~ mPlaylist.size() - 1, or invalid

    private void play(@NonNull List<LocalSongDto> songs, int type) {
        mAllSongs = songs;
        mCurPlayType = type;

        makePlaylist();
        calculateAndPlayNextSong();
    }

    private void makePlaylist() {
        LocalSongDto curSong = null;
        if(mCurPlayPos >= 0)
            curSong = mPlaylist.get(mCurPlayPos);

        if(mCurPlayType == PLAY_SHUFFLE) {
            mPlaylist = new LinkedList<>(mAllSongs);
            Collections.shuffle(mPlaylist);
        } else if(mCurPlayType == PLAY_LOOP_ONE) {
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

    public void play(@NonNull List<LocalSongDto> songs) {
        play(songs, PLAY_ORDER);
    }

    public void shuffle() {
        mCurPlayType = PLAY_SHUFFLE;
        makePlaylist();
    }

    public void loopOne() {
        mCurPlayType = PLAY_LOOP_ONE;
        makePlaylist();
    }

    public void loopAll() {
        mCurPlayType = PLAY_LOOP_ALL;
        makePlaylist();
    }

    public void order() {
        mCurPlayType = PLAY_ORDER;
        makePlaylist();
    }

    private void calculateAndPlayNextSong() {
        if(mCurPlayType == PLAY_ORDER && mCurPlayPos == mPlaylist.size() - 1) {
            // order mode, stop music, do nothing
        } else {
            mCurPlayPos = ++mCurPlayPos % mPlaylist.size();
            play(mPlaylist.get(mCurPlayPos));
        }
    }

    @Override
    protected void playDone() {
        calculateAndPlayNextSong();
    }
}
