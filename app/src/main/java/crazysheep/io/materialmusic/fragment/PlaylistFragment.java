package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.PlaylistAdapter;
import crazysheep.io.materialmusic.bean.PlaylistDto;
import crazysheep.io.materialmusic.net.DoubanService;
import crazysheep.io.materialmusic.utils.L;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * fragment show hot playlist
 *
 * Created by crazysheep on 15/12/17.
 */
public class PlaylistFragment extends BaseFragment {

    @Bind(R.id.playlist_rv) RecyclerView mPlaylistRv;
    private PlaylistAdapter mAdapter;

    private Call<PlaylistDto> mPlaylistCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlaylistCall = mRetrofit.create(DoubanService.class).fetchPlaylist(2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, contentView);

        mAdapter = new PlaylistAdapter(getActivity(), null);
        mPlaylistRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlaylistRv.setAdapter(mAdapter);

        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPlaylistCall.enqueue(new Callback<PlaylistDto>() {
            @Override
            public void onResponse(Response<PlaylistDto> response, Retrofit retrofit) {
                L.d("url: " + response.raw().request().urlString()
                        + ", response: " + response.raw().toString());


            }

            @Override
            public void onFailure(Throwable t) {
                L.d("error: " + t.getMessage());
            }
        });
    }

}
