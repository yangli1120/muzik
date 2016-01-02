package crazysheep.io.materialmusic.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageButton;

import crazysheep.io.materialmusic.R;

/**
 * ImageButton switch playList or pause state of music
 *
 * Created by crazysheep on 16/1/2.
 */
public class PlayOrPauseImageButton extends ImageButton {

    private static final int RES_PLAY = R.drawable.ic_play_circle_outline;
    private static final int RES_PAUSE = R.drawable.ic_pause_circle_outline;

    private int mPlayRes = RES_PLAY;
    private int mPauseRes = RES_PAUSE;

    public PlayOrPauseImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlayOrPauseImageButton(Context context) {
        super(context);
    }

    public PlayOrPauseImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayOrPauseImageButton(Context context, AttributeSet attrs, int defStyleAttr,
                                  int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setResources(@DrawableRes int playRes, @DrawableRes int pauseRes) {
        mPlayRes = playRes;
        mPauseRes = pauseRes;
    }

    public void toggle(boolean play) {
        if(play)
            setImageResource(mPauseRes);
        else
            setImageResource(mPlayRes);
    }

}
