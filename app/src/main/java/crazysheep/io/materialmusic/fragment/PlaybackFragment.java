package crazysheep.io.materialmusic.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.animator.FabAlphaDirector;
import crazysheep.io.materialmusic.animator.PlaybackDirector;
import crazysheep.io.materialmusic.bean.AlbumDto;
import crazysheep.io.materialmusic.bean.ArtistDto;
import crazysheep.io.materialmusic.bean.SongDto;
import crazysheep.io.materialmusic.net.DoubanService;
import crazysheep.io.materialmusic.service.FMService;
import crazysheep.io.materialmusic.utils.L;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * playback fragment
 *
 * Created by crazysheep on 15/12/20.
 */
public class PlaybackFragment extends BaseFragment {

    @Bind(R.id.song_artist_tv) TextView mArtistTv;
    @Bind(R.id.song_name_tv) TextView mSongNameTv;
    @Bind(R.id.song_cover_iv) ImageView mSongCoverIv;
    @Bind(R.id.play_fab) FloatingActionButton mPlayFab;
    @Bind(R.id.song_pause_iv) ImageView mSongPauseIv;
    @Bind(R.id.song_control_ll) View mControlLl;
    @Bind(R.id.album_cover_iv) ImageView mAlbumCoverIv;
    @Bind(R.id.album_artist_tv) TextView mAlbumArtistTv;
    @Bind(R.id.album_publish_date_tv) TextView mAlbumPubDateTv;
    @Bind(R.id.album_name_tv) TextView mAlbumNameTv;

    private PlaybackDirector.Builder mFabDirector;
    private FabAlphaDirector.Builder mFabAlphaDirector;

    private boolean bindService = false;
    private FMService mFMService;

    private SongDto mCurSong;
    private SongDto mNextSong;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFMService = ((FMService.FmBinder) service).getService();
            if(mFMService.isStartup())
                mFMService.fetchSong();

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
        updateCurrentSongUI(event.mSongDto);
    }

    private void getAlbumInfo(long albumId) {
        Call<AlbumDto> mAlbumCall = mRetrofit.create(DoubanService.class).getAlbumInfo(albumId);
        mAlbumCall.enqueue(new Callback<AlbumDto>() {
            @Override
            public void onResponse(Response<AlbumDto> response, Retrofit retrofit) {
                L.d("url: " + response.raw().request().urlString()
                        + ", response: " + response.raw().toString());

                AlbumDto result = response.body();
                if(result != null) {
                    Picasso.with(getActivity())
                            .load(result.image)
                            .noPlaceholder()
                            .into(mAlbumCoverIv);

                    StringBuilder sb = new StringBuilder();
                    for(ArtistDto item : result.author)
                        sb.append(item.name).append(";");
                    sb.deleteCharAt(sb.length() - 1);
                    mAlbumArtistTv.setText(sb.toString());
                    mAlbumPubDateTv.setText(result.attrs.pubdate.get(0));
                    mAlbumNameTv.setText(getString(R.string.tv_album, result.title));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                L.d("error: " + t.getMessage());
            }
        });
    }

    private void updateCurrentSongUI(@NonNull SongDto songDto) {
        Picasso.with(getActivity())
                .load(songDto.picture)
                .noPlaceholder()
                .into(mSongCoverIv);

        mArtistTv.setText(songDto.artist);
        mSongNameTv.setText(songDto.title);

        // get album info
        getAlbumInfo(songDto.aid);

        mCurSong = songDto;
    }

    @OnClick(R.id.play_fab)
    protected void fabOnClick() {
        mFMService.play(mCurSong);

        mFabAlphaDirector.animate();
        mFabDirector.expand();
    }

    @OnClick(R.id.song_pause_iv)
    protected void pauseOnClick() {
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

}
