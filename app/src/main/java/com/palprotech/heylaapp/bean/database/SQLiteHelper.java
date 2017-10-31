package com.palprotech.heylaapp.bean.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 24-10-2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String TAG = "SQLiteHelper.java";

    private static final String DATABASE_NAME = "heyla.db";
    private static final int DATABASE_VERSION = 2;

    private static final String table_create_remember_me = "Create table IF NOT EXISTS rememberMe(_id integer primary key autoincrement,"
            + "username text," //1
            + "password text);";//2

    private static final String table_create_welcome_screen_check = "Create table IF NOT EXISTS appInfoCheck(_id integer primary key autoincrement,"
            + "status text);";//2

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Remember Me
        db.execSQL(table_create_remember_me);
        //Check App Info Screen Viewed
        db.execSQL(table_create_welcome_screen_check);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        //Remember Me
        db.execSQL("DROP TABLE IF EXISTS rememberMe");
        //Check App Info Screen Viewed
        db.execSQL("DROP TABLE IF EXISTS appInfoCheck");
    }

    public void open() throws SQLException {
        db = this.getWritableDatabase();
    }

    /*Remember me Data Store and Retrieve Functionality*/

    public long remember_me_insert(String val1, String val2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("username", val1);
        initialValues.put("password", val2);
        long l = db.insert("rememberMe", null, initialValues);
        db.close();
        return l;
    }

    public Cursor selectRememberMe() throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String fetch = "Select username,password from rememberMe limit 1;";
        Cursor c = db.rawQuery(fetch, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void deleteRememberMe() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("rememberMe", null, null);
    }

    /*End*/

    /*Check App Info Screen Viewed Data Store and Retrieve Functionality*/

    public long app_info_check_insert(String val1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("status", val1);
        long l = db.insert("appInfoCheck", null, initialValues);
        db.close();
        return l;
    }

    public Cursor selectAppInfoCheck() throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String fetch = "Select count(*) from appInfoCheck;";
        Cursor c = db.rawQuery(fetch, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /*End*/
}
