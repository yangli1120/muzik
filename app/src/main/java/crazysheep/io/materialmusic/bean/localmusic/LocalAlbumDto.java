package crazysheep.io.materialmusic.bean.localmusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import crazysheep.io.materialmusic.db.MediaStoreHelper;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * local album
 *
 * Created by crazysheep on 15/12/28.
 */
@ParcelablePlease
public class LocalAlbumDto implements Parcelable {

    public static final String[] ALBUM_COLUMNS = new String[]{
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    };

    public static LocalAlbumDto createFromCursor(@NonNull Cursor cursor) {
        LocalAlbumDto albumDto = new LocalAlbumDto();

        albumDto.album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        albumDto.album_cover = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

        return albumDto;
    }

    /**
     * get first song's cover of this album
     * */
    public String getAlbumCover(@NonNull ContentResolver resolver) {
        if(!TextUtils.isEmpty(album_cover))
            return album_cover;
        else if(!Utils.checkNull(songs)
                && songs.size() > 0
                && !Utils.checkNull(songs.get(0)))
            return album_cover = MediaStoreHelper.getAlbumCover(resolver, songs.get(0).album_id);

        return null;
    }

    public List<LocalSongDto> songs;
    public String album_name;
    public String album_cover;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        LocalAlbumDtoParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<LocalAlbumDto> CREATOR = new Creator<LocalAlbumDto>() {
        public LocalAlbumDto createFromParcel(Parcel source) {
            LocalAlbumDto target = new LocalAlbumDto();
            LocalAlbumDtoParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public LocalAlbumDto[] newArray(int size) {
            return new LocalAlbumDto[size];
        }
    };
}
