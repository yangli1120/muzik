package crazysheep.io.materialmusic.bean.localmusic;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

/**
 * local song
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalSongDto {

    public static final String[] MUSIC_COLUMNS = new String[] {
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DATE_ADDED,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
    };

    /**
     * create a LocalSongDto from cursor
     * */
    public static LocalSongDto createFromCursor(@NonNull Cursor cursor) {
        LocalSongDto song = new LocalSongDto();

        song.album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
        song.album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
        song.artist_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID));
        song.artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
        song.song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
        song.song_path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
        song.date_added = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED));
        song.date_modified = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED));
        song.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));

        return song;
    }

    public String album_name;
    public long album_id;
    public String artist_name;
    public long artist_id;
    public long duration;
    public String song_name;
    public String song_path;
    public long date_added;
    public long date_modified;

    public String album_cover;
}
