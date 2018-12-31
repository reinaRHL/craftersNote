package com.mp.rena.craftersnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TASK";

    // Table Names
    private static final String TABLE_TODAY = "TodayTask";
    private static final String TABLE_EVERYDAY = "EverydayTask";

    // Common column names
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON = "icon";
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_URLTYPE = "urlType";

    // Table Create Statements
    // Today table create statement
    private static final String CREATE_TABLE_TODAY = "CREATE TABLE "
            + TABLE_TODAY + "(" + KEY_NAME + " TEXT," + KEY_ID + " INTEGER," + KEY_ICON
            + " TEXT," + KEY_URL + " TEXT," + KEY_URLTYPE + " TEXT" + ")";

    // Everyday table create statement
    private static final String CREATE_TABLE_EVERYDAY = "CREATE TABLE "
            + TABLE_EVERYDAY + "(" + KEY_NAME + " TEXT," + KEY_ID + " INTEGER," + KEY_ICON
            + " TEXT," + KEY_URL + " TEXT," + KEY_URLTYPE + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public long insertToday(Item item ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_ID, item.getId());
        values.put(KEY_ICON, item.getIcon());
        values.put(KEY_URL, item.getUrl());
        values.put(KEY_URLTYPE, item.getUrlType());
        long today_id = db.insert(TABLE_TODAY, null, values);
        MainActivity.saveSharedPreference();
        return today_id;
    }

    public long insertEveryday(Item item ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_ID, item.getId());
        values.put(KEY_ICON, item.getIcon());
        values.put(KEY_URL, item.getUrl());
        values.put(KEY_URLTYPE, item.getUrlType());
        long every_id = db.insert(TABLE_EVERYDAY, null, values);
        MainActivity.saveSharedPreference();
        return every_id;
    }

    public void deleteToday(String name, int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String deleteQ = "DELETE FROM " + TABLE_TODAY + " WHERE "
                + KEY_NAME + " = \'" + name + "\' and " + KEY_ID + " = \'" + id + "\'";

        db.execSQL(deleteQ);
        MainActivity.saveSharedPreference();

    }

    public void deleteEveryDay(String name, int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String deleteQ = "DELETE FROM " + TABLE_EVERYDAY + " WHERE "
                + KEY_NAME + " = \'" + name + "\' and " + KEY_ID + " = \'" + id + "\'";

        db.execSQL(deleteQ);
        MainActivity.saveSharedPreference();
    }

    public void deleteAllToday() {
        SQLiteDatabase db = this.getReadableDatabase();

        String deleteQ = "DELETE FROM " + TABLE_TODAY;

        db.execSQL(deleteQ);
    }


    //copy all from everyday to today
    public void copyFromEveryToToday() {
        SQLiteDatabase db = this.getReadableDatabase();

        String copyQ = "INSERT INTO " + TABLE_TODAY + " SELECT * FROM " + TABLE_EVERYDAY;
        db.execSQL(copyQ);
    }


    // populate everyday list from everyday table
    public void populateEFromE(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_EVERYDAY, null);
        int nameIndex = c.getColumnIndex("name");
        int idIndex = c.getColumnIndex("id");
        int iconIndex = c.getColumnIndex("icon");
        int urlIndex = c.getColumnIndex("url");
        int urlTypeIndex = c.getColumnIndex("urlType");
        c.moveToFirst();
        if (c.moveToFirst()){
            do {
                String name = c.getString(nameIndex);
                int id = c.getInt(idIndex);
                String icon = c.getString(iconIndex);
                String url = c.getString(urlIndex);
                String urlType = c.getString(urlTypeIndex);
                Item itemPopulate = new Item(name, id, icon, url, urlType);
                ManageTask.list.add(itemPopulate);
            } while(c.moveToNext());
        }
    }

    // populate today list from today table
    public void populateTFromT(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_TODAY, null);
        int nameIndex = c.getColumnIndex("name");
        int idIndex = c.getColumnIndex("id");
        int iconIndex = c.getColumnIndex("icon");
        int urlIndex = c.getColumnIndex("url");
        int urlTypeIndex = c.getColumnIndex("urlType");
        c.moveToFirst();
        if (c.moveToFirst()){
            do {
                String name = c.getString(nameIndex);
                int id = c.getInt(idIndex);
                String icon = c.getString(iconIndex);
                String url = c.getString(urlIndex);
                String urlType = c.getString(urlTypeIndex);
                Item itemPopulate = new Item(name, id, icon, url, urlType);
                TodaysTask.list.add(itemPopulate);
            } while(c.moveToNext());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODAY);
        db.execSQL(CREATE_TABLE_EVERYDAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVERYDAY);

        // create new tables
        onCreate(db);
    }
}
