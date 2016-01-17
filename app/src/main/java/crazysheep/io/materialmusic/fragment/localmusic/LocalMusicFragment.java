package crazysheep.io.materialmusic.fragment.localmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.MainActivity;
import crazysheep.io.materialmusic.PlaybackActivity;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.MusicPagerAdapter;
import crazysheep.io.materialmusic.bean.PlaylistModel;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.prefs.PlaylistPrefs;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.ActivityUtils;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * show local music on external storage
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalMusicFragment extends BaseFragment {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tabs) TabLayout mTabLayout;
    @Bind(R.id.content_vp) ViewPager mContentVp;
    @Bind(R.id.collapsing_player_content_ft) View mMiniPlayerLayout;

    private MusicPagerAdapter mMusicAdapter;

    private PlaylistPrefs mPlaylistPrefs;

    private boolean isServiceBind = false;
    private MusicService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBind = true;
            mService = ((MusicService.MusicBinder)service).getService();

            // ready playlist
            if(!TextUtils.isEmpty(mPlaylistPrefs.getLastPlaylist())) {
                List<PlaylistModel> playlistModels = new Select()
                        .from(PlaylistModel.class)
                        .where(PlaylistModel.PLAYLIST_NAME + "=?", mPlaylistPrefs.getLastPlaylist())
                        .execute();
                if(playlistModels.size() == 1)
                    mService.playList(playlistModels.get(0), mPlaylistPrefs.getLastPlayMode(),
                            mPlaylistPrefs.getLastPlaySong(), false);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBind = false;
            mService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // query system media store, update table 'songs'
        RxDB.queryAndUpdateSongs(getActivity().getContentResolver());

        mPlaylistPrefs = new PlaylistPrefs(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_local_music, container, false);
        ButterKnife.bind(this, contentView);

        initUI();

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // start music service, init current playlist in service
        Intent serviceIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(isServiceBind)
            getActivity().unbindService(mConnection);
        if(mService.isIdle())
            getActivity().stopService(new Intent(getActivity(), MusicService.class));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.collapsing_player_content_ft)
    public void clickMiniPlayer() {
        ActivityUtils.start(getActivity(), PlaybackActivity.class);
        getActivity().overridePendingTransition(R.anim.slide_left_in, 0);
    }

    private void initUI() {
        ((MainActivity)getActivity()).setToolbar(mToolbar);
        if(!Utils.checkNull(getSupportActionBar())) {
            getSupportActionBar().setTitle(getString(R.string.tv_local_music_title));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mMusicAdapter = new MusicPagerAdapter(getFragmentManager(), getActivity());
        mContentVp.setAdapter(mMusicAdapter);
        mContentVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mContentVp);
        mContentVp.setOffscreenPageLimit(mMusicAdapter.getCount());

        // init bottom layout
        getChildFragmentManager().beginTransaction()
                .replace(R.id.collapsing_player_content_ft, new MiniPlayerFragment(),
                        MiniPlayerFragment.TAG)
                .commitAllowingStateLoss();
    }

}
