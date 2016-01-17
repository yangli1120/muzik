package crazysheep.io.materialmusic;

import android.os.Bundle;

import crazysheep.io.materialmusic.fragment.localmusic.PlaybackFragment;

/**
 * playback activity
 *
 * Created by crazysheep on 16/1/17.
 */
public class PlaybackActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_ft, new PlaybackFragment(), PlaybackFragment.TAG)
                .commitAllowingStateLoss();
    }
}
