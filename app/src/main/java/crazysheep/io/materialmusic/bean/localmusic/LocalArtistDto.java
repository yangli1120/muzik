package crazysheep.io.materialmusic.bean.localmusic;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

/**
 * local artist
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalArtistDto {

    public static final String[] ARTIST_COLUMNS = new String[] {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    };

    public static LocalArtistDto createFromCursor(@NonNull Cursor cursor) {
        LocalArtistDto artistDto = new LocalArtistDto();
        artistDto.artist_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        artistDto.artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        artistDto.artist_albums_num = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        artistDto.artist_tracks_num = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));

        return artistDto;
    }

    public long artist_id;
    public String artist_name;
    public int artist_tracks_num;
    public int artist_albums_num;

    public String artist_first_album_cover;
}
