package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;

/**
 * simple songs adapter for current playlist,
 * see{@link crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment}
 *
 * Created by crazysheep on 16/1/15.
 */
public class SimpleSongsAdapter extends RecyclerViewBaseAdapter<SimpleSongsAdapter.SimpleSongHolder,
        ISong> {

    private int highlightPos = -1;

    public SimpleSongsAdapter(@NonNull Context context, List<ISong> data) {
        super(context, data);
    }

    @Override
    protected SimpleSongHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SimpleSongHolder(mInflater.inflate(R.layout.item_simple_song, parent, false));
    }

    @Override
    public void onBindViewHolder(SimpleSongHolder holder, int position) {
        ISong item = getItem(position);
        holder.nameTv.setText(item.getName());
        holder.artistTv.setText(item.getArtist());

        if(position == highlightPos) {
            holder.nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.highlight_song));
            holder.divideTv.setTextColor(ContextCompat.getColor(mContext, R.color.highlight_song));
            holder.artistTv.setTextColor(ContextCompat.getColor(mContext, R.color.highlight_song));
        } else {
            holder.nameTv.setTextColor(ContextCompat.getColor(mContext, (R.color.text_grey_dark)));
            holder.divideTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_grey_light));
            holder.artistTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_grey_light));
        }
    }

    public void highlight(@NonNull String songUrl) {
        for(int index = 0; index < getData().size(); index++)
            if(songUrl.equals(getItem(index).getUrl()))
                notifyItemChanged(highlightPos = index);
    }

    //////////////////////////// holder /////////////////////////////////////

    static class SimpleSongHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.song_name_tv) TextView nameTv;
        @Bind(R.id.song_artist_tv) TextView artistTv;
        @Bind(R.id.divide_tv) TextView divideTv;

        public SimpleSongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
