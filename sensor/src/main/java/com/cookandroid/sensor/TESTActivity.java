package com.cookandroid.sensor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class TESTActivity extends AppCompatActivity  {
    static AlarmManager alarmManager;
    public static int cnt = 0;
    static TextView tView;
    PendingIntent pendingIntent;
    Intent my_intent;
    Receiver a;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        db = new DataBase(getApplicationContext(), "STEP.db", null, 1);
        read_db(db);
        tView = (TextView) findViewById(R.id.cntView);
        tView.setText(Integer.toString(cnt));
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        my_intent = new Intent(TESTActivity.this, Receiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        my_intent.putExtra("day",calendar.get(Calendar.DAY_OF_YEAR));
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        pendingIntent = PendingIntent.getBroadcast(TESTActivity.this, 1, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        a=new Receiver();
        Intent intent=new Intent(TESTActivity.this,foreground.class);
        IntentFilter intentFilter=new IntentFilter("com.cookandroid.sensor.foreground");
        registerReceiver(a,intentFilter);
        startForegroundService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        read_db(db);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
      unregisterReceiver(a);
    }
   public void read_db(DataBase db) {
        int a[][] = db.getResult();
        Calendar calendar=Calendar.getInstance();
        if(a[6][0]==0){ //차트보여주기위해 임의값 삽입
            db.insert(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)-5,new Random().nextInt(12000));
            db.insert(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)-4,new Random().nextInt(12000));
            db.insert(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)-3,new Random().nextInt(12000));
            db.insert(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)-2,new Random().nextInt(12000));
            db.insert(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)-1,new Random().nextInt(12000));
            a = db.getResult();
        }
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        List<BarEntry> values = new ArrayList<>();
        final ArrayList<String> day1 = new ArrayList<>();
        int b = 0,c=0;
        while (b <7) {
            if (a[b][0] == 0)
                b++;
            else if(a[b][0]==month&&a[b][1]==day) {
                cnt = a[b][2];
                break;
            }
            else {
                String buf = a[b][0] + "월" + a[b][1] + "일";
                day1.add(buf);
                values.add(new BarEntry(c, a[b][2]));
                b++;c++;
            }
        }
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        BarDataSet data_set = new BarDataSet(values, "걸음 수");
        BarData data = new BarData(data_set);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(day1));
        data_set.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getDescription().setEnabled(false);
        barChart.setData(data);
        barChart.invalidate();
    }
}