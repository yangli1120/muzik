package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.SpringSystem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.localmusic.LocalArtistDto;

/**
 * artists adapter
 *
 * Created by crazysheep on 15/12/28.
 */
public class ArtistsAdapter extends RecyclerViewBaseAdapter<ArtistsAdapter.ArtistHolder, LocalArtistDto> {

    private SpringSystem mSrpingSystem = SpringSystem.create();

    public ArtistsAdapter(@NonNull Context context, List<LocalArtistDto> data) {
        super(context, data);
    }

    @Override
    protected ArtistHolder onCreateHolder(ViewGroup parent, int viewType) {
        final ArtistHolder holder = new ArtistHolder(
                mInflater.inflate(R.layout.item_artist, parent, false), mSrpingSystem);
        holder.coverIv.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        holder.coverIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        ViewGroup.LayoutParams params = holder.coverIv.getLayoutParams();
                        if(holder.coverIv.getMeasuredWidth() > 0)
                            params.height = Math.round(holder.coverIv.getMeasuredWidth() * 3f / 5);
                        holder.coverIv.setLayoutParams(params);
                    }
                });

        return holder;
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        LocalArtistDto item = getItem(position);

        // cancel request of holder.coverIv before
        Picasso.with(mContext)
                .cancelRequest(holder.coverIv);

        if(!TextUtils.isEmpty(item.artist_first_album_cover))
            Picasso.with(mContext)
                    .load(new File(item.artist_first_album_cover))
                    .fit()
                    .centerCrop()
                    .into(holder.coverIv);
        else
            holder.coverIv.setImageResource(R.drawable.place_holder);

        holder.nameTv.setText(item.artist_name);
    }

    /////////////////////// view holder ////////////////////////////////

    protected static class ArtistHolder extends RecyclerViewBaseAdapter.ReboundViewHolder {

        @Bind(R.id.artist_cover_iv) ImageView coverIv;
        @Bind(R.id.artist_name_tv) TextView nameTv;

        public ArtistHolder(View view, SpringSystem ss) {
            super(view, ss);
            ButterKnife.bind(this, view);
        }
    }
}
