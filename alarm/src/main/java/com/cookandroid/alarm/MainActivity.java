package com.cookandroid.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static AlarmManager alarm_manager;
    static TimePicker alarm_timepicker;
    EditText editText;
    Context context;
    PendingIntent pendingIntent;
    int b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context = this;
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.time_1);
        editText =findViewById(R.id.abc);
        final Calendar calendar = Calendar.getInstance();
        Button alarm_on = findViewById(R.id.btn_start);
        final MyDataBaseOpenHelper db= new MyDataBaseOpenHelper(getApplicationContext(),"ALARM.db",null,1);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                db.insert(hour,minute,editText.getText().toString());
                Toast.makeText(MainActivity.this, "Alarm 예정 " + hour + "시 " + minute + "분 ", Toast.LENGTH_SHORT).show();
                result(db);
            }
        });
        result(db);
    }
    public void result(MyDataBaseOpenHelper db){
        final MyDataBaseOpenHelper db1=db;
        LinearLayout lm = (LinearLayout) findViewById(R.id.a);
        lm.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String a[][]=db1.getResult();
        b=0;
        while(b<20){
            // LinearLayout 생성
            if(a[b][0]==null) break;
            else{
            alarm_set(Integer.parseInt(a[b][1]),Integer.parseInt(a[b][2]),Integer.parseInt(a[b][0]),a[b][3]);
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            // TextView 생성
            TextView tvProdc = new TextView(this);
                if(Integer.parseInt(a[b][1])<10)
                    if(Integer.parseInt(a[b][2])<10)
                        tvProdc.setText("0"+a[b][1]+":0"+a[b][2]+"("+a[b][3]+")");
                    else
                        tvProdc.setText("0"+a[b][1]+":"+a[b][2]+"("+a[b][3]+")");
                else
                if(Integer.parseInt(a[b][2])<10)
                    tvProdc.setText(+Integer.parseInt(a[b][1])+":0"+a[b][2]+"("+a[b][3]+")");
                else
                    tvProdc.setText(Integer.parseInt(a[b][1])+":"+a[b][2]+"("+a[b][3]+")");
            tvProdc.setTextSize(30);
            params.weight=7;
            params.gravity= Gravity.CENTER;
            tvProdc.setLayoutParams(params);
            ll.addView(tvProdc);
            // TextView 생성
            // 버튼 생성
            Button btn = new Button(this);
            // setId 버튼에 대한 키값
            btn.setId(Integer.parseInt(a[b][0]));
            btn.setText("삭제");
                params.weight=3;
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    db1.delete(v.getId());
                    Toast.makeText(MainActivity.this, "Alarm 삭제", Toast.LENGTH_SHORT).show();
                    alarm_unset(v.getId());
                    update(db1);
                }
            });
            //버튼 add
            ll.addView(btn);
            //LinearLayout 정의된거 add
            lm.addView(ll);
                b++;}
        }}
        public void alarm_set(int hour,int minute,int i,String a){
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            Intent my_intent = new Intent(this, Alarm_Receiver.class);
            my_intent.putExtra("state", "alarm on");
            my_intent.putExtra("id", i);
            my_intent.putExtra("content",a);
            if (calendar.getTimeInMillis() +59000< System.currentTimeMillis() )
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, i, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        public void alarm_unset(int i){
            Intent my_intent = new Intent(this, Alarm_Receiver.class);
            my_intent.putExtra("state", "alarm off");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, i, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        public void update(MyDataBaseOpenHelper db){
        final MyDataBaseOpenHelper db1=db;
        LinearLayout lm = (LinearLayout) findViewById(R.id.a);
        lm.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String a[][]=db1.getResult();
        b=0;
        while(b<20){
        // LinearLayout 생성
        if(a[b][0]==null) break;
        else{
            alarm_set(Integer.parseInt(a[b][1]),Integer.parseInt(a[b][2]),Integer.parseInt(a[b][0]),a[b][3]);
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            // TextView 생성
            TextView tvProdc = new TextView(this);
            if(Integer.parseInt(a[b][1])<10)
                if(Integer.parseInt(a[b][2])<10)
                    tvProdc.setText("0"+a[b][1]+":0"+a[b][2]+"("+a[b][3]+")");
                else
                    tvProdc.setText("0"+a[b][1]+":"+a[b][2]+"("+a[b][3]+")");
            else
                if(Integer.parseInt(a[b][2])<10)
                    tvProdc.setText(+Integer.parseInt(a[b][1])+":0"+a[b][2]+"("+a[b][3]+")");
                else
                    tvProdc.setText(Integer.parseInt(a[b][1])+":"+a[b][2]+"("+a[b][3]+")");
            params.weight=7;
            params.gravity= Gravity.CENTER;
            tvProdc.setLayoutParams(params);
            tvProdc.setTextSize(30);
            ll.addView(tvProdc);
            // TextView 생성
            // 버튼 생성
            Button btn = new Button(this);
            // setId 버튼에 대한 키값
            btn.setId(Integer.parseInt(a[b][0]));
            btn.setText("삭제");
            params.weight=3;
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    db1.delete(v.getId());
                    Toast.makeText(MainActivity.this, "Alarm 삭제", Toast.LENGTH_SHORT).show();
                    alarm_unset(v.getId());
                    result(db1);
                }
            });
            //버튼 add
            ll.addView(btn);
            //LinearLayout 정의된거 add
            lm.addView(ll);
            b++;}
    }}
}