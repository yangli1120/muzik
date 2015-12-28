package crazysheep.io.materialmusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import crazysheep.io.materialmusic.fragment.PlaybackFragment;
import crazysheep.io.materialmusic.fragment.PlaylistFragment;

/**
 * view pager adapter for LocalMusicAdapter
 *
 * Created by crazysheep on 15/12/28.
 */
public class MusicPagerAdapter extends FragmentPagerAdapter {

    private static String[] mTitles = new String[] {"播放列表", "流派", "音乐人", "专辑", "歌曲"};
    @SuppressWarnings("unchecked")
    private static Class<? extends Fragment>[] mFts = new Class[] {
            PlaylistFragment.class, PlaybackFragment.class,
            PlaybackFragment.class, PlaybackFragment.class,
            PlaybackFragment.class
    };

    private Context mContext;

    public MusicPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mContext, mFts[position].getName());
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
