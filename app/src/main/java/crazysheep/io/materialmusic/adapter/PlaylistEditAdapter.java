package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.SongModel;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.helper.ItemTouchHelperAdapter;
import crazysheep.io.materialmusic.widget.helper.ItemTouchHelperViewHolder;
import crazysheep.io.materialmusic.widget.helper.OnStartDragListener;

/**
 * adapter for edit playlist
 *
 * Created by crazysheep on 16/1/17.
 */
public class PlaylistEditAdapter extends RecyclerViewBaseAdapter<PlaylistEditAdapter.SongHolder,
        SongModel> implements ItemTouchHelperAdapter {

    private OnStartDragListener mStartDragListener;

    private LinkedList<Boolean> mChooseMap;

    public PlaylistEditAdapter(@NonNull Context context, List<SongModel> data,
                               OnStartDragListener dragListener) {
        super(context, data);
        mStartDragListener = dragListener;

        initChooseMap();
    }

    @Override
    public void setData(List<SongModel> items) {
        super.setData(items);

        initChooseMap();
    }

    private void initChooseMap() {
        mChooseMap = new LinkedList<>();
        for(int i = 0; i < getItemCount(); i++)
            mChooseMap.add(i, false);
    }

    @Override
    protected SongHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SongHolder(mInflater.inflate(R.layout.item_edit_song, parent, false));
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, int position) {
        SongModel item = getItem(position);
        holder.nameTv.setText(item.getName());
        holder.artistTv.setText(item.getArtist());
        holder.hungIv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                        && !Utils.checkNull(mStartDragListener))
                    mStartDragListener.onStartDrag(holder);

                return false;
            }
        });

        holder.songCb.setChecked(mChooseMap.get(position));
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void toggleItemChosen(int position) {
        mChooseMap.set(position, !mChooseMap.get(position));
        notifyItemChanged(position);
    }

    public int getItemChosenCount() {
        int count = 0;
        for(int pos = 0; pos < mChooseMap.size(); pos++)
            if(mChooseMap.get(pos))
                count++;

        return count;
    }

    public @NonNull List<SongModel> getItemChosenList() {
        List<SongModel> chosenList = new ArrayList<>();
        for(int pos = 0; pos < getItemCount(); pos++)
            if(mChooseMap.get(pos))
                chosenList.add(getItem(pos));

        return chosenList;
    }

    public void checkAllItem() {
        for(int pos = 0; pos < mChooseMap.size(); pos++)
            mChooseMap.set(pos, true);
        notifyDataSetChanged();
    }

    public void removeChosenItems() {
        for(SongModel item : getItemChosenList())
            removeItem(item);
    }

    /////////////////////// holder //////////////////////////////

    static class SongHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @Bind(R.id.song_cb) CheckBox songCb;
        @Bind(R.id.song_name_tv) TextView nameTv;
        @Bind(R.id.song_artist_tv) TextView artistTv;
        @Bind(R.id.song_hung_iv) ImageView hungIv;
        @Bind(R.id.divide_line) View divideL;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
