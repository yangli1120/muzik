package crazysheep.io.materialmusic.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import crazysheep.io.materialmusic.bean.localmusic.LocalAlbumDto;
import crazysheep.io.materialmusic.bean.localmusic.LocalArtistDto;
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
                MediaStore.Audio.AudioColumns.DATE_MODIFIED + " DESC");
        List<LocalSongDto> songs = new ArrayList<>();
        if(!Utils.checkNull(cursor) && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()) {
                LocalSongDto songDto = LocalSongDto.createFromCursor(cursor);
                songDto.album_cover = getAlbumCover(resolver, songDto.album_id);
                songs.add(songDto);
            }

            cursor.close();
        }

        return songs;
    }

    /**
     * query system media store, get all artists
     * */
    public static List<LocalArtistDto> getAllArtist(@NonNull ContentResolver resolver) {
        Cursor cursor = resolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                LocalArtistDto.ARTIST_COLUMNS, null, null,
                MediaStore.Audio.Artists.ARTIST + " ASC");

        List<LocalArtistDto> artistDtos = new ArrayList<>();
        if(!Utils.checkNull(cursor) && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                LocalArtistDto artistDto = LocalArtistDto.createFromCursor(cursor);
                // find a no-null cover or until iterator done
                for(LocalAlbumDto album : getAlbumsOfArtist(resolver, artistDto.artist_name))
                    if(!TextUtils.isEmpty(album.album_cover)) {
                        artistDto.artist_first_album_cover = album.album_cover;
                        break;
                    }

                artistDtos.add(artistDto);
            }

            cursor.close();
        }

        return artistDtos;
    }

    /**
     * get all albums on external storage
     * */
    public static List<LocalAlbumDto> getAllAlbums(@NonNull ContentResolver resolver) {
        Cursor cursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                LocalAlbumDto.ALBUM_COLUMNS, null, null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        List<LocalAlbumDto> albumDtos = new ArrayList<>();
        if(!Utils.checkNull(cursor) && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
                albumDtos.add(LocalAlbumDto.createFromCursor(cursor));

            cursor.close();
        }

        return albumDtos;
    }

    /**
     * get all albums of target artist, but BE CAREFUL! Without query songs of single album
     * */
    private static List<LocalAlbumDto> getAlbumsOfArtist(@NonNull ContentResolver resolver,
                                                        String artist) {
        Cursor cursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                LocalAlbumDto.ALBUM_COLUMNS,
                MediaStore.Audio.Albums.ARTIST + " = ?",
                new String[] {artist},
                MediaStore.Audio.Albums.ALBUM + " ASC");

        List<LocalAlbumDto> albums = new ArrayList<>();
        if(!Utils.checkNull(cursor) && cursor.getCount() > 0) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
                albums.add(LocalAlbumDto.createFromCursor(cursor));

            cursor.close();
        }

        return albums;
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
