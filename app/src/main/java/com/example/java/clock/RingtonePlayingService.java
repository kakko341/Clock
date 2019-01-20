package com.example.java.clock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class RingtonePlayingService extends Service {

    NotificationManager notify_manager;
    MediaPlayer media_song;
    int startId;
    Boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("LocalService", "received start id" + startId + ":" + intent);

        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone extra is", state);

        notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent_main_activity = new Intent(this.getApplicationContext(),
                Alarm.class);

        PendingIntent pendingIntent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("アラームを解除します。")
                .setContentText("クリックしてください。")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent_main_activity)
                .setAutoCancel(true)
                .build();

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                Log.e("there is no music", "and you want to start");
                media_song = MediaPlayer.create(this, R.raw.crow1);
                media_song.start();
                notify_manager.notify(1, notification_popup);
                break;
            case "alarm off":
                startId = 0;
                Log.e("there is music", "and you want to end");
                media_song.stop();
                media_song.reset();
                Log.e("Start ID is", state);
                break;
            default:
                startId = 0;
                break;
        }

        return START_NOT_STICKY;

    }

        @Override
        public void onDestroy () {
            Log.e("on Destroy called", "ta da");
            super.onDestroy();
            this.isRunning = false;
        }

}

