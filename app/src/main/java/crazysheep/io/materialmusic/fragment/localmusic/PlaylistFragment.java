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
import crazysheep.io.materialmusic.PlaylistDetailActivity;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.PlaylistAdapter;
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.bean.PlaylistModel;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.utils.ActivityUtils;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Subscription;

/**
 * fragment show local playlist
 *
 * Created by crazysheep on 15/12/17.
 */
public class PlaylistFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mPlaylistRv;
    private PlaylistAdapter mAdapter;

    private Subscription mSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        ButterKnife.bind(this, contentView);

        mAdapter = new PlaylistAdapter(getActivity(), null);
        mPlaylistRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mPlaylistRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActivityUtils.start(getActivity(),
                        ActivityUtils.prepare(getActivity(), PlaylistDetailActivity.class)
                                .putExtra(MusicConstants.EXTRA_PLAYLIST, mAdapter.getItem(position)));
            }
        });

        queryPlaylists();

        return contentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && (Utils.checkNull(mSubscription) || mSubscription.isUnsubscribed()))
            queryPlaylists();
    }

    private void queryPlaylists() {
        mSubscription = RxDB.query(PlaylistModel.class, null, null, null,
                new RxDB.OnQueryListener<PlaylistModel>() {
                    @Override
                    public void onResult(List<PlaylistModel> results) {
                        mAdapter.setData(results);
                    }

                    @Override
                    public void onError(String err) {
                    }
                });
    }

}
