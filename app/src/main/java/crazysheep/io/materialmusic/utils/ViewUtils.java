package crazysheep.io.materialmusic.utils;

import android.view.View;

/**
 * view utils
 *
 * Created by crazysheep on 15/11/29.
 */
public class ViewUtils {

    public static int getRelativeLeft(View view) {
        if (view.getParent() == view.getRootView())
            return view.getLeft();
        else
            return view.getLeft() + getRelativeLeft((View) view.getParent());
    }

    public static int getRelativeTop(View view) {
        if (view.getParent() == view.getRootView())
            return view.getTop();
        else
            return view.getTop() + getRelativeTop((View) view.getParent());
    }
}
