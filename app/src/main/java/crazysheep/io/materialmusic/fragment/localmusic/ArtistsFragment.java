package crazysheep.io.materialmusic.fragment.localmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.ArtistsAdapter;
import crazysheep.io.materialmusic.bean.localmusic.LocalArtistDto;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Subscription;

/**
 * artist list fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class ArtistsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mArtistsRv;
    private GridLayoutManager mLayoutMgr;
    private ArtistsAdapter mAdapter;

    private Subscription mSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, contentView);

        mLayoutMgr = new GridLayoutManager(getActivity(), 2);
        mAdapter = new ArtistsAdapter(getActivity(), null);
        mArtistsRv.setLayoutManager(mLayoutMgr);
        mArtistsRv.setAdapter(mAdapter);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        queryArtists();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(!Utils.checkNull(mSubscription) && mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    private void queryArtists() {
        mSubscription = RxDB.getAllArtists(getActivity().getContentResolver(),
                new RxDB.OnQueryListener<LocalArtistDto>() {
                    @Override
                    public void onResult(List<LocalArtistDto> results) {
                        mAdapter.setData(results);
                    }

                    @Override
                    public void onError(String err) {
                    }
                });
    }

}
