package crazysheep.io.materialmusic.fragment.localmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.media.MusicPlayer;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.PlayOrPauseImageButton;

/**
 * bottom mini player fragment, communicate with {@link crazysheep.io.materialmusic.service.MusicService}
 *
 * Created by crazysheep on 16/1/2.
 */
public class MiniPlayerFragment extends BaseFragment {

    @Bind(R.id.song_cover_iv) ImageView mSongCoverIv;
    @Bind(R.id.song_play_or_pause_ib) PlayOrPauseImageButton mPlayOrPauseBtn;
    @Bind(R.id.song_name_tv) TextView mSongNameTv;
    @Bind(R.id.song_artist_tv) TextView mSongArtistTv;

    private ISong mCurSong;

    private boolean isServiceBind = false;
    private MusicService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBind = true;
            mService = ((MusicService.MusicBinder)service).getService();

            mCurSong = mService.getCurrentSong();
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBind = false;
            mService = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mini_player, container, false);
        ButterKnife.bind(this, contentView);

        updateUI();

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

        getActivity().bindService(new Intent(getActivity(), MusicService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isServiceBind)
            mPlayOrPauseBtn.toggle(mService.isPlaying());
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        if(isServiceBind)
            getActivity().unbindService(mConnection);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.song_next_ib)
    protected void clickNextButton() {
        if(isServiceBind)
            mService.next();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.song_play_or_pause_ib)
    protected void clickPlayOrPauseButton() {
        if(isServiceBind) {
            if(mService.isPlaying()) {
                mPlayOrPauseBtn.toggle(false);
                mService.pause();
            } else {
                mPlayOrPauseBtn.toggle(true);
                mService.playOrResume();
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(@NonNull BaseMusicService.EventSongProgress event) {
        if(Utils.checkNull(mCurSong) || !event.song.getCover().equals(mCurSong.getUrl())) {
            mCurSong = event.song;
            updateUI();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(@NonNull MusicPlayer.EventMusicResume event) {
        mPlayOrPauseBtn.toggle(true);
        compareEventSong(event.song);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(@NonNull MusicPlayer.EventMusicPause event) {
        mPlayOrPauseBtn.toggle(false);
        compareEventSong(event.song);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(@NonNull MusicPlayer.EventMusicStop event) {
        mPlayOrPauseBtn.toggle(false);
        compareEventSong(event.song);
    }

    private void compareEventSong(@NonNull ISong song) {
        if(Utils.checkNull(mCurSong) || !mCurSong.getUrl().equals(song.getUrl())) {
            mCurSong = song;
            updateUI();
        }
    }

    private void updateUI() {
        if (!Utils.checkNull(mCurSong)) {
            mSongArtistTv.setText(mCurSong.getArtist());
            mSongNameTv.setText(mCurSong.getName());

            if(!TextUtils.isEmpty(mCurSong.getCover())) {
                Uri uri = mCurSong.isLocal() ? Uri.fromFile(new File(mCurSong.getCover()))
                        : Uri.parse(mCurSong.getCover());
                Picasso.with(getActivity())
                        .load(uri)
                        .error(R.drawable.place_holder)
                        .fit()
                        .into(mSongCoverIv);
            } else {
                mSongCoverIv.setImageResource(R.drawable.place_holder);
            }
        }
    }

}
