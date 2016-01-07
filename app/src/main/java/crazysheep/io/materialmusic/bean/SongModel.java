package crazysheep.io.materialmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * song model
 *
 * Created by crazysheep on 16/1/4.
 */
@ParcelablePlease
@Table(name = "songs")
public class SongModel extends BaseModel implements ISong {

    public static final String NAME = "name";
    public static final String SONG_ID = "song_id";
    public static final String URL = "url";
    public static final String COVER = "cover";
    public static final String ARTIST = "artist";
    public static final String ARTIST_ID = "artist_id";
    public static final String ALBUM = "album";
    public static final String ALBUM_ID = "album_id";
    public static final String IS_LOCAL = "is_local";

    @Column(name = NAME)
    public String name;

    @Column(name = SONG_ID)
    public long songId; // media id in android system media store

    @Column(name = URL)
    public String url;

    @Column(name = COVER)
    public String cover;

    @Column(name = ARTIST)
    public String artist;

    @Column(name = ARTIST_ID)
    public long artistId; // artist id in android system media store

    @Column(name = ALBUM)
    public String album;

    @Column(name = ALBUM_ID)
    public long albumId; // artist id in android system media store

    @Column(name = IS_LOCAL)
    public boolean isLocal;

    public SongModel() {
        super();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getCover() {
        return cover;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SongModelParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        public SongModel createFromParcel(Parcel source) {
            SongModel target = new SongModel();
            SongModelParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
