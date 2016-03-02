package crazysheep.io.materialmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * model for playlist
 *
 * Created by crazysheep on 16/1/4.
 */
@ParcelablePlease
@Table(name = "playlists")
public class PlaylistModel extends BaseModel implements Parcelable, IPlaylist {

    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String PLAYLIST_COVER = "playlist_cover";
    public static final String SONG_COUNT = "song_count";
    public static final String CREATED_AT = "created_at";

    @Column(name = PLAYLIST_NAME, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String playlist_name;

    @Column(name = PLAYLIST_COVER)
    public String playlist_cover;

    @Column(name = SONG_COUNT)
    public int song_count;

    @Column(name = CREATED_AT)
    public long created_at;

    public PlaylistModel() {
        super();
    }

    public PlaylistModel(@NonNull String playlist_name, long created_at) {
        super();
        this.playlist_name = playlist_name;
        this.created_at = created_at;
    }

    public boolean isEditable = true; // if playlist can been edited

    @Override
    public String getAvatar() {
        return playlist_cover;
    }

    @Override
    public String getPlaylistName() {
        return playlist_name;
    }

    ////////////////////////////// parcelable //////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        PlaylistModelParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<PlaylistModel> CREATOR = new Creator<PlaylistModel>() {
        public PlaylistModel createFromParcel(Parcel source) {
            PlaylistModel target = new PlaylistModel();
            target.setAaId(source.readLong());
            PlaylistModelParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public PlaylistModel[] newArray(int size) {
            return new PlaylistModel[size];
        }
    };

    @Override
    public String toString() {
        return playlist_name;
    }
}
