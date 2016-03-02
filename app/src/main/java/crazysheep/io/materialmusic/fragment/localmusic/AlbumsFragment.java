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
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.PlaylistAdapter;
import crazysheep.io.materialmusic.bean.IPlaylist;
import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Subscription;

/**
 * albums fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class AlbumsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mAlbumsRv;
    private GridLayoutManager mLayoutMgr;
    private PlaylistAdapter mAdapter;

    private Subscription mSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, contentView);

        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
        mAdapter = new PlaylistAdapter(getActivity(), null);
        mAlbumsRv.setLayoutManager(mLayoutMgr);
        mAlbumsRv.setAdapter(mAdapter);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        queryAlbums();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(!Utils.checkNull(mSubscription) && mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    private void queryAlbums() {
        mSubscription = RxDB.getAllAlbums(getActivity().getContentResolver(),
                new RxDB.OnQueryListener<LocalAlbumDto>() {
                    @Override
                    public void onResult(List<LocalAlbumDto> results) {
                        List<IPlaylist> albums = new ArrayList<>(results.size());
                        for(LocalAlbumDto album : results)
                            albums.add(album);
                        mAdapter.setData(albums);
                    }

                    @Override
                    public void onError(String err) {
                    }
                });
    }

}
