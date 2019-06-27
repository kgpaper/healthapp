package com.cookandroid.sensor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE STEP (month INTEGER,day INTEGER , step INTEGER);");
    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int month,int day,int step) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO STEP VALUES('"+month+"', '" + day + "', " + step + ");");
        db.close();
    }
    public boolean update(int month, int day, int step){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM STEP", null);
        int a=0;
        while(cursor.moveToNext()){
            if(cursor.getInt(0)==month&&cursor.getInt(1)==day)
                break;
            if(cursor.isLast()) a=-1;
        }
        if(a==-1){db.close(); return false;}
        else{
        db.execSQL("UPDATE STEP SET step='"+step+"' WHERE month='"+month+"' and day='"+day+"'");
        db.close();
        return true;
        }
    }
    public int[][] getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        int result[][] = new int[7][3];
        int a=6;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM STEP", null);
        cursor.moveToLast();
        cursor.moveToNext();
        while (cursor.moveToPrevious()) {
            result[a][0] = cursor.getInt(0); // 월
            result[a][1] = cursor.getInt(1);// 일
            result[a][2] = cursor.getInt(2);// step count
            a--;
            if(a<0)
                break;
        }
        return result;
    }
}
