package com.example.kabarmalang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "KabarMalang.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "berita_details";
    public static final String COLUMN_ID = "berita_id";
    public static final String COLUMN_TITLE = "berita_title";
    public static final String COLUMN_DESC = "berita_desc";
    public static final String COLUMN_IMG = "berita_img";
    public static final String COLUMN_DATE = "berita_date";
    public static final String COLUMN_LOCATION = "berita_location";
    public static final String COLUMN_LATITUDE = "berita_latitude";
    public static final String COLUMN_LONGITUDE = "berita_longitude";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_DATE + " DATE DEFAULT CURRENT_DATE, " +
                COLUMN_IMG + " BLOB, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
