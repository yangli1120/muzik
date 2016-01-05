package crazysheep.io.materialmusic.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * song model
 *
 * Created by crazysheep on 16/1/4.
 */
@Table(name = "songs")
public class SongModel extends Model {

    public static final String NAME = "name";
    public static final String SONG_ID = "song_id";
    public static final String URL = "url";
    public static final String COVER = "cover";
    public static final String ARTIST = "artist";
    public static final String ARTIST_ID = "artist_id";
    public static final String ALBUM = "album";
    public static final String ALBUM_ID = "album_id";
    public static final String IS_LOCAL = "is_local";
    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String PLAYLIST_ID = "playlist_id";

    @Column(name = NAME)
    public String name;

    @Column(name = SONG_ID)
    public long songId; // media id in android system media store

    @Column(name = URL)
    public String url;

    @Column(name = COVER)
    public String cover;

    @Column(name = ARTIST)
    public String artist;

    @Column(name = ARTIST_ID)
    public long artistId; // artist id in android system media store

    @Column(name = ALBUM)
    public String album;

    @Column(name = ALBUM_ID)
    public long albumId; // artist id in android system media store

    @Column(name = IS_LOCAL)
    public boolean isLocal;

    @Column(name = PLAYLIST_NAME)
    public String playlist;

    @Column(name = PLAYLIST_ID)
    public long playlistId;

}
