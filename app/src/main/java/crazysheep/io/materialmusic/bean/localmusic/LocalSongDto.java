package crazysheep.io.materialmusic.bean.localmusic;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import crazysheep.io.materialmusic.bean.ISong;

/**
 * local song
 *
 * Created by crazysheep on 15/12/28.
 */
@ParcelablePlease
public class LocalSongDto implements Parcelable, ISong {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        LocalSongDtoParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<LocalSongDto> CREATOR = new Creator<LocalSongDto>() {
        public LocalSongDto createFromParcel(Parcel source) {
            LocalSongDto target = new LocalSongDto();
            LocalSongDtoParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public LocalSongDto[] newArray(int size) {
            return new LocalSongDto[size];
        }
    };

    @Override
    public String getName() {
        return song_name;
    }

    @Override
    public String getUrl() {
        return song_path;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public String getCover() {
        return album_cover;
    }

    @Override
    public String getArtist() {
        return artist_name;
    }

    @Override
    public String toString() {
        return song_name;
    }
}
