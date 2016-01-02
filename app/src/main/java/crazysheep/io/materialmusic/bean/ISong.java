package crazysheep.io.materialmusic.bean;

import android.os.Parcelable;

/**
 * interface definition base song data structure
 *
 * Created by crazysheep on 15/12/30.
 */
public interface ISong extends Parcelable {

    String getName();
    String getUrl();
    String getCover();
    String getArtist();
    boolean isLocal();
}
