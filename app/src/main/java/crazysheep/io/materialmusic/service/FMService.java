package crazysheep.io.materialmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

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

        public List<SongDto> mSongs;

        public EventCurrentSong(@NonNull List<SongDto> songs) {
            mSongs = songs;
        }
    }

    public static class EventCurrentProgress {
        public SongDto mCurrentSong;
        /**
         * seconds
         * */
        public int mProgress;

        public EventCurrentProgress(@NonNull SongDto songDto, int progress) {
            mCurrentSong = songDto;
            mProgress = progress;
        }
    }

    ///////////////////////////////////////////////////////

    private static final int MSG_TIK_TOK = 111;
    private static final int DURATION_TIK_TOK = 1000; // 1s

    private boolean isInit = false;

    private Retrofit mRetorfit;
    private DoubanService mDoubanService;
    private Call<PlaylistDto> mPlaylistCall;
    private LinkedList<SongDto> mSongs = new LinkedList<>();

    private static class MyHandler extends Handler {

        private WeakReference<FMService> mReference;

        public MyHandler(FMService service) {
            mReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIK_TOK: {
                    if(!Utils.checkNull(mReference.get())) {
                        FMService service = mReference.get();
                        EventBus.getDefault().post(new EventCurrentProgress(service.mSongs.get(0),
                                service.getProgress() / 1000));
                    }

                    removeMessages(MSG_TIK_TOK);
                    sendEmptyMessageDelayed(MSG_TIK_TOK, DURATION_TIK_TOK);
                }break;
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

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

        isInit = true;

        // fetch songs
        checkIfNeedFetchSong();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isInit = false;
        MusicPlayer.getInstance(this).release();
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
                    broadcastSongs();
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

    private void broadcastSongs() {
        EventBus.getDefault().post(new EventCurrentSong(mSongs));
    }

    //////////////// event action //////////////////////////////

    public void requestSongs() {
        if(!Utils.checkNull(mSongs) && mSongs.size() >= 2)
            broadcastSongs();
        else
            fetchSong();
    }

    public void next() {
        mSongs.removeFirst();
        play(mSongs.get(0), true);

        checkIfNeedFetchSong();
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

        toggleTikTokEvent(true);
    }

    public void pause() {
        MusicPlayer.getInstance(this).pause();

        toggleTikTokEvent(false);
    }

    public void stop() {
        MusicPlayer.getInstance(this).stop();

        toggleTikTokEvent(false);
    }

    public boolean isPlaying() {
        return MusicPlayer.getInstance(this).isPlaying();
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
