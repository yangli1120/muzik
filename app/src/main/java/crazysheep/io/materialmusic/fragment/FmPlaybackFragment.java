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

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.animator.FabAlphaDirector;
import crazysheep.io.materialmusic.animator.PlaybackDirector;
import crazysheep.io.materialmusic.bean.doubanfm.SongDto;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.FMService;
import crazysheep.io.materialmusic.utils.StringUtils;
import crazysheep.io.materialmusic.widget.SimpleAnimatorListener;
import de.greenrobot.event.EventBus;

/**
 * playback fragment
 *
 * Created by crazysheep on 15/12/20.
 */
public class FmPlaybackFragment extends BaseFragment implements View.OnClickListener {

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
    @Bind(R.id.music_sb) DiscreteSeekBar mMusicSb;
    @Bind(R.id.song_volume_on_iv) ImageView mVolumeOnIv;

    private int mInfoLayoutTranslateDis = 0;

    private PlaybackDirector.Builder mFabDirector;
    private FabAlphaDirector.Builder mFabAlphaDirector;

    private SongDto mCurSong;
    private SongDto mNextSong;

    private boolean bindService = false;
    private FMService mFMService;

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
        View contentView = inflater.inflate(R.layout.fragment_fm_playback, container, false);
        ButterKnife.bind(this, contentView);

        initUI();

        return contentView;
    }

    private void initUI() {
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

        // pretty seek bar
        mMusicSb.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                if (mFMService.isStartup())
                    mFMService.seekTo(seekBar.getProgress());
            }
        });
        mMusicSb.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value;
            }

            @Override
            public String transformToString(int value) {
                return StringUtils.formatDuration(value);
            }

            @Override
            public boolean useStringTransform() {
                return true;
            }
        });
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
            mFMService.stop();
        } else {
            mFMService.notifyPlaying();
        }
        if(bindService) {
            getActivity().unbindService(mConnection);
            bindService = false;
        }
    }

    // event bus message receiver
    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull BaseMusicService.EventCurrentSong event) {
        if(mCurSong.sid != ((SongDto)event.currentSong).sid) {
            mCurSong = (SongDto)event.currentSong;
            updateCurrentSongUI();
            mMusicSb.setMax(mCurSong.length);
        }

        // same song, just update progress
        mMusicSb.setProgress(event.progress);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull FMService.EventCurrentAndNextSong event) {
        mCurSong = event.currentSong;
        mNextSong = event.nextSong;

        updateCurrentSongUI();
        updateNextSongUI();
    }

    private void updateCurrentSongUI() {
        Picasso.with(getActivity())
                .load(mCurSong.picture)
                .noPlaceholder()
                .noFade()
                .into(mSongCoverIv);
        mArtistTv.setText(mCurSong.artist);
        mSongNameTv.setText(mCurSong.title);
        mCurrentSongInfoTv.setText(getString(R.string.tv_next_song_info, mCurSong.artist,
                mCurSong.title));
    }

    private void updateNextSongUI() {
        Picasso.with(getActivity())
                .load(mNextSong.picture)
                .noPlaceholder()
                .into(mNextSongCoverIv);
        mNextSongArtistTv.setText(mNextSong.artist);
        mNextSongNameTv.setText(mNextSong.title);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.play_fab)
    protected void fabOnClick() {
        showCurrentSongInfoLayout(true);

        mFMService.play(mCurSong);

        mFabAlphaDirector.animate();
        mFabDirector.expand();
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    @OnClick(R.id.song_volume_on_iv)
    protected void toggleVolume() {
        boolean volumeOn = !mFMService.isVolumeOn();
        mVolumeOnIv.setImageResource(volumeOn ? R.drawable.ic_volume_up : R.drawable.ic_volume_off);
        mFMService.toggleVolume(volumeOn);
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
