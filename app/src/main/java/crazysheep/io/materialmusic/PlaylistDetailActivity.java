package crazysheep.io.materialmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.adapter.SongsAdapter;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.service.MusicService;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.PlayOrPauseImageButton;
import crazysheep.io.materialmusic.widget.SimplePanelSlideListener;
import de.greenrobot.event.EventBus;

/**
 * show contain songs of a playlist
 *
 * Created by crazysheep on 15/12/29.
 */
public class PlaylistDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    public static final String EXTRA_ALBUM = "extra_album";

    @Bind(R.id.appbar) AppBarLayout mAppbar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    @Bind(R.id.parallax_header_iv) ImageView mParallaxHeaderIv;
    @Bind(R.id.shuffle_fab) FloatingActionButton mShuffleFab;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPl;
    @Bind(R.id.song_name_tv) TextView mBottomSongNameTv;
    @Bind(R.id.song_artist_tv) TextView mBottomSongArtistTv;
    @Bind(R.id.song_cover_iv) ImageView mBottomSongCoverIv;
    @Bind(R.id.song_play_iv) PlayOrPauseImageButton mBottomSongPlayOrPauseBtn;
    @Bind(R.id.song_next_iv) ImageButton mBottomSongNextBtn;
    @Bind(R.id.mini_player_ll) View mBottomMusicMiniLayout;

    private SongsAdapter mAdapter;
    private LinearLayoutManager mLayoutMgr;

    private LocalAlbumDto mAlbumDto;

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
            case R.id.shuffle_fab: {
                if(isServiceBind) {
                    if(mMusicService.isPause()) {
                        mMusicService.resume();

                        mShuffleFab.setImageResource(R.drawable.ic_pause);
                    } else if(!mMusicService.isPlaying()) {
                        mMusicService.play(mAlbumDto.songs, MusicConstants.PLAY_SHUFFLE);

                        mShuffleFab.setImageResource(R.drawable.ic_pause);
                    } else {
                        mMusicService.pause();

                        mShuffleFab.setImageResource(R.drawable.ic_shuffle);
                    }
                }
            }break;

            case R.id.song_play_iv: {
                if(isServiceBind) {
                    if(mMusicService.isPlaying() || mMusicService.isPrepare()) {
                        mMusicService.pause();

                        mBottomSongPlayOrPauseBtn.toggle(false);
                    } else {
                        mMusicService.resume();

                        mBottomSongPlayOrPauseBtn.toggle(true);
                    }
                }
            }break;

            case R.id.song_next_iv: {
                if(isServiceBind) {
                    mMusicService.next();
                }
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
            getSupportActionBar().setTitle(mAlbumDto.album_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mShuffleFab.setOnClickListener(this);
        mBottomSongPlayOrPauseBtn.setOnClickListener(this);
        mBottomSongNextBtn.setOnClickListener(this);

        mLayoutMgr = new LinearLayoutManager(this);
        mSongsRv.setLayoutManager(mLayoutMgr);

        if(!TextUtils.isEmpty(mAlbumDto.album_cover))
            Picasso.with(this)
                    .load(new File(mAlbumDto.album_cover))
                    .into(mParallaxHeaderIv);
        mAdapter = new SongsAdapter(this, mAlbumDto.songs);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isServiceBind) {
                    if (mMusicService.isPlaying() || mMusicService.isPause())
                        mMusicService.playItem(position);
                    else
                        mMusicService.play(mAlbumDto.songs);
                }
            }
        });
        mSongsRv.setAdapter(mAdapter);

        // sliding up layout, music info use first song by default
        mSlidingUpPl.setPanelSlideListener(new SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                mBottomMusicMiniLayout.setAlpha(1f - slideOffset);
            }
        });
        updateBottomUIWithSong(mAlbumDto.songs.get(0), false);
        Fragment playFt = new PlaybackFragment();
        Bundle argument = new Bundle();
        argument.putParcelable(PlaybackFragment.EXTRA_SONG, mAlbumDto.songs.get(0));
        playFt.setArguments(argument);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.expanded_player_content_ft, playFt, PlaybackFragment.TAG)
                .commitAllowingStateLoss();
    }

    private void parseIntent() {
        mAlbumDto = getIntent().getParcelableExtra(EXTRA_ALBUM);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull BaseMusicService.EventCurrentSong event) {
        if(Utils.checkNull(mCurrentSong)
                || !event.currentSong.getUrl().equals(mCurrentSong.getUrl())) {
            mCurrentSong = event.currentSong;
            mAdapter.highlightItem(mAdapter.findPositionByUrl(event.currentSong.getUrl()));

            updateBottomUIWithSong(mCurrentSong, true);
        }
    }

    private void updateBottomUIWithSong(@NonNull ISong song, boolean isPlaying) {
        mBottomSongNameTv.setText(song.getName());
        mBottomSongArtistTv.setText(song.getArtist());
        if(!TextUtils.isEmpty(song.getCover()))
            Picasso.with(this)
                    .load(new File(song.getCover()))
                    .fit()
                    .error(R.drawable.place_holder)
                    .into(mBottomSongCoverIv);
        else
            mBottomSongCoverIv.setImageResource(R.drawable.place_holder);

        mBottomSongPlayOrPauseBtn.toggle(isPlaying);
    }

}
