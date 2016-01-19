package crazysheep.io.materialmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.fragment.BaseFragment;
import crazysheep.io.materialmusic.fragment.FmPlaybackFragment;
import crazysheep.io.materialmusic.fragment.localmusic.LocalMusicFragment;
import crazysheep.io.materialmusic.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.nav_view) NavigationView mNavView;

    private LocalMusicFragment mMuzikFt;
    private BaseFragment mCurFt;

    @Override
    protected int getCurrentTheme() {
        return R.style.AppTheme_Night;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUI();
    }

    public void setToolbar(@NonNull Toolbar toolbar) {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initUI() {
        mNavView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_fl, new LocalMusicFragment(), FmPlaybackFragment.TAG)
                .commitAllowingStateLoss();
        mNavView.getMenu().findItem(R.id.nav_local).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.nav_search: {
                // TODO show search fragment
            }break;

            case R.id.nav_local: {
                if(mCurFt == mMuzikFt)
                    break;

                if(Utils.checkNull(mMuzikFt))
                    mMuzikFt = new LocalMusicFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_fl, mMuzikFt, LocalMusicFragment.TAG)
                        .commitAllowingStateLoss();
                mCurFt = mMuzikFt;
            }break;
        }

        Observable.just(true)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mDrawer.closeDrawer(GravityCompat.START);
                    }
                });

        return true;
    }
}
