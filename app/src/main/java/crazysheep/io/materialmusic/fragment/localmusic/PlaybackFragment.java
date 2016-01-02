package crazysheep.io.materialmusic.fragment.localmusic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.service.BaseMusicService;
import crazysheep.io.materialmusic.utils.Utils;
import crazysheep.io.materialmusic.widget.PlayOrPauseImageButton;
import de.greenrobot.event.EventBus;

/**
 * playback fragment for local music
 *
 * Created by crazysheep on 16/1/2.
 */
public class PlaybackFragment extends BaseFragment {

    @Bind(R.id.song_cover_iv) ImageView mBigCoverIv;
    @Bind(R.id.song_previous_ib) ImageButton mPreviousBtn;
    @Bind(R.id.song_next_ib) ImageButton mNextBtn;
    @Bind(R.id.song_play_or_pause_ib) PlayOrPauseImageButton mPlayOrPauseBtn;

    public static final String EXTRA_SONG = "extra_song";

    private ISong mCurSong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_local_music_playback,
                container, false);
        ButterKnife.bind(this, contentView);

        mCurSong = getArguments().getParcelable(EXTRA_SONG);

        updateUI();

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    private void updateUI() {
        if(!TextUtils.isEmpty(mCurSong.getCover())) {
            Uri uri = mCurSong.isLocal()
                    ? Uri.fromFile(new File(mCurSong.getCover())) : Uri.parse(mCurSong.getCover());
            Picasso.with(getActivity())
                    .load(uri)
                    .into(mBigCoverIv);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull BaseMusicService.EventCurrentSong event) {
        if(Utils.checkNull(mCurSong) || !event.currentSong.getUrl().equals(mCurSong.getUrl())) {
            mCurSong = event.currentSong;
            updateUI();
        }
    }

}
