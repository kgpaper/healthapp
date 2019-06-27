package com.cookandroid.walk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase1 extends SQLiteOpenHelper {

    public DataBase1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE MAP (_id INTEGER, lat DOUBLE,lon DOUBLE);");
    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int id, double lat,double lon) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MAP VALUES('"+id+"', '" + lat + "', " + lon + ");");
        db.close();
    }
    public double[][] getLatLng(int id) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        double result[][] = new double[20][2];
        int a=0;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MAP", null);
        while (cursor.moveToNext()) {
            if(cursor.getInt(0)==id) {
                result[a][0] = cursor.getDouble(1);
                result[a][1] = cursor.getDouble(2);
                a++;
            }
        }
        return result;
    }
}
