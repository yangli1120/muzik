package crazysheep.io.materialmusic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crazysheep.io.materialmusic.adapter.PlaylistEditAdapter;
import crazysheep.io.materialmusic.adapter.RecyclerViewBaseAdapter;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.constants.MusicConstants;
import crazysheep.io.materialmusic.utils.DialogUtils;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.helper.OnStartDragListener;
import crazysheep.io.materialmusic.widget.helper.SimpleItemTouchHelperCallback;

/**
 * edit playlist activity
 *
 * <br><h1>The draggable and swipe RecyclerView see{https://github.com/iPaulPro/Android-ItemTouchHelper-Demo}</br>
 *
 * Created by crazysheep on 16/1/17.
 */
public class PlaylistEditActivity extends BaseSwipeBackActivity implements OnStartDragListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.songs_rv) RecyclerView mSongsRv;

    private PlaylistEditAdapter mAdapter;
    private List<SongModel> mSongs;

    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_edit);
        ButterKnife.bind(this);

        mSongs = getIntent().getParcelableArrayListExtra(MusicConstants.EXTRA_SONG_LIST);

        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playlist_edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }

            case R.id.action_edit_done: {
                Intent data = new Intent();
                data.putParcelableArrayListExtra(MusicConstants.EXTRA_SONG_LIST,
                        new ArrayList<>(mAdapter.getData()));
                setResult(Activity.RESULT_OK, data);
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showConfirmDialog(this, getString(R.string.tv_give_up_playlist_edit), null,
                new DialogUtils.ButtonAction() {
                    @Override
                    public String getTitle() {
                        return getString(R.string.tv_ok);
                    }

                    @Override
                    public void onClick(DialogInterface dialog) {
                        finish();
                    }
                },
                null);
    }

    private void initUI() {
        setSupportActionBar(mToolbar);
        if(!Utils.checkNull(getSupportActionBar())) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.tv_item_chosen, String.valueOf(0)));
        }

        mSongsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PlaylistEditAdapter(getActivity(), mSongs, this);
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.toggleItemChosen(position);

                if(!Utils.checkNull(getSupportActionBar()))
                    getSupportActionBar().setTitle(getString(R.string.tv_item_chosen,
                            mAdapter.getItemChosenCount()));
            }
        });
        mSongsRv.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mSongsRv);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.delete_ib)
    public void clickDeleteButton() {
        if(mAdapter.getItemChosenCount() == 0) {
            Snackbar.make(mSongsRv, R.string.tv_no_item_chosen, Snackbar.LENGTH_SHORT).show();
            return;
        }

        DialogUtils.showConfirmDialog(this, getString(R.string.tv_delete),
                getString(R.string.tv_delete_confirm, mAdapter.getItemChosenCount()),
                new DialogUtils.ButtonAction() {
                    @Override
                    public String getTitle() {
                        return getString(R.string.tv_ok);
                    }

                    @Override
                    public void onClick(DialogInterface dialog) {
                        mAdapter.removeChosenItems();
                    }
                },
                null);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.select_all_ib)
    public void clickSelectAllButton() {
        mAdapter.checkAllItem();
    }

}
