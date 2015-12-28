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
import crazysheep.io.materialmusic.adapter.ArtistsAdapter;
import crazysheep.io.materialmusic.db.MediaStoreHelper;

/**
 * artist list fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class ArtistsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mArtistsRv;
    private GridLayoutManager mLayoutMgr;
    private ArtistsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, contentView);

        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
        mAdapter = new ArtistsAdapter(getActivity(),
                MediaStoreHelper.getAllArtist(getActivity().getContentResolver()));
        mArtistsRv.setLayoutManager(mLayoutMgr);
        mArtistsRv.setAdapter(mAdapter);

        return contentView;
    }
}
