package com.cookandroid;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cookandroid.health.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btn1=(ImageButton)findViewById(R.id.알람);
        final Intent intent = new Intent(this,com.cookandroid.alarm.MainActivity.class);
        btn1.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
            startActivity(intent);
                            }
        });
        ImageButton btn2=(ImageButton)findViewById(R.id.만보기);
        final Intent intent1 = new Intent(this,com.cookandroid.sensor.TESTActivity.class);
        btn2.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                startActivity(intent1);
            }
        });
        ImageButton btn3=(ImageButton)findViewById(R.id.산책로);
        final Intent intent2 = new Intent(this,com.cookandroid.walk.MapsActivity.class);
        btn3.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
    }
}
