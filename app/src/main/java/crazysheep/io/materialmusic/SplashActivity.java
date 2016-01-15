package crazysheep.io.materialmusic;

import android.Manifest;
import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import butterknife.Bind;
import butterknife.ButterKnife;
import crazysheep.io.materialmusic.utils.ActivityUtils;

/**
 * splash activity
 *
 * Created by crazysheep on 15/12/18.
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_logo_tv) TextView mLogoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        mLogoTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLogoTv.getViewTreeObserver().removeOnPreDrawListener(this);

                mLogoTv.setAlpha(0.2f);
                mLogoTv.setTranslationY(300f);
                mLogoTv.animate()
                        .alpha(1f)
                        .translationY(-300f)
                        .setDuration(1000)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                requestStoragePermission();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        })
                        .start();

                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void goMain() {
        ActivityUtils.start(this, MainActivity.class);
        finish();
    }

    private void requestStoragePermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        goMain();
                    }

                    @Override
                    public void onDenied(String permission) {
                        finish();
                    }
                });
    }

}
