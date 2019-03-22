package com.example.challenge1part2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.Blob;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Images.db";
    public static final String TABLE_NAME = "images_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " BLOB" + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(byte[] imagebyte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, imagebyte);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public byte[] getData(int numberid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columnNames = {COL_1, COL_2};
        Cursor cr = db.query(TABLE_NAME, columnNames, "ID =? ", new String [] {String.valueOf(numberid)}, null, null, null, null);

        if (cr != null) {
            cr.moveToFirst();
            byte[] imageInBytes = cr.getBlob(1);
            cr.close();
            return imageInBytes;
        }

        cr.close();
        return new byte[0];
    }


}
