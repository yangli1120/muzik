package crazysheep.io.materialmusic.bean;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * model for playlist
 *
 * Created by crazysheep on 16/1/4.
 */
@Table(name = "playlists")
public class PlaylistModel extends Model {

    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String IS_LAST_PLAYED = "is_last_played";

    @Column(name = PLAYLIST_NAME, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String playlist_name;

    @Column(name = IS_LAST_PLAYED)
    public boolean is_last_played; // if this playlist is last played before user exit app

    public PlaylistModel() {
        super();
    }

    public PlaylistModel(@NonNull String playlist_name) {
        this.playlist_name = playlist_name;
    }

}
