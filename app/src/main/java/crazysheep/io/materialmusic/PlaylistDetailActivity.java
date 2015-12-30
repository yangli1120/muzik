package crazysheep.io.materialmusic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.adapter.SongsAdapter;
import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * show contain songs of a playlist
 *
 * Created by crazysheep on 15/12/29.
 */
public class PlaylistDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener,
        SlidingUpPanelLayout.PanelSlideListener {

    public static final String EXTRA_ALBUM = "extra_album";

    @Bind(R.id.appbar) AppBarLayout mAppbar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    @Bind(R.id.parallax_header_iv) ImageView mParallaxHeaderIv;
    @Bind(R.id.shuffle_fab) FloatingActionButton mShuffleFab;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mSlidingUpPl;
    private SongsAdapter mAdapter;
    private LinearLayoutManager mLayoutMgr;

    private LocalAlbumDto mAlbumDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        ButterKnife.bind(this);

        parseIntent();
        initUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shuffle_fab: {
                // TODO show button play panel
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

        mLayoutMgr = new LinearLayoutManager(this);
        mSongsRv.setLayoutManager(mLayoutMgr);

        if(!TextUtils.isEmpty(mAlbumDto.album_cover))
            Picasso.with(this)
                    .load(new File(mAlbumDto.album_cover))
                    .into(mParallaxHeaderIv);
        mAdapter = new SongsAdapter(this, mAlbumDto.songs);
        mSongsRv.setAdapter(mAdapter);

        // sliding up panel
        mSlidingUpPl.setPanelSlideListener(this);
    }

    private void parseIntent() {
        mAlbumDto = getIntent().getParcelableExtra(EXTRA_ALBUM);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
    }

    @Override
    public void onPanelCollapsed(View panel) {
    }

    @Override
    public void onPanelExpanded(View panel) {
    }

    @Override
    public void onPanelAnchored(View panel) {
    }

    @Override
    public void onPanelHidden(View panel) {
    }

}
