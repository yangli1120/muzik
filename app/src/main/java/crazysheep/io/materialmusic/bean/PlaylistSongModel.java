package crazysheep.io.materialmusic.bean;

import android.support.annotation.NonNull;

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

    ////////////////////////// easy make playlist_id_plus_song_id ///////////////////////////

    public static String make(long playlist_id, long song_id) {
        return String.valueOf(playlist_id) + "_" + String.valueOf(song_id);
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public static final String TABLE_NAME = "playlist_song";

    public static final String PLAYLIST = "playlist";
    public static final String ADDED_AT = "added_at";
    public static final String INDEX_OF_PLAYLIST = "index_of_playlist";
    public static final String PLAYLIST_ID_PLUS_SONG_ID = "playlist_id_plus_song_id";
    public static final String SONG = "song";

    @Column(name = PLAYLIST)
    public PlaylistModel playlist;

    @Column(name = ADDED_AT)
    public long added_at;

    @Column(name = INDEX_OF_PLAYLIST)
    public int index_of_playlist;

    @Column(name = PLAYLIST_ID_PLUS_SONG_ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String playlist_id_plus_song_id;

    @Column(name = SONG)
    public SongModel song;

    public PlaylistSongModel() {
        super();
    }

    public PlaylistSongModel(@NonNull PlaylistModel playlist, @NonNull SongModel song,
                             long added_at) {
        super();
        this.playlist = playlist;
        this.song = song;
        this.playlist_id_plus_song_id = make(playlist.getId(), song.songId);
        this.added_at = added_at;
    }

    @Override
    public String toString() {
        return playlist + "/" + song;
    }
}
