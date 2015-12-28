package crazysheep.io.materialmusic.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * system media utils
 *
 * Created by crazysheep on 15/12/28.
 */
public class SystemMediaUtils {

    /**
     * get album cover from system media store, block thread, do not use in UI thread
     * */
    public static Bitmap getAlbumCover(@NonNull Context context, long albumId) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
        ContentResolver res = context.getContentResolver();
        try {
            InputStream in = res.openInputStream(uri);
            return BitmapFactory.decodeStream(in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    /**
     * get album cover image path
     * */
    public static String getAlbumCover(@NonNull ContentResolver resolver, long albumId) {
        Cursor cursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
                null);

        if(!Utils.checkNull(cursor)) {
            cursor.moveToFirst();
            String coverPath = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            cursor.close();

            return coverPath;
        }

        return null;
    }
}
