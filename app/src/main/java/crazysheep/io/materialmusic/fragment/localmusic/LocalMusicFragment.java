package crazysheep.io.materialmusic.fragment.localmusic;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import crazysheep.io.materialmusic.service.MusicService;
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

    private boolean isServiceBind = false;
    private MusicService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBind = true;
            mService = ((MusicService.MusicBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBind = false;
            mService = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_local_music, container, false);
        ButterKnife.bind(this, contentView);

        requestStoragePermission();
        initUI();

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // start music service, init current playlist in service
        Intent serviceIntent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(isServiceBind)
            getActivity().unbindService(mConnection);
        // TODO if is not playing current, stop service
        if(mService.isIdle())
            getActivity().stopService(new Intent(getActivity(), MusicService.class));
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
