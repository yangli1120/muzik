package crazysheep.io.materialmusic.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import java.io.File;

import crazysheep.io.materialmusic.MainActivity;
import crazysheep.io.materialmusic.R;
import crazysheep.io.materialmusic.bean.ISong;

/**
 * notification utils
 *
 * Created by crazysheep on 15/12/31.
 */
public class NotifyUtils {

    /**
     * make notification for music, the Notification API is shit
     * */
    public static Notification buildWithSong(@NonNull Context context, @NonNull ISong song,
                                             int notifyId, boolean isPlaying) {
        // big layout
        RemoteViews bgLayout = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.layout_music_notify_big);
        bgLayout.setTextViewText(R.id.song_name_tv, song.getName());
        bgLayout.setTextViewText(R.id.song_artist_tv, song.getArtist());
        bgLayout.setImageViewResource(R.id.song_cover_iv, R.drawable.place_holder);
        bgLayout.setImageViewResource(R.id.song_play_iv, isPlaying
                ? R.drawable.ic_pause_circle_outline : R.drawable.ic_play_circle_outline);

        // small layout
        RemoteViews smallLayout = new RemoteViews(context.getPackageName(),
                R.layout.layout_music_notify_small);
        smallLayout.setTextViewText(R.id.song_name_tv, song.getName());
        smallLayout.setTextViewText(R.id.song_artist_tv, song.getArtist());
        smallLayout.setImageViewResource(R.id.song_cover_iv, R.drawable.place_holder);
        smallLayout.setImageViewResource(R.id.song_play_iv, isPlaying
                ? R.drawable.ic_play_circle_outline : R.drawable.ic_pause_circle_outline);

        Intent launcherIntent = new Intent(context, MainActivity.class);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentPI = PendingIntent.getActivity(context, notifyId,
                launcherIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context)
                .setPriority(Notification.PRIORITY_HIGH)
                .setTicker(context.getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(smallLayout)
                .setContentIntent(contentPI)
                .setOngoing(true);

        Notification notify = builder.build();
        notify.bigContentView = bgLayout;

        // load song cover
        if(!TextUtils.isEmpty(song.getCover())) {
            Uri uri = song.isLocal() ? Uri.fromFile(new File(song.getCover()))
                    : Uri.parse(song.getCover());
            Picasso.with(context)
                    .load(uri)
                    .error(R.drawable.place_holder)
                    .into(smallLayout, R.id.song_cover_iv, notifyId, notify);
            Picasso.with(context)
                    .load(uri)
                    .error(R.drawable.place_holder)
                    .into(bgLayout, R.id.song_cover_iv, notifyId, notify);
        }

        return notify;
    }

}
