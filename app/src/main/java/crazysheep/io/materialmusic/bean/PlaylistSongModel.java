package crazysheep.io.materialmusic.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * table contains song of a playlist
 *
 * Created by crazysheep on 16/1/5.
 */
@Table(name = "playlist_song")
public class PlaylistSongModel extends Model {

    public static final String TABLE_NAME = "playlist_song";

    public static final String PLAYLIST_ID = "playlist_id";
    public static final String SONG_ID = "song_id";
    public static final String PLAYLIST_ID_PLUS_SONG_ID = "playlist_id_plus_song_id";

    @Column(name = PLAYLIST_ID)
    public long playlist_id;

    @Column(name = SONG_ID)
    public long song_id;

    @Column(name = PLAYLIST_ID_PLUS_SONG_ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String playlist_id_plus_song_id;

    public PlaylistSongModel() {
        super();
    }

    public PlaylistSongModel(long playlist_id, long song_id) {
        super();
        this.playlist_id = playlist_id;
        this.song_id = song_id;
        this.playlist_id_plus_song_id = String.valueOf(playlist_id) + "_" + String.valueOf(song_id);
    }

}
