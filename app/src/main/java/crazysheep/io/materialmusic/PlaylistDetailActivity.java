package crazysheep.io.materialmusic;

import android.app.Activity;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.ActivityUtils;
import crazysheep.io.materialmusic.utils.L;
import crazysheep.io.materialmusic.utils.Utils;
import de.greenrobot.event.EventBus;

/**
 * show contain songs of a playlist
 *
 * Created by crazysheep on 15/12/29.
 */
public class PlaylistDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    private static final int REQUEST_EDIT_PLAYLIST = 9527;

    @Bind(R.id.appbar) AppBarLayout mAppbar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    @Bind(R.id.parallax_header_iv) ImageView mParallaxHeaderIv;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFab;

    private SongsAdapter mAdapter;

    private PlaylistModel mPlaylistModel;
    private List<PlaylistSongModel> mPlaylistSongModels;

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
                ActivityUtils.startResult(this, REQUEST_EDIT_PLAYLIST,
                        ActivityUtils.prepare(this, PlaylistEditActivity.class)
                                .putParcelableArrayListExtra(MusicConstants.EXTRA_SONG_LIST,
                                        new ArrayList<>(mAdapter.getData())));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EDIT_PLAYLIST: {
                    ArrayList<SongModel> songModels = data.getParcelableArrayListExtra(
                            MusicConstants.EXTRA_SONG_LIST);

                    // playlist is modified, update database
                    List<PlaylistSongModel> surviveItems = new ArrayList<>();
                    for(int x = 0; x < songModels.size(); x++)
                        for(int y = 0; y < mPlaylistSongModels.size(); y++)
                            if(songModels.get(x).songId == mPlaylistSongModels.get(y).song.songId)
                                surviveItems.add(mPlaylistSongModels.get(y));
                    mPlaylistSongModels.removeAll(surviveItems);
                    // delete items first
                    for(PlaylistSongModel playlistSongModel : mPlaylistSongModels)
                        playlistSongModel.delete();
                    // update index in playlist
                    for(int index = 0; index < surviveItems.size(); index++) {
                        surviveItems.get(index).index_of_playlist = surviveItems.size() - index;
                        surviveItems.get(index).save();
                    }

                    // re-query from db
                    queryPlaylist();
                }break;
            }
        }
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
                    mMusicService.playList(mPlaylistModel, MusicConstants.PLAY_LOOP_ALL,
                            mAdapter.getItem(position).songId, true);
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
    }

    private void parseIntent() {
        mPlaylistModel = getIntent().getParcelableExtra(MusicConstants.EXTRA_PLAYLIST);

        queryPlaylist();
    }

    private void queryPlaylist() {
        // query table 'playlist_song', get songs of this playlist
        RxDB.query(PlaylistSongModel.class,
                PlaylistSongModel.PLAYLIST + "=?", String.valueOf(mPlaylistModel.getId()),
                PlaylistSongModel.INDEX_OF_PLAYLIST + " DESC",
                new RxDB.OnQueryListener<PlaylistSongModel>() {
                    @Override
                    public void onResult(List<PlaylistSongModel> results) {
                        mPlaylistSongModels = results;

                        ArrayList<SongModel> songs = new ArrayList<>();
                        for(PlaylistSongModel model : results)
                            songs.add(model.song);

                        mAdapter.setData(songs);
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
