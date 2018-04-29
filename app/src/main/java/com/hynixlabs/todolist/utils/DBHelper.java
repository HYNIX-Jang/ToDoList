package com.hynixlabs.todolist.utils;

/**
 * Created by HYNIX Jang on 2018.04.21
 * For : LoginActivity
 * Github : https://github.com/HYNIX-Jang
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "loginDB", null, 11);
    }

    // DB생성 시 호출되는 DB 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE TB_MEMBERS(NAME TEXT NOT NULL, EMAIL TEXT PRIMARY KEY NOT NULL, PASSWORD TEXT NOT NULL)";
        db.execSQL(sql);
        sql = "CREATE TABLE INFO(ID INTEGER PRIMARY KEY AUTOINCREMENT, INFO TEXT UNIQUE ,EMAIL TEXT NOT NULL, LIKES BOOLEAN)";
        db.execSQL(sql);
        System.out.println("Successfully Created the table!");
    }

    // DB 버전업그레이드 시 호출되는 DB 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE TB_MEMBERS";
        db.execSQL(sql);
        sql = "DROP TABLE INFO";
        db.execSQL(sql);
        System.out.println("Successfully Dropped the table!");
        sql = "CREATE TABLE TB_MEMBERS(NAME TEXT NOT NULL, EMAIL TEXT PRIMARY KEY NOT NULL, PASSWORD TEXT NOT NULL)";
        db.execSQL(sql);
        sql = "CREATE TABLE INFO(ID INTEGER PRIMARY KEY AUTOINCREMENT, INFO TEXT UNIQUE,EMAIL TEXT NOT NULL, LIKES BOOLEAN)";
        db.execSQL(sql);
    }
}
