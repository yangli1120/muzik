package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
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
import crazysheep.io.materialmusic.bean.PlaylistDto;
import crazysheep.io.materialmusic.net.DoubanService;
import crazysheep.io.materialmusic.utils.L;
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

    private DoubanService mDoubanService;
    private Call<PlaylistDto> mPlaylistCall;
    private Call<AlbumDto> mAlbumCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDoubanService = mRetrofit.create(DoubanService.class);
        mPlaylistCall = mDoubanService.fetchPlaylist(2);
    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPlaylistCall.enqueue(new Callback<PlaylistDto>() {
            @Override
            public void onResponse(Response<PlaylistDto> response, Retrofit retrofit) {
                L.d("request url: " + response.raw().request().urlString()
                        + ", response: " + response.raw().toString());

                PlaylistDto result = response.body();
                if(result != null && result.song != null && result.song.size() > 0) {
                    Picasso.with(getActivity())
                            .load(result.song.get(0).picture)
                            .noPlaceholder()
                            .into(mSongCoverIv);

                    mArtistTv.setText(result.song.get(0).artist);
                    mSongNameTv.setText(result.song.get(0).title);

                    // get album info
                    getAlbumInfo(result.song.get(0).aid);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                L.d("error: " + t.getMessage());
            }
        });
    }

    private void getAlbumInfo(long albumId) {
        mAlbumCall = mDoubanService.getAlbumInfo(albumId);
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

    @OnClick(R.id.play_fab)
    protected void fabOnClick() {
        mFabAlphaDirector.animate();
        mFabDirector.expand();
    }

    @OnClick(R.id.song_pause_iv)
    protected void pauseOnClick() {
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
