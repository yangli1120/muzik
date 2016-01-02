package crazysheep.io.materialmusic.animator;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.nineoldandroids.animation.Animator;

import crazysheep.io.materialmusic.fragment.FmPlaybackFragment;
import crazysheep.io.materialmusic.utils.SystemUIHelper;
import crazysheep.io.materialmusic.utils.ViewUtils;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;

/**
 * direct the playback reveal animator at {@link FmPlaybackFragment}
 *
 * Created by crazysheep on 15/12/20.
 */
public class PlaybackDirector {

    public interface OnAnimationListener {
        void onAnimationStart();
        void onAnimationEnd();
    }

    public static class SimpleAnimationListener implements OnAnimationListener {
        @Override
        public void onAnimationStart() {
        }

        @Override
        public void onAnimationEnd() {
        }
    }

    public static class Builder {
        private FloatingActionButton mFab;
        private View mAnchorView;
        private View mRevealView;

        private OnAnimationListener mListener;

        public Builder(@NonNull FloatingActionButton fab, @NonNull View fabAnchor,
                       @NonNull View revealView) {
            mFab = fab;
            mAnchorView = fabAnchor;
            mRevealView = revealView;
        }

        public Builder setListener(OnAnimationListener listener) {
            mListener = listener;

            return this;
        }

        public void expand() {
            ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(mFab,
                    ViewUtils.getRelativeLeft(mAnchorView) + mAnchorView.getWidth() / 2,
                    ViewUtils.getRelativeTop(mAnchorView) + mAnchorView.getHeight() / 2
                            - SystemUIHelper.getToolbarSize(mFab.getContext())
                            - SystemUIHelper.getStatusBarSize(mFab.getContext()),
                    360,
                    Side.RIGHT);
            arcAnimator.setDuration(150);
            arcAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            arcAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if(mListener != null)
                        mListener.onAnimationStart();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mFab.setVisibility(View.GONE);
                    mRevealView.setVisibility(View.VISIBLE);

                    SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView,
                            getAnchorCenterXRelativeParent(),
                            getAnchorCenterYRelativeParent(),
                            (float) Math.hypot(mFab.getWidth() / 2, mFab.getHeight() / 2),
                            (float) Math.hypot(mRevealView.getWidth() / 2, mRevealView.getHeight() / 2));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(150);
                    animator.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {
                        }

                        @Override
                        public void onAnimationEnd() {
                            if(mListener != null)
                                mListener.onAnimationEnd();
                        }

                        @Override
                        public void onAnimationCancel() {
                        }

                        @Override
                        public void onAnimationRepeat() {
                        }
                    });
                    animator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            arcAnimator.start();
        }

        public void close() {
            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView,
                    getAnchorCenterXRelativeParent(),
                    getAnchorCenterYRelativeParent(),
                    (float) Math.hypot(mRevealView.getWidth(), mRevealView.getHeight()),
                    (float) Math.hypot(mFab.getWidth() / 2, mFab.getHeight() / 2));
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(150);
            animator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                    if(mListener != null)
                        mListener.onAnimationStart();
                }

                @Override
                public void onAnimationEnd() {
                    mRevealView.setVisibility(View.GONE);
                    mFab.setVisibility(View.VISIBLE);

                    ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(mFab,
                            mFab.getX() - mFab.getTranslationX() + mFab.getWidth() / 2,
                            mFab.getY() - mFab.getTranslationY() + mFab.getHeight() / 2,
                            360,
                            Side.RIGHT);
                    arcAnimator.setInterpolator(new AccelerateInterpolator());
                    arcAnimator.setDuration(150);
                    arcAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(mListener != null)
                                mListener.onAnimationEnd();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    arcAnimator.start();
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
            animator.start();
        }

        private int getAnchorCenterXRelativeParent() {
            int anchorCenterX = mAnchorView.getLeft() + mAnchorView.getWidth() / 2;
            return Math.round(anchorCenterX * 1f / mRevealView.getWidth() * mRevealView.getWidth());
        }

        private int getAnchorCenterYRelativeParent() {
            int anchorCenterY = mAnchorView.getTop() + mAnchorView.getHeight() / 2;
            return Math.round(anchorCenterY * 1f / mRevealView.getHeight() * mRevealView.getHeight());
        }

    }
}
