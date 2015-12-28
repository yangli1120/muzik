package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;

/**
 * songs adapter
 *
 * Created by crazysheep on 15/12/29.
 */
public class SongsAdapter extends RecyclerViewBaseAdapter<SongsAdapter.SongHolder, LocalSongDto> {

    public SongsAdapter(@NonNull Context context, @NonNull List<LocalSongDto> data) {
        super(context, data);
    }

    @Override
    protected SongHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SongHolder(mInflater.inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        LocalSongDto item = getItem(position);

        Picasso.with(mContext)
                .cancelRequest(holder.coverIv);

        if(!TextUtils.isEmpty(item.album_cover))
            Picasso.with(mContext)
                    .load(new File(item.album_cover))
                    .fit()
                    .centerCrop()
                    .into(holder.coverIv);
        else
            holder.coverIv.setImageResource(R.drawable.place_holder);
        holder.artistTv.setText(item.artist_name);
        holder.nameTv.setText(item.song_name);
    }

    protected static class SongHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.song_cover_iv) ImageView coverIv;
        @Bind(R.id.song_artist_tv) TextView artistTv;
        @Bind(R.id.song_name_tv) TextView nameTv;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
