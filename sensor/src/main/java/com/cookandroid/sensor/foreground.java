package com.cookandroid.sensor;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;


import java.util.Calendar;


public class foreground extends Service implements SensorEventListener {
    private SensorManager sensorM;
    private Sensor sensorC;

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 400;
    static NotificationCompat.Builder builder;
    DataBase db;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate() {
        unregisterRestartAlarm();
        super.onCreate();
        db = new DataBase(getApplicationContext(), "STEP.db", null, 1);
        sensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorC = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorM.registerListener(this, sensorC, sensorM.SENSOR_DELAY_NORMAL);

    }
    @Override
    public int onStartCommand(Intent intent,int flag,int start_id) {
        NotificationChannel mChannel;
        String channelId = "channel1";
        String channelName = "Channel Name";
        mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        mChannel.enableVibration(false);
        mChannel.setSound(null,null);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationmanager.createNotificationChannel(mChannel);
        builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(0).setContentTitle("만보기").setContentText(Integer.toString(TESTActivity.cnt)).setWhen(System.currentTimeMillis()).build();
        startForeground(9999,builder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerRestartAlarm();
        stopForeground(true);
    }
    private void registerRestartAlarm(){

        Intent intent = new Intent(foreground.this,Receiver.class);
        intent.setAction("ACTION.RESTART.foreground");
        PendingIntent sender = PendingIntent.getBroadcast(foreground.this,0,intent,0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1*1000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,1*1000,sender);

    }
    private void unregisterRestartAlarm(){

        Intent intent = new Intent(foreground.this,Receiver.class);
        intent.setAction("ACTION.RESTART.foreground");
        PendingIntent sender = PendingIntent.getBroadcast(foreground.this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 알람 취소
         */
        alarmManager.cancel(sender);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    TESTActivity.cnt++;
                    TESTActivity.tView.setText(Integer.toString(TESTActivity.cnt));
                    builder.setContentText(Integer.toString(TESTActivity.cnt));
                    Calendar calendar=Calendar.getInstance();
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    if(!db.update(month,day,TESTActivity.cnt))
                        db.insert(month,day,TESTActivity.cnt);
                   startForeground(9999,builder.build());
                }

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            }
        }
    }
    static public void setCnt(int a) {
        TESTActivity.cnt = a;
        TESTActivity.tView.setText(Integer.toString(TESTActivity.cnt));
        builder.setContentText(Integer.toString(TESTActivity.cnt));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

