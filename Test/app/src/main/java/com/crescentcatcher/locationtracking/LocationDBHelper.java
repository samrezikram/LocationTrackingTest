package com.crescentcatcher.locationtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by samrezikram on 11/22/17.
 */

public class LocationDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocationSQLite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LOCATION_TABLE_NAME = "Location";
    public static final String LOCATION_COLUMN_ID = "_id";
    public static final String LOCATION_COLUMN_LATITUDE = "lati";
    public static final String LOCATION_COLUMN_LONGITUDE = "long";

    public LocationDBHelper(Context context){
        super(context, DATABASE_NAME , null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + LOCATION_TABLE_NAME + "(" +
                LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                LOCATION_COLUMN_LATITUDE + " TEXT, " +
                LOCATION_COLUMN_LONGITUDE + " TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertRoute(String latitude, String longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_COLUMN_LATITUDE, latitude);
        contentValues.put(LOCATION_COLUMN_LONGITUDE, longitude);
        db.insert(LOCATION_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateRoute(Integer id, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_COLUMN_LATITUDE, latitude);
        contentValues.put(LOCATION_COLUMN_LONGITUDE, longitude);
        db.update(LOCATION_TABLE_NAME, contentValues, LOCATION_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
}
