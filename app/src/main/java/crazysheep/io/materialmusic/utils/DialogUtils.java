package crazysheep.io.materialmusic.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

/**
 * dialog utils
 * <p/>
 * Created by crazysheep on 16/1/4.
 */
public class DialogUtils {

    public interface SingleChoiceCallback {
        void onItemClick(int position);
    }

    public interface EditDoneCallback {
        void onEditDone(String editableString);
    }

    public interface ButtonAction {
        String getTitle();
        void onClick(DialogInterface dialog);
    }

    public static Dialog showConfirmDialog(@NonNull Activity activity, String title, String content,
                                           final ButtonAction okAction) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .positiveText(okAction.getTitle())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        dismissDialog(dialog);
                        okAction.onClick(dialog);
                    }
                })
                .build();
        dialog.setOwnerActivity(activity);

        if (dialog.getOwnerActivity() != null
                && !dialog.getOwnerActivity().isFinishing())
            dialog.show();

        return dialog;
    }

    /**
     * show a item list dialog
     */
    public static Dialog showListDialog(@NonNull Activity activity, String title,
                                        @NonNull List<String> choices,
                                        @NonNull final SingleChoiceCallback callback) {
        Dialog dialog = new MaterialDialog.Builder(activity)
                .title(title)
                .items(choices.toArray(new String[choices.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        dismissDialog(dialog);

                        callback.onItemClick(which);
                    }
                })
                .build();
        showDialog(activity, dialog);

        return dialog;
    }

    /**
     * show a edit dialog
     * */
    public static Dialog showEditDialog(@NonNull Activity activity, String title, String hint,
                                        @NonNull final EditDoneCallback doneCallback) {
        Dialog dialog = new MaterialDialog.Builder(activity)
                .title(title)
                .input(hint, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        dismissDialog(dialog);

                        doneCallback.onEditDone(input.toString());
                    }
                })
                .positiveText("OK")
                .build();
        showDialog(activity, dialog);

        return dialog;
    }

    /**
     * show dialog safety
     */
    public static void showDialog(@NonNull Activity activity, @NonNull Dialog dialog) {
        dialog.setOwnerActivity(activity);
        if (!Utils.checkNull(dialog.getOwnerActivity()) && !dialog.getOwnerActivity().isFinishing())
            dialog.show();
    }

    /**
     * dismiss dialog safety
     */
    public static void dismissDialog(@NonNull Dialog dialog) {
        if (!Utils.checkNull(dialog.getOwnerActivity()) && !dialog.getOwnerActivity().isFinishing())
            dialog.dismiss();
    }
}
