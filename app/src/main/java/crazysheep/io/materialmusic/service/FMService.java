package crazysheep.io.materialmusic.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;

import crazysheep.io.materialmusic.bean.doubanfm.PlaylistDto;
import crazysheep.io.materialmusic.bean.doubanfm.SongDto;
import crazysheep.io.materialmusic.net.DoubanService;
import crazysheep.io.materialmusic.net.NetClient;
import crazysheep.io.materialmusic.utils.Utils;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * fm background service, implement FM background logic
 *
 * Created by crazysheep on 15/12/21.
 */
public class FMService extends BaseMusicService<SongDto> {

    ////////////////// event bus /////////////////////////

    public static class EventCurrentAndNextSong {
        public SongDto currentSong;
        public SongDto nextSong;

        public EventCurrentAndNextSong(@NonNull SongDto cur, @NonNull SongDto next) {
            currentSong = cur;
            nextSong = next;
        }
    }

    /////////////////////////////////////////////////////

    private Retrofit mRetorfit;
    private DoubanService mDoubanService;
    private Call<PlaylistDto> mPlaylistCall;

    protected LinkedList<SongDto> mSongs = new LinkedList<>();

    private FmBinder mBinder = new FmBinder();

    public class FmBinder extends BaseBinder<FMService> {
        public FMService getService() {
            return FMService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRetorfit = NetClient.retrofit();
        mDoubanService = mRetorfit.create(DoubanService.class);

        // fetch songs
        checkIfNeedFetchSong();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void fetchSong() {
        mPlaylistCall = mDoubanService.fetchPlaylist(DoubanService.DEFAULT_CHANNEL_ID);
        mPlaylistCall.enqueue(new Callback<PlaylistDto>() {
            @Override
            public void onResponse(Response<PlaylistDto> response, Retrofit retrofit) {
                if (!Utils.checkNull(response.body())
                        && !Utils.checkNull(response.body().song)
                        && !Utils.checkNull(response.body().song.get(0)))
                    mSongs.add(response.body().song.get(0));

                if (mSongs.size() >= 2)
                    broadCurrentAndNextSong();
                checkIfNeedFetchSong();
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void checkIfNeedFetchSong() {
        if(mSongs.size() < 2)
            fetchSong();
    }

    // ui request current song, then service will broadcast them
    public void requestSongs() {
        broadcastCurrentSong();
        if(!Utils.checkNull(mSongs) && mSongs.size() >= 2)
            broadCurrentAndNextSong();
        else
            fetchSong();
    }

    private void broadCurrentAndNextSong() {
        EventBus.getDefault().post(new EventCurrentAndNextSong(mSongs.get(0), mSongs.get(1)));
    }

    public void next() {
        mSongs.removeFirst();
        play(mSongs.get(0), true);
        if(mSongs.size() >= 2)
            broadCurrentAndNextSong();

        checkIfNeedFetchSong();
    }
}
