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
import crazysheep.io.materialmusic.constants.MusicConstants;

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
        PendingIntent playOrPause = PendingIntent.getBroadcast(context, 0,
                new Intent(isPlaying ? MusicConstants.ACTION_PAUSE : MusicConstants.ACTION_PLAY),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // big layout
        RemoteViews bigLayout = new RemoteViews(context.getPackageName(),
                R.layout.layout_music_notify_big);
        bigLayout.setTextViewText(R.id.song_name_tv, song.getName());
        bigLayout.setTextViewText(R.id.song_artist_tv, song.getArtist());
        bigLayout.setOnClickPendingIntent(R.id.song_next_iv,
                PendingIntent.getBroadcast(context, 0, new Intent(MusicConstants.ACTION_NEXT),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        bigLayout.setOnClickPendingIntent(R.id.song_previous_iv,
                PendingIntent.getBroadcast(context, 0, new Intent(MusicConstants.ACTION_PREVIOUS),
                        PendingIntent.FLAG_CANCEL_CURRENT));
        bigLayout.setImageViewResource(R.id.song_play_iv,
                isPlaying ? R.drawable.ic_pause_circle_outline : R.drawable.ic_play_circle_outline);
        bigLayout.setOnClickPendingIntent(R.id.song_play_iv, playOrPause);
        bigLayout.setOnClickPendingIntent(R.id.song_stop_iv,
                PendingIntent.getBroadcast(context, 0, new Intent(MusicConstants.ACTION_STOP),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        bigLayout.setImageViewResource(R.id.song_cover_iv, R.drawable.place_holder);

        // small layout
        RemoteViews smallLayout = new RemoteViews(context.getPackageName(),
                R.layout.layout_music_notify_small);
        smallLayout.setTextViewText(R.id.song_name_tv, song.getName());
        smallLayout.setTextViewText(R.id.song_artist_tv, song.getArtist());
        smallLayout.setOnClickPendingIntent(R.id.song_next_iv,
                PendingIntent.getBroadcast(context, 0, new Intent(MusicConstants.ACTION_NEXT),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        smallLayout.setImageViewResource(R.id.song_play_iv,
                isPlaying ? R.drawable.ic_pause_circle_outline : R.drawable.ic_play_circle_outline);
        smallLayout.setOnClickPendingIntent(R.id.song_play_iv,
                playOrPause);
        smallLayout.setImageViewResource(R.id.song_cover_iv, R.drawable.place_holder);

        Intent launcherIntent = new Intent(context, MainActivity.class);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentPI = PendingIntent.getActivity(context, notifyId,
                launcherIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int largeIconSize = (int)Utils.dp2px(context.getResources(), 64);
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setTicker(context.getText(R.string.app_name))
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(smallLayout)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(contentPI)
                .setDeleteIntent(PendingIntent.getBroadcast(context, 0,
                        new Intent(MusicConstants.ACTION_STOP), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(song.getName())
                .setContentText(song.getArtist())
                .setOngoing(true);

        Notification notify = builder.build();
        notify.bigContentView = bigLayout;

        // load song cover
        if(!TextUtils.isEmpty(song.getCover())) {
            Uri uri = song.isLocal() ? Uri.fromFile(new File(song.getCover()))
                    : Uri.parse(song.getCover());

            Picasso.with(context)
                    .load(uri)
                    .resize(largeIconSize, largeIconSize)
                    .error(R.drawable.place_holder)
                    .into(notify.contentView, R.id.song_cover_iv, notifyId, notify);
            Picasso.with(context)
                    .load(uri)
                    .resize(largeIconSize, largeIconSize)
                    .error(R.drawable.place_holder)
                    .into(notify.bigContentView, R.id.song_cover_iv, notifyId, notify);
        }

        return notify;
    }

}
