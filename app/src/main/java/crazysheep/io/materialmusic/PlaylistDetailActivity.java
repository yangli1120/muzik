package crazysheep.io.materialmusic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.adapter.PlaylistDetailAdapter;
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
    private PlaylistDetailAdapter mAdapter;
    private LinearLayoutManager mLayoutMgr;
    private ImageView mParallaxHeaderIv;

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

        // set appbar layout alpha 0f
        alphaAppbar(0);

        mLayoutMgr = new LinearLayoutManager(this);
        mSongsRv.setLayoutManager(mLayoutMgr);

        View parallaxHeader = LayoutInflater.from(this).inflate(
                R.layout.layout_playlist_detail_parallax_header, mSongsRv, false);
        mParallaxHeaderIv = ButterKnife.findById(parallaxHeader, R.id.parallax_header_iv);
        if(!TextUtils.isEmpty(mAlbumDto.album_cover))
            Picasso.with(this)
                    .load(new File(mAlbumDto.album_cover))
                    .into(mParallaxHeaderIv);
        mAdapter = new PlaylistDetailAdapter(this, mAlbumDto.songs);
        mAdapter.setParallaxHeader(parallaxHeader, mSongsRv);
        mAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                if(!Float.isNaN(percentage)) {
                    if (parallax.getBottom() < 10)
                        percentage = 1f;
                    alphaAppbar(Math.round(percentage * 255));
                }
            }
        });
        mSongsRv.setAdapter(mAdapter);
    }

    private void parseIntent() {
        mAlbumDto = getIntent().getParcelableExtra(EXTRA_ALBUM);
    }

    private void alphaAppbar(int alpha) {
        mAppbar.getBackground().setAlpha(alpha);
    }

}
