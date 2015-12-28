package crazysheep.io.materialmusic.bean.localmusic;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import crazysheep.io.materialmusic.utils.SystemMediaUtils;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * local album
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalAlbumDto {

    /**
     * get first song's cover of this album
     * */
    public String getAlbumCover(@NonNull ContentResolver resolver) {
        if(!TextUtils.isEmpty(album_cover))
            return album_cover;
        else if(!Utils.checkNull(songs)
                && songs.size() > 0
                && !Utils.checkNull(songs.get(0)))
            return album_cover = SystemMediaUtils.getAlbumCover(resolver, songs.get(0).album_id);

        return null;
    }

    public List<LocalSongDto> songs;
    public String album_name;
    public String album_cover;
}
