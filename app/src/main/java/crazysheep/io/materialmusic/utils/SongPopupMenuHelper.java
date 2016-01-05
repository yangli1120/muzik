package crazysheep.io.materialmusic.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.PopupMenu;

import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;

/**
 * a helper for show single song's action menu
 *
 * Created by crazysheep on 16/1/4.
 */
public class SongPopupMenuHelper {

    public static final int FLAG_SHOW_ADD = 1;
    public static final int FLAG_SHOW_REMOVE = 1 << 1;
    public static final int FLAG_SHOW_ALL = FLAG_SHOW_REMOVE | FLAG_SHOW_ADD;

    /**
     * show a popup menu for a {@link ISong}
     * */
    public static @NonNull PopupMenu showMenu(@NonNull Context context, @NonNull View anchorView,
                                              int flag,
                                              @NonNull PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu menu;
        if(ApiHelper.checkK())
            menu = new PopupMenu(context, anchorView, GravityCompat.END);
        else
            menu = new PopupMenu(context, anchorView);
        menu.getMenuInflater().inflate(R.menu.single_song, menu.getMenu());
        if((flag & FLAG_SHOW_REMOVE) == 0)
            menu.getMenu().findItem(R.id.menu_remove).setVisible(false);
        if((flag & FLAG_SHOW_ADD) == 0)
            menu.getMenu().findItem(R.id.menu_add_to_playlist).setVisible(false);

        menu.setOnMenuItemClickListener(listener);
        menu.show();

        return menu;
    }

    public static class FlagBuilder {

        private int flag = FLAG_SHOW_ALL;

        public FlagBuilder noRemove() {
            flag &= ~FLAG_SHOW_REMOVE;
            return this;
        }

        public FlagBuilder noAddToPlaylist() {
            flag &= ~FLAG_SHOW_ADD;

            return this;
        }

        public int build() {
            return flag;
        }
    }

 }
