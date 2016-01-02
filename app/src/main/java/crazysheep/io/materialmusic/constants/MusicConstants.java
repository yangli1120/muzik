package crazysheep.io.materialmusic.constants;

/**
 * contains music action constants
 *
 * Created by crazysheep on 16/1/1.
 */
public class MusicConstants extends Constants {

    //////////////// notification action ////////////////////////////////////

    public static final String ACTION_PREVIOUS = PACKAGE_NAME + ".action_previous"; // previous song
    public static final String ACTION_PAUSE = PACKAGE_NAME + ".action_pause"; // pause
    public static final String ACTION_PLAY = PACKAGE_NAME + ".action_play"; // play
    public static final String ACTION_NEXT = PACKAGE_NAME + ".action_next"; // next song
    public static final String ACTION_STOP = PACKAGE_NAME + ".action_stop"; // stop music service

    //////////////// play mode ///////////////////////////////////

    public static final int PLAY_ORDER = 0;
    public static final int PLAY_LOOP_ONE = 1;
    public static final int PLAY_LOOP_ALL = 2;
    public static final int PLAY_SHUFFLE = 3;

}
