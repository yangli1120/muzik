package crazysheep.io.materialmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.adapter.SongsAdapter;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.PlaylistModel;
import crazysheep.io.materialmusic.bean.PlaylistSongModel;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.db.RxDB;
import crazysheep.io.materialmusic.fragment.localmusic.MiniPlayerFragment;
import crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.L;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.SimplePanelSlideListener;
import de.greenrobot.event.EventBus;

/**
 * show contain songs of a playlist
 *
 * Created by crazysheep on 15/12/29.
 */
public class PlaylistDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @Bind(R.id.appbar) AppBarLayout mAppbar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    @Bind(R.id.parallax_header_iv) ImageView mParallaxHeaderIv;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFab;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPl;
    @Bind(R.id.collapsing_player_content_ft) View mBottomMusicMiniLayout;

    private SongsAdapter mAdapter;

    private PlaylistModel mPlaylistModel;

    private ISong mCurrentSong;

    private boolean isServiceBind;
    private MusicService mMusicService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBinder) service).getService();
            isServiceBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
            isServiceBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        ButterKnife.bind(this);

        parseIntent();
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        bindService(new Intent(this, MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        if(isServiceBind)
            unbindService(mConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_fab: {
                // TODO modify playlist
            }break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        setSupportActionBar(mToolbar);
        if(!Utils.checkNull(getSupportActionBar())) {
            getSupportActionBar().setTitle(mPlaylistModel.playlist_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mEditFab.setOnClickListener(this);

        mSongsRv.setLayoutManager(new LinearLayoutManager(this));

        if(!TextUtils.isEmpty(mPlaylistModel.playlist_cover))
            Picasso.with(this)
                    .load(new File(mPlaylistModel.playlist_cover))
                    .into(mParallaxHeaderIv);
        mAdapter = new SongsAdapter(this, null);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isServiceBind) {
                    if (mMusicService.isPlaying() || mMusicService.isPause())
                        mMusicService.playItem(position);
                    else
                        ;// TODO mMusicService.playList(mAlbumDto.songs);
                }
            }
        });
        mSongsRv.setAdapter(mAdapter);

        // if album is not editable, do not show fab
        // see{@link http://stackoverflow.com/questions/31269958/floatingactionbutton-doesnt-hide}
        if(!mPlaylistModel.isEditable) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)mEditFab
                    .getLayoutParams();
            params.setAnchorId(View.NO_ID);
            mEditFab.setLayoutParams(params);
            mEditFab.setVisibility(View.GONE);
        }

        // sliding up layout, music info use first song by default
        mSlidingUpPl.setPanelSlideListener(new SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                mBottomMusicMiniLayout.setAlpha(1f - slideOffset);
            }
        });
    }

    private void parseIntent() {
        mPlaylistModel = getIntent().getParcelableExtra(MusicConstants.EXTRA_PLAYLIST);

        // query table 'playlist_song', get songs of this playlist
        RxDB.query(PlaylistSongModel.class,
                PlaylistSongModel.PLAYLIST + "=?", String.valueOf(mPlaylistModel.getId()),
                PlaylistSongModel.ADDED_AT + " DESC",
                new RxDB.OnQueryListener<PlaylistSongModel>() {
                    @Override
                    public void onResult(List<PlaylistSongModel> results) {
                        ArrayList<SongModel> songs = new ArrayList<>();
                        for(PlaylistSongModel model : results)
                            songs.add(model.song);

                        mAdapter.setData(songs);

                        // init bottom music control layout
                        Bundle argument = new Bundle();
                        argument.putParcelableArrayList(MusicConstants.EXTRA_PLAYLIST, songs);
                        Fragment playFt = new PlaybackFragment();
                        playFt.setArguments(argument);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.expanded_player_content_ft, playFt,
                                        PlaybackFragment.TAG)
                                .commitAllowingStateLoss();
                        Fragment miniFt = new MiniPlayerFragment();
                        miniFt.setArguments(argument);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.collapsing_player_content_ft, miniFt,
                                        MiniPlayerFragment.TAG)
                                .commitAllowingStateLoss();
                    }

                    @Override
                    public void onError(String err) {
                        L.d(err);
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
