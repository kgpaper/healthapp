package com.example.sec.mi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void addClick (View v){
        EditText number1 =(EditText) findViewById(R.id.number1);
        EditText number2 =(EditText) findViewById(R.id.number2);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        int n1= Integer.parseInt(number1.getText().toString());
        int n2= Integer.parseInt(number2.getText().toString());

        textView7.setText(Integer.toString(n1+n2));

    }
    public void subClick (View v){
        EditText number1 =(EditText) findViewById(R.id.number1);
        EditText number2 =(EditText) findViewById(R.id.number2);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        int n1= Integer.parseInt(number1.getText().toString());
        int n2= Integer.parseInt(number2.getText().toString());

        textView7.setText(Integer.toString(n1-n2));

    }
    public void multiClick (View v){
        EditText number1 =(EditText) findViewById(R.id.number1);
        EditText number2 =(EditText) findViewById(R.id.number2);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        int n1= Integer.parseInt(number1.getText().toString());
        int n2= Integer.parseInt(number2.getText().toString());

        textView7.setText(Integer.toString(n1*n2));

    }
    public void divClick (View v){
        EditText number1 =(EditText) findViewById(R.id.number1);
        EditText number2 =(EditText) findViewById(R.id.number2);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        int n1= Integer.parseInt(number1.getText().toString());
        int n2= Integer.parseInt(number2.getText().toString());

        textView7.setText(Integer.toString(n1/n2));

    }
}
