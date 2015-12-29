package crazysheep.io.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.SongsAdapter;
import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Subscription;

/**
 * all local songs fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class SongsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    private LinearLayoutManager mLayoutMgr;
    private SongsAdapter mAdapter;

    private Subscription mSubsription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_songs, container, false);
        ButterKnife.bind(this, contentView);

        mLayoutMgr = new LinearLayoutManager(getActivity());
        mAdapter = new SongsAdapter(getActivity(), null);
        mSongsRv.setLayoutManager(mLayoutMgr);
        mSongsRv.setAdapter(mAdapter);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        querySongs();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(!Utils.checkNull(mSubsription) && mSubsription.isUnsubscribed())
            mSubsription.unsubscribe();
    }

    private void querySongs() {
        mSubsription = RxDB.getAllSongs(getActivity().getContentResolver(),
                new RxDB.OnQueryListener<LocalSongDto>() {
                    @Override
                    public void onResult(List<LocalSongDto> results) {
                        mAdapter.setData(results);
                    }

                    @Override
                    public void onError(String err) {
                    }
                });
    }

}
