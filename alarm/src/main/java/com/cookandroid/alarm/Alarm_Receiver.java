package com.cookandroid.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.POWER_SERVICE;

public class Alarm_Receiver extends BroadcastReceiver {
    Context context;
    PowerManager powerManager;

    PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String a = intent.getStringExtra("state");
        String b= intent.getStringExtra("content");
        if (a.contentEquals("alarm on"))
            powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "My Tag:LOCK");
            long[] pattern = {1000, 50, 1000, 50, 1000, 50};
            NotificationChannel mChannel;
            String channelId = "channel";
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(channelId, channelName, importance);
            mChannel.enableVibration(true);
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationmanager.createNotificationChannel(mChannel);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, mChannel.getId());
            builder.setSmallIcon(R.drawable.ic_launcher_foreground).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setNumber(1).setContentTitle(b).setContentText("먹을시간!").setWhen(System.currentTimeMillis())
                    .setLights(Color.BLUE, 1, 1).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).setVibrate(pattern).setContentIntent(pendingIntent).setAutoCancel(true);
            Notification note = builder.build();
            note.flags |= Notification.FLAG_INSISTENT;
            wakeLock.acquire(5000);
            notificationmanager.notify(1, note);
            wakeLock.release();
            Intent schedule = new Intent(context, Alarm_Receiver.class);
            schedule.putExtra("state", "alarm on");
            int i= intent.getIntExtra("id",-1);
            schedule.putExtra("id",i);
            PendingIntent sender = PendingIntent.getBroadcast(context, i, schedule, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar interval = Calendar.getInstance();
            interval.setTimeInMillis(System.currentTimeMillis());
            interval.set(Calendar.SECOND,0);
            interval.add(Calendar.DAY_OF_MONTH, 1);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,interval.getTimeInMillis(), sender);
        }
    }

