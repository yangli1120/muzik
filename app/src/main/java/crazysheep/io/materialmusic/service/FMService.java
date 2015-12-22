package crazysheep.io.materialmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import crazysheep.io.materialmusic.bean.AlbumDto;
import crazysheep.io.materialmusic.bean.PlaylistDto;
import crazysheep.io.materialmusic.bean.SongDto;
import crazysheep.io.materialmusic.media.MusicPlayer;
import crazysheep.io.materialmusic.net.DoubanService;
import crazysheep.io.materialmusic.net.NetClient;
import crazysheep.io.materialmusic.utils.Utils;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * fm background service
 *
 * Created by crazysheep on 15/12/21.
 */
public class FMService extends Service {

    /////////////////// event bus /////////////////////////

    public static class EventCurrentSong {

        public SongDto mSongDto;

        public EventCurrentSong(SongDto songDto) {
            mSongDto = songDto;
        }
    }

    ///////////////////////////////////////////////////////

    private boolean isInit = false;

    private Retrofit mRetorfit;
    private DoubanService mDoubanService;
    private Call<PlaylistDto> mPlaylistCall;
    private Call<AlbumDto> mAlbumCall;

    private FmBinder mBinder = new FmBinder();

    public class FmBinder extends Binder {
        public FMService getService() {
            return FMService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRetorfit = NetClient.retrofit();
        mDoubanService = mRetorfit.create(DoubanService.class);
        mPlaylistCall = mDoubanService.fetchPlaylist(DoubanService.DEFAULT_CHANNEL_ID);

        isInit = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isInit = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public boolean isStartup() {
        return isInit;
    }

    //////////////// net action //////////////////////////////

    public void fetchSong() {
        mPlaylistCall.enqueue(new Callback<PlaylistDto>() {
            @Override
            public void onResponse(Response<PlaylistDto> response, Retrofit retrofit) {
                if(!Utils.checkNull(response.body())
                        && !Utils.checkNull(response.body().song))
                    EventBus.getDefault().post(new EventCurrentSong(response.body().song.get(0)));
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    //////////////// music action ////////////////////////////

    public void play(@NonNull SongDto song) {
        play(song, false);
    }

    /**
     * if force is true, init to play even the song is same one.
     * */
    public void play(@NonNull SongDto song, boolean force) {
        if(!force && MusicPlayer.getInstance(this).isPause()
                && MusicPlayer.getInstance(this).isCurrentUrl(song.url))
            MusicPlayer.getInstance(this).resume();
        else
            MusicPlayer.getInstance(this).play(song.url);
    }

    public void pause() {
        MusicPlayer.getInstance(this).pause();
    }

    public void stop() {
        MusicPlayer.getInstance(this).stop();
    }

    public void release() {
        MusicPlayer.getInstance(this).release();
    }

    public boolean isPlaying() {
        return MusicPlayer.getInstance(this).isPlaying();
    }

    public void notifyPlaying() {
        // TODO show ongoing notification to avoid service be killed
    }

}