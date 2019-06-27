package com.cookandroid.alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseOpenHelper extends SQLiteOpenHelper {

    public MyDataBaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE ALARM (_id INTEGER PRIMARY KEY AUTOINCREMENT, hour INTEGER,minute INTEGER,context STRING);");
    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int hour,int minute,String context) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO ALARM VALUES(null, '" + hour + "', '" + minute + "','"+context+"');");
        db.close();
    }
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM AlARM WHERE _id='" + id + "'");
        db.close();
    }
    public void deleteall(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + "ALARM");
    }

    public String[][] getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result[][] = new String[20][4];
        int a=0;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ALARM", null);
        while (cursor.moveToNext()) {
            result[a][0] = cursor.getString(0);
            result[a][1] = cursor.getString(1);
            result[a][2] = cursor.getString(2);
            result[a][3] = cursor.getString(3);
            a++;
        }
        return result;
    }
}

