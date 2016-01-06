package crazysheep.io.materialmusic.fragment.localmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.adapter.SongsAdapter;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.Utils;
import de.greenrobot.event.EventBus;
import rx.Subscription;

/**
 * all local songs fragment
 *
 * Created by crazysheep on 15/12/28.
 */
public class SongsFragment extends BaseFragment {

    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    private SongsAdapter mAdapter;

    private Subscription mSubscription;

    private ISong mCurrentSong;
    private boolean isBindService = false;
    private MusicService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBindService = true;
            mService = ((MusicService.MusicBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBindService = false;
            mService = null;
        }
    };

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_songs, container, false);
        ButterKnife.bind(this, contentView);

        mAdapter = new SongsAdapter(getActivity(), null);
        mSongsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSongsRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isBindService) {
                    if (mService.isPlaying() || mService.isPause()) {
                        mService.playItem(position);
                    } else {
                        //mService.playList(mAdapter.getData());
                        // TODO play playlist
                    }
                }
            }
        });

        querySongs();

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        getActivity().bindService(new Intent(getActivity(), MusicService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(!Utils.checkNull(mSubscription) && mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        EventBus.getDefault().unregister(this);
        if(isBindService)
            getActivity().unbindService(mConnection);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && (mSubscription == null || mSubscription.isUnsubscribed()))
            querySongs();
    }

    @SuppressWarnings("unchecked")
    private void querySongs() {
        mSubscription = RxDB.query(SongModel.class, new RxDB.OnQueryListener<SongModel>() {
            @Override
            public void onResult(List<SongModel> results) {
                mAdapter.setData(results);
            }

            @Override
            public void onError(String err) {
            }
        });
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull BaseMusicService.EventSongProgress event) {
        if(Utils.checkNull(mCurrentSong)
                || !event.song.getUrl().equals(mCurrentSong.getUrl())) {
            mCurrentSong = event.song;
            mAdapter.highlightItem(mAdapter.findPositionByUrl(event.song.getUrl()));
        }
    }

}
