package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.PlaylistAdapter;
import crazysheep.io.materialmusic.db.MediaStoreHelper;

/**
 * albums fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class AlbumsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mAlbumsRv;
    private GridLayoutManager mLayoutMgr;
    private PlaylistAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, contentView);

        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
        mAdapter = new PlaylistAdapter(getActivity(),
                MediaStoreHelper.getAllAlbums(getActivity().getContentResolver()));
        mAlbumsRv.setLayoutManager(mLayoutMgr);
        mAlbumsRv.setAdapter(mAdapter);

        return contentView;
    }

}
