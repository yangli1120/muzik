package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;

/**
 * adapter for playlist at {@link crazysheep.io.materialmusic.fragment.PlaylistFragment}
 *
 * Created by crazysheep on 15/12/17.
 */
public class PlaylistAdapter extends RecyclerViewBaseAdapter<PlaylistAdapter.PlaylistHolder,
        LocalAlbumDto>{

    public PlaylistAdapter(Context context, List<LocalAlbumDto> datas) {
        super(context, datas);
    }

    @Override
    protected PlaylistHolder onCreateHolder(ViewGroup parent, int viewType) {
        final PlaylistHolder holder = new PlaylistHolder(
                mInflater.inflate(R.layout.item_album, parent, false));
        holder.coverIv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        holder.coverIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        ViewGroup.LayoutParams params = holder.coverIv.getLayoutParams();
                        params.height = holder.coverIv.getMeasuredWidth();
                    }
                });

        return holder;
    }

    @Override
    public void onBindViewHolder(PlaylistHolder holder, int position) {
        LocalAlbumDto item = getItem(position);

        // cancel request before
        Picasso.with(mContext)
                .cancelRequest(holder.coverIv);

        String coverPath = item.getAlbumCover(mContext.getContentResolver());
        if(!TextUtils.isEmpty(coverPath))
            Picasso.with(mContext)
                    .load(new File(coverPath))
                    .fit()
                    .centerCrop()
                    .into(holder.coverIv);
        else
            holder.coverIv.setImageResource(0);
        holder.nameTv.setText(item.album_name);
    }

    //////////////////////// view holder ///////////////////////////////
    public static class PlaylistHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.playlist_cover_iv) ImageView coverIv;
        @Bind(R.id.playlist_name_tv) TextView nameTv;

        public PlaylistHolder(View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }
    }

}
