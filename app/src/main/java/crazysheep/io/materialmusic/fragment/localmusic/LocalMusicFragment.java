package crazysheep.io.materialmusic.fragment.localmusic;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.MainActivity;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.adapter.MusicPagerAdapter;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.utils.Utils;

/**
 * show local music on external storage
 *
 * Created by crazysheep on 15/12/28.
 */
public class LocalMusicFragment extends BaseFragment {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tabs) TabLayout mTabLayout;
    @Bind(R.id.content_vp) ViewPager mContentVp;
    private MusicPagerAdapter mMusicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_local_music, container, false);
        ButterKnife.bind(this, contentView);

        requestStoragePermission();
        initUI();

        return contentView;
    }

    private void requestStoragePermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                    }

                    @Override
                    public void onDenied(String permission) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void initUI() {
        ((MainActivity)getActivity()).setToolbar(mToolbar);
        if(!Utils.checkNull(getSupportActionBar())) {
            getSupportActionBar().setTitle(getString(R.string.tv_local_music_title));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mMusicAdapter = new MusicPagerAdapter(getFragmentManager(), getActivity());
        mContentVp.setAdapter(mMusicAdapter);
        mContentVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mContentVp);
        mContentVp.setOffscreenPageLimit(mMusicAdapter.getCount());
    }

}
