package crazysheep.io.materialmusic.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

import crazysheep.io.materialmusic.R;

/**
 * ImageButton contains play mode icons
 *
 * Created by crazysheep on 16/1/15.
 */
public class PlayModeImageButton extends ImageButton {

    private static int RES_LOOP = R.drawable.ic_loop_black;
    private static int RES_LOOP_ONE = R.drawable.ic_loop_one_black;
    private static int RES_SHUFFLE = R.drawable.ic_shuffle_black;

    public PlayModeImageButton(Context context) {
        super(context);
    }

    public PlayModeImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayModeImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayModeImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loop() {
        setImageResource(RES_LOOP);
    }

    public void loopOne() {
        setImageResource(RES_LOOP_ONE);
    }

    public void shuffle() {
        setImageResource(RES_SHUFFLE);
    }

}
