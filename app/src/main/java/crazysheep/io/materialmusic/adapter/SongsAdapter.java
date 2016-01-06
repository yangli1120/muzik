package crazysheep.io.materialmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.bean.PlaylistModel;
import crazysheep.io.materialmusic.bean.PlaylistSongModel;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.utils.DialogUtils;
import crazysheep.io.materialmusic.utils.SongPopupMenuHelper;

/**
 * songs adapter
 *
 * Created by crazysheep on 15/12/29.
 */
public class SongsAdapter extends RecyclerViewBaseAdapter<SongsAdapter.SongHolder, SongModel> {

    private int mHighlightPos = -1;

    public SongsAdapter(@NonNull Context context, List<SongModel> data) {
        super(context, data);
    }

    @Override
    protected SongHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SongHolder(mInflater.inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, int position) {
        ISong item = getItem(position);

        Picasso.with(mContext)
                .cancelRequest(holder.coverIv);

        if(!TextUtils.isEmpty(item.getCover()))
            Picasso.with(mContext)
                    .load(new File(item.getCover()))
                    .fit()
                    .centerCrop()
                    .into(holder.coverIv);
        else
            holder.coverIv.setImageResource(R.drawable.place_holder);
        holder.artistTv.setText(item.getArtist());
        holder.nameTv.setText(item.getName());

        if(mHighlightPos == position)
            holder.highlightV.setVisibility(View.VISIBLE);
        else
            holder.highlightV.setVisibility(View.INVISIBLE);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show popup menu
                showSongPopupMenu(holder, getItem(holder.getAdapterPosition()));
            }
        });
    }

    private void showSongPopupMenu(SongHolder holder, final SongModel song) {
        SongPopupMenuHelper.showMenu(mContext, holder.editBtn,
                new SongPopupMenuHelper.FlagBuilder().noRemove().build(),
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_add_to_playlist: {
                                showPlaylistsDialog(song);

                                return true;
                            }
                        }

                        return false;
                    }
                });
    }

    private void showPlaylistsDialog(final SongModel song) {
        // query playlists
        final List<PlaylistModel> playlists= new Select().from(PlaylistModel.class).execute();
        final List<String> plNames = new ArrayList<>(playlists.size());
        plNames.add(mContext.getString(R.string.tv_create_a_new_playlist));
        for(PlaylistModel model : playlists)
                plNames.add(model.playlist_name);
        // show all playlists
        DialogUtils.showListDialog((Activity) mContext,
                mContext.getString(R.string.tv_choose_playlist),
                plNames,
                new DialogUtils.SingleChoiceCallback() {
                    @Override
                    public void onItemClick(int position) {
                        if (position == 0) {
                            showCreatePlaylistDialog(song);
                        } else {
                            PlaylistModel playlist = playlists.get(position - 1);
                            PlaylistSongModel playlistSongModel = new PlaylistSongModel(
                                    playlist.getId(), song.songId);
                            playlistSongModel.save();
                        }
                    }
                });
    }

    private void showCreatePlaylistDialog(final SongModel song) {
        DialogUtils.showEditDialog((Activity) mContext,
                mContext.getString(R.string.tv_create_a_new_playlist),
                mContext.getString(R.string.hint_input_playlist_name),
                new DialogUtils.EditDoneCallback() {
                    @Override
                    public void onEditDone(String editableString) {
                        if(TextUtils.isEmpty(editableString)) {
                            Toast.makeText(mContext, R.string.toast_playlist_name_is_empty,
                                    Toast.LENGTH_SHORT).show();

                            return;
                        }

                        List<PlaylistModel> results = new Select().from(PlaylistModel.class)
                                .where(PlaylistModel.PLAYLIST_NAME + "= ?", editableString.trim())
                                .execute();
                        if(results.size() > 0) {
                            Toast.makeText(mContext, R.string.toast_playlist_already_exist,
                                    Toast.LENGTH_LONG).show();

                            return;
                        }

                        new PlaylistModel(editableString.trim()).save();
                        List<PlaylistModel> playlistModels = new Select().from(PlaylistModel.class)
                                .where(PlaylistModel.PLAYLIST_NAME + "= ?", editableString.trim())
                                .execute();
                        PlaylistSongModel playlistSongModel = new PlaylistSongModel(
                                playlistModels.get(0).getId(), song.songId);
                        playlistSongModel.save();
                    }
                });
    }

    public void highlightItem(int position) {
        // before highlight current song, notify the old item refresh
        int oldHighlightPos = mHighlightPos;
        mHighlightPos = position;
        if(oldHighlightPos >= 0)
            notifyItemChanged(oldHighlightPos);
        notifyItemChanged(mHighlightPos);
    }

    public int findPositionByUrl(@NonNull String url) {
        for(int i = 0; i < getItemCount(); i++)
            if(url.equals(getItem(i).getUrl()))
                return i;

        return -1;
    }

    protected static class SongHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.song_cover_iv) ImageView coverIv;
        @Bind(R.id.song_artist_tv) TextView artistTv;
        @Bind(R.id.song_name_tv) TextView nameTv;
        @Bind(R.id.highlight_v) View highlightV;
        @Bind(R.id.song_edit_ib) ImageButton editBtn;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
