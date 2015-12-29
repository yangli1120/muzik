package crazysheep.io.materialmusic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

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
public class PlaylistDetailActivity extends BaseSwipeBackActivity {

    public static final String EXTRA_ALBUM = "extra_album";

    @Bind(R.id.appbar) AppBarLayout mAppbar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.data_rv) RecyclerView mSongsRv;
    @Bind(R.id.parallax_header_iv) ImageView mParallaxHeaderIv;
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
    }

    private void parseIntent() {
        mAlbumDto = getIntent().getParcelableExtra(EXTRA_ALBUM);
    }

}
