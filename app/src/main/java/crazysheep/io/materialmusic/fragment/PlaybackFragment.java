package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
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

    private Call<PlaylistDto> mPlaylistCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlaylistCall = mRetrofit.create(DoubanService.class).fetchPlaylist(2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playback, container, false);
        ButterKnife.bind(this, contentView);

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
                }
            }

            @Override
            public void onFailure(Throwable t) {
                L.d("error: " + t.getMessage());
            }
        });
    }
}
