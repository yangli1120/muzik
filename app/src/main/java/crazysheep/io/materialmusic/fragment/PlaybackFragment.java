package crazysheep.io.materialmusic.fragment;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.animator.FabAlphaDirector;
import crazysheep.io.materialmusic.animator.PlaybackDirector;
import crazysheep.io.materialmusic.bean.SongDto;
import crazysheep.io.materialmusic.service.FMService;
import crazysheep.io.materialmusic.widget.SimpleAnimatorListener;
import de.greenrobot.event.EventBus;

/**
 * playback fragment
 *
 * Created by crazysheep on 15/12/20.
 */
public class PlaybackFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.song_next_iv) ImageView mSongNextIv;
    @Bind(R.id.song_artist_tv) TextView mArtistTv;
    @Bind(R.id.song_name_tv) TextView mSongNameTv;
    @Bind(R.id.song_cover_iv) ImageView mSongCoverIv;
    @Bind(R.id.play_fab) FloatingActionButton mPlayFab;
    @Bind(R.id.song_pause_iv) ImageView mSongPauseIv;
    @Bind(R.id.current_song_info_tv) TextView mCurrentSongInfoTv;
    @Bind(R.id.current_song_info_ll) View mCurrentSongInfoLl;
    @Bind(R.id.song_control_ll) View mControlLl;
    @Bind(R.id.album_cover_iv) ImageView mNextSongCoverIv;
    @Bind(R.id.next_song_artist_tv) TextView mNextSongArtistTv;
    @Bind(R.id.next_song_name_tv) TextView mNextSongNameTv;
    @Bind(R.id.next_song_info_ll) View mNextSongInfoLl;

    private int mInfoLayoutTranslateDis = 0;

    private PlaybackDirector.Builder mFabDirector;
    private FabAlphaDirector.Builder mFabAlphaDirector;

    private boolean bindService = false;
    private FMService mFMService;

    private List<SongDto> mSongList;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFMService = ((FMService.FmBinder) service).getService();
            if(mFMService.isStartup())
                mFMService.requestSongs();

            bindService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindService = false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playback, container, false);
        ButterKnife.bind(this, contentView);

        mNextSongInfoLl.setOnClickListener(this);
        mSongNextIv.setOnClickListener(this);
        mNextSongInfoLl.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mNextSongInfoLl.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        mInfoLayoutTranslateDis = mNextSongInfoLl.getHeight();
                    }
                });
        mControlLl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mControlLl.getViewTreeObserver().removeOnPreDrawListener(this);
                mControlLl.setVisibility(View.GONE);

                return true;
            }
        });
        mFabDirector = new PlaybackDirector.Builder(mPlayFab, mSongPauseIv, mControlLl);
        mFabAlphaDirector = new FabAlphaDirector.Builder(mPlayFab, R.drawable.ic_play_arrow,
                R.drawable.ic_pause);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

        getActivity().bindService(new Intent(getActivity(), FMService.class),
                mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

        if(!mFMService.isPlaying()) {
            mFMService.release();
        } else {
            mFMService.notifyPlaying();
        }
        if(bindService) {
            getActivity().unbindService(mConnection);
            bindService = false;
        }
    }

    public void onEventMainThread(@NonNull FMService.EventCurrentSong event) {
        updateSongUI(event.mSongs);
    }

    private void updateSongUI(@NonNull List<SongDto> songs) {
        // update current song ui
        SongDto currentSong = songs.get(0);
        Picasso.with(getActivity())
                .load(currentSong.picture)
                .noPlaceholder()
                .noFade()
                .into(mSongCoverIv);
        mArtistTv.setText(currentSong.artist);
        mSongNameTv.setText(currentSong.title);
        mCurrentSongInfoTv.setText(getString(R.string.tv_next_song_info, currentSong.artist,
                currentSong.title));

        // update next song ui
        SongDto nextSong = songs.get(1);
        Picasso.with(getActivity())
                .load(nextSong.picture)
                .noPlaceholder()
                .into(mNextSongCoverIv);
        mNextSongArtistTv.setText(nextSong.artist);
        mNextSongNameTv.setText(nextSong.title);

        // save songs
        mSongList = songs;
    }

    @OnClick(R.id.play_fab)
    protected void fabOnClick() {
        showCurrentSongInfoLayout(true);

        mFMService.play(mSongList.get(0));

        mFabAlphaDirector.animate();
        mFabDirector.expand();
    }

    @OnClick(R.id.song_pause_iv)
    protected void pauseOnClick() {
        showCurrentSongInfoLayout(false);

        mFMService.pause();

        mFabDirector
                .setListener(new PlaybackDirector.SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd() {
                        mFabDirector.setListener(null);
                        mFabAlphaDirector.reverse();
                    }
                })
                .close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_next_iv:
            case R.id.next_song_info_ll: {
                mFMService.next();
            }break;
        }
    }

    private void showCurrentSongInfoLayout(boolean show) {
        if(show) {
            mNextSongInfoLl.setVisibility(View.GONE);
            mCurrentSongInfoLl.setVisibility(View.VISIBLE);
            mCurrentSongInfoLl.setTranslationY(-mInfoLayoutTranslateDis);
            mCurrentSongInfoLl.animate()
                    .translationYBy(mInfoLayoutTranslateDis)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(150)
                    .setListener(null)
                    .start();
        } else {
            mCurrentSongInfoLl.animate()
                    .translationYBy(-mInfoLayoutTranslateDis)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(150)
                    .setListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mNextSongInfoLl.setVisibility(View.VISIBLE);
                            mCurrentSongInfoLl.setVisibility(View.GONE);
                        }
                    })
                    .start();
        }
    }

}
