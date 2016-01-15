package crazysheep.io.materialmusic.prefs;

import android.content.Context;

import crazysheep.io.materialmusic.constants.MusicConstants;

/**
 * save last playlist
 *
 * Created by crazysheep on 16/1/13.
 */
public class PlaylistPrefs extends BasePrefs {

    public static final String PREFS_NAME = "playlist_prefs";

    public static final String KEY_LAST_PLAYLIST = "last_playlist";
    public static final String KEY_LAST_PLAY_MODE = "last_play_mode";
    public static final String KEY_LAST_PLAY_SONG_ID = "last_play_song_id";

    public PlaylistPrefs(Context context) {
        super(context);
    }

    public void setLastPlaylist(String lastPlaylistName) {
        setString(KEY_LAST_PLAYLIST, lastPlaylistName);
    }

    public String getLastPlaylist() {
        return getString(KEY_LAST_PLAYLIST, null);
    }

    /**
     * see{@link crazysheep.io.materialmusic.constants.MusicConstants}
     * */
    public void setLastPlayMode(int mode) {
        setInt(KEY_LAST_PLAY_MODE, mode);
    }

    public int getLastPlayMode() {
        return getInt(KEY_LAST_PLAY_MODE, MusicConstants.PLAY_LOOP_ALL);
    }

    public void setLastPlaySong(long songId) {
        setLong(KEY_LAST_PLAY_SONG_ID, songId);
    }

    public long getLastPlaySong() {
        return getLong(KEY_LAST_PLAY_SONG_ID, -1);
    }

}
