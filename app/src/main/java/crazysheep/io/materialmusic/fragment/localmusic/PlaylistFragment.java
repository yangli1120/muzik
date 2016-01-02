package crazysheep.io.materialmusic.fragment.localmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.PlaylistDetailActivity;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.PlaylistAdapter;
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.db.MediaStoreHelper;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.utils.ActivityUtils;

/**
 * fragment show local playlist
 *
 * Created by crazysheep on 15/12/17.
 */
public class PlaylistFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mPlaylistRv;
    private GridLayoutManager mLayoutMgr;
    private PlaylistAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, contentView);

        mAdapter = new PlaylistAdapter(getActivity(), null);
        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
        mPlaylistRv.setLayoutManager(mLayoutMgr);
        mPlaylistRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActivityUtils.start(getActivity(),
                        ActivityUtils.prepare(getActivity(), PlaylistDetailActivity.class)
                                .putExtra(PlaylistDetailActivity.EXTRA_ALBUM,
                                        mAdapter.getItem(position)));
            }
        });

        initDefaultPlaylist();

        return contentView;
    }

    private void initDefaultPlaylist() {
        LocalAlbumDto albumDto = new LocalAlbumDto();
        albumDto.songs = MediaStoreHelper.getAllSongs(getActivity().getContentResolver());
        albumDto.album_name = getString(R.string.tv_default_playlist_name);

        List<LocalAlbumDto> albums = new ArrayList<>();
        albums.add(albumDto);
        mAdapter.setData(albums);
    }

}
