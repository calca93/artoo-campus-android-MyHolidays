package com.example.marko.myholidays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 03/08/2016.
 */
public class DB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "holidaysInfo";
    private static final String TABLE_HOLIDAY = "holiday";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PLACE = "place";
    private static final String KEY_DATE = "date";
    private static final String KEY_IMAGE = "image";

    public DB (Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_HOLIDAY_TABLE = "CREATE TABLE " + TABLE_HOLIDAY + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TITLE + " TEXT, " +
                KEY_PLACE + " TEXT, " +
                KEY_DATE + " TEXT, " +
                KEY_IMAGE + " TEXT" + ")";

        db.execSQL(CREATE_HOLIDAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAY);
        onCreate(db);
    }

    public void addHoliday (Holiday newHoliday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, newHoliday.getTitle());
        values.put(KEY_PLACE, newHoliday.getPlace());
        values.put(KEY_DATE, newHoliday.getDate());
        values.put(KEY_IMAGE, newHoliday.getImage());

        db.insert(TABLE_HOLIDAY, null, values);
        db.close();
    }

    public Holiday getHoliday(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOLIDAY,
                new String[] {KEY_ID, KEY_TITLE, KEY_PLACE, KEY_DATE, KEY_IMAGE},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        Holiday holiday = new Holiday(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        return holiday;
    }

    public void deleteHoliday(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_HOLIDAY, KEY_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    public int updateHoliday (Holiday newHoliday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, newHoliday.getTitle());
        values.put(KEY_PLACE, newHoliday.getPlace());
        values.put(KEY_DATE, newHoliday.getDate());
        values.put(KEY_IMAGE, newHoliday.getImage());

        return db.update(TABLE_HOLIDAY, values, KEY_ID + "=?",
                new String[] {String.valueOf(newHoliday.getId())});
    }

    public List<Holiday> getAll () {
        List<Holiday> holidayList = new ArrayList<Holiday>();
        String select = "SELECT * FROM " + TABLE_HOLIDAY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                Holiday holiday = new Holiday();
                holiday.setId(Integer.parseInt(cursor.getString(0)));
                holiday.setTitle(cursor.getString(1));
                holiday.setPlace(cursor.getString(2));
                holiday.setDate(cursor.getString(3));
                holiday.setImage(cursor.getString(4));

                holidayList.add(holiday);
            }while (cursor.moveToNext());
        }

        return holidayList;
    }

    public void addColumn(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE "+ TABLE_HOLIDAY + " ADD COLUMN "+ name +" STRING");
    }
}
