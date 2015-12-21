package crazysheep.io.materialmusic.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.AccelerateInterpolator;

import crazysheep.io.materialmusic.widget.SimpleAnimatorListener;

/**
 * fab alpha animator at {@link crazysheep.io.materialmusic.fragment.PlaybackFragment}
 *
 * Created by crazysheep on 15/12/21.
 */
public class FabAlphaDirector {

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
        private int mStartDrawableRes;
        private int mEndDrawableRes;
        private OnAnimationListener mListener;

        public Builder(@NonNull FloatingActionButton fab, @DrawableRes int startRes,
                       @DrawableRes int endRes) {
            mFab = fab;
            mStartDrawableRes = startRes;
            mEndDrawableRes = endRes;
        }

        public Builder setListener(OnAnimationListener listener) {
            mListener = listener;

            return this;
        }

        public void animate() {
            // alpha animator
            alphaAnimator(0, 30, new SimpleAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mListener != null)
                        mListener.onAnimationStart();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    to();
                }
            });
        }

        public void reverse() {
            alphaAnimator(0, 100, new SimpleAnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if(mListener != null)
                        mListener.onAnimationStart();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    back();
                }
            });
        }

        private void to() {
            mFab.setImageResource(mEndDrawableRes);

            alphaAnimator(255, 150, new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mListener != null)
                        mListener.onAnimationEnd();
                }
            });
        }

        private void back() {
            mFab.setImageResource(mStartDrawableRes);

            alphaAnimator(255, 150, new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(mListener != null)
                        mListener.onAnimationEnd();
                }
            });
        }

        private void alphaAnimator(int alpha, int duration, Animator.AnimatorListener listener) {
            // see{@link http://stackoverflow.com/questions/2902222/apply-an-animation-on-a-drawable-in-android}
            Drawable fabDrawable = mFab.getDrawable();
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabDrawable,
                    PropertyValuesHolder.ofInt("alpha", alpha));
            animator.setTarget(fabDrawable);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(duration);
            animator.addListener(listener);
            animator.start();
        }

    }
}
