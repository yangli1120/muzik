package crazysheep.io.materialmusic.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import crazysheep.io.materialmusic.bean.localmusic.LocalSongDto;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * helper for system media store
 *
 * Created by crazysheep on 15/12/28.
 */
public class MediaStoreHelper {

    /**
     * query system media store, get all songs on external storage
     * */
    public static List<LocalSongDto> getAllSongs(@NonNull ContentResolver resolver) {
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                LocalSongDto.MUSIC_COLUMNS,
                MediaStore.Audio.AudioColumns.IS_MUSIC + " = ?",
                new String[] {String.valueOf(1)}, // 1 stand for true
                MediaStore.Audio.AudioColumns.DATE_MODIFIED + " ASC");
        List<LocalSongDto> songs = new ArrayList<>();
        if(!Utils.checkNull(cursor) && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while(cursor.moveToNext())
                songs.add(LocalSongDto.createFromCursor(cursor));
        }

        return songs;
    }
}
