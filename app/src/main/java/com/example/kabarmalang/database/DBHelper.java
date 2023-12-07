package com.example.kabarmalang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "KabarMalang.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "berita_details";
    private static final String COLUMN_ID = "berita_id";
    private static final String COLUMN_TITLE = "berita_title";
    private static final String COLUMN_DESC = "berita_desc";
    private static final String COLUMN_IMG = "berita_img";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_IMG + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBerita(String title, String desc, int image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESC, desc);
        cv.put(COLUMN_IMG, image);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            Toast.makeText(context, "Failed to Add",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Added Successfully!",
                    Toast.LENGTH_SHORT).show();

    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String title, String desc, int image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESC, desc);
        contentValues.put(COLUMN_IMG, image);
        long result = db.update(TABLE_NAME, contentValues, "berita_id=?", new
                String[] {row_id});
        if (result == -1)
            Toast.makeText(context, "Failed to Update",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated Successfully!",
                    Toast.LENGTH_SHORT).show();
    }

    public void deleteData(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "berita_id=?", new String[]
                {row_id});
        if (result == -1)
            Toast.makeText(context, "Failed to Delete",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Deleted Successfully!",
                    Toast.LENGTH_SHORT).show();
    }
}
