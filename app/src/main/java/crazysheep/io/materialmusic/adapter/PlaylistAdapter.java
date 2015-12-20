package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * adapter for playlist at {@link crazysheep.io.materialmusic.fragment.PlaylistFragment}
 *
 * Created by crazysheep on 15/12/17.
 */
public class PlaylistAdapter extends RecyclerViewBaseAdapter<PlaylistAdapter.PlaylistHolder,
        Object>{

    public PlaylistAdapter(Context context, List<Object> datas) {
        super(context, datas);
    }

    @Override
    protected PlaylistHolder onCreateHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PlaylistHolder holder, int position) {

    }

    //////////////////////// view holder ///////////////////////////////
    public static class PlaylistHolder extends RecyclerView.ViewHolder {
        public PlaylistHolder(View parent) {
            super(parent);
        }
    }
}
