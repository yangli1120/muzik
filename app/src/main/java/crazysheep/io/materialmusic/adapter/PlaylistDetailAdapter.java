package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;

/**
 * parallax recycler adapter for {@link crazysheep.io.materialmusic.PlaylistDetailActivity}
 *
 * Created by crazysheep on 15/12/29.
 */
public class PlaylistDetailAdapter extends ParallaxRecyclerAdapter<LocalSongDto> {

    private Context mContext;
    private LayoutInflater mInflater;

    public PlaylistDetailAdapter(@NonNull Context context, List<LocalSongDto> data) {
        super(data);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<LocalSongDto> parallaxRecyclerAdapter, int i) {
        SongHolder holder = (SongHolder) viewHolder;

        Picasso.with(mContext)
                .cancelRequest(holder.coverIv);

        LocalSongDto item = getItem(i);
        if(!TextUtils.isEmpty(item.album_cover))
            Picasso.with(mContext)
                    .load(new File(item.album_cover))
                    .fit()
                    .centerCrop()
                    .into(holder.coverIv);
        else
            holder.coverIv.setImageResource(R.drawable.place_holder);

        holder.nameTv.setText(item.song_name);
        holder.artistTv.setText(item.artist_name);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<LocalSongDto> parallaxRecyclerAdapter, int i) {
        return new SongHolder(mInflater.inflate(R.layout.item_song, viewGroup, false));
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<LocalSongDto> parallaxRecyclerAdapter) {
        return getData() == null ? 0 : getData().size();
    }

    public LocalSongDto getItem(int position) {
        return getData() == null ? null : getData().get(position);
    }

    ////////////////////////////// view holder /////////////////////////////

    protected static class SongHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.song_cover_iv) ImageView coverIv;
        @Bind(R.id.song_name_tv) TextView nameTv;
        @Bind(R.id.song_artist_tv) TextView artistTv;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
