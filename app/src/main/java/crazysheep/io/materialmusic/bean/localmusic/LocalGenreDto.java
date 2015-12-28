package crazysheep.io.materialmusic.bean.localmusic;

import android.provider.MediaStore;

import java.util.List;

/**
 * genre info
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalGenreDto {

    public static final String[] GENRE_COLUMNS = new String[] {
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME,
    };

    public String genre_name;
    public List<LocalSongDto> genre_songs;
}
