package com.example.dovydas.dots_reborn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dovydas on 9/14/2015.
 */
public class UserDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "HIGH_SCORE.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY =
                                                "CREATE TABLE " + Record.User_HighScores.TABLE_NAME + "(" + Record.User_HighScores.USER_SCORE + " TEXT," +
                                                Record.User_HighScores.USER_DATE + " TEXT);";


    public UserDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS", "Database created / opened");
    }

    public Cursor getInformations(SQLiteDatabase db){
        Cursor cursor;
        String [] projections  = {Record.User_HighScores.USER_SCORE, Record.User_HighScores.USER_DATE};
        cursor = db.query(Record.User_HighScores.TABLE_NAME, projections, null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATIONS", "Table created");

    }

    public void addInformations(String score, String date, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Record.User_HighScores.USER_SCORE, score);
        contentValues.put(Record.User_HighScores.USER_DATE, date);
        db.insert(Record.User_HighScores.TABLE_NAME, null, contentValues);
        Log.e("DATABASE OPERATIONS", "One row inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
