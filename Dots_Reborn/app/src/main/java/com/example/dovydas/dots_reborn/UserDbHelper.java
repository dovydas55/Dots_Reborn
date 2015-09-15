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
                                                                                                            Record.User_HighScores.USER_DATE + " TEXT," +
                                                                                                            Record.User_HighScores.BOARD_SIZE + " TEXT," +
                                                                                                            Record.User_HighScores.GAME_MODE + " TEXT);";

    public UserDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME);
        //Log.e("DATABASE OPERATIONS", "Database deleted");
        Log.e("DATABASE OPERATIONS", "Database created / opened");
    }

    public Cursor getInformations(String size, String mode, SQLiteDatabase db){
        Cursor cursor;
        String [] projections  = {Record.User_HighScores.USER_SCORE, Record.User_HighScores.USER_DATE};
        String selection = Record.User_HighScores.BOARD_SIZE +"=? AND " + Record.User_HighScores.GAME_MODE +"=?";
        String[] selection_args = {size, mode};
        cursor = db.query(Record.User_HighScores.TABLE_NAME, projections, selection, selection_args,null,null,null);
        return cursor;
    }

    public void deleteInformation(String size, String mode, SQLiteDatabase db){
        String selection = Record.User_HighScores.BOARD_SIZE +"=? AND " + Record.User_HighScores.GAME_MODE +"=?";
        String[] selection_args = {size, mode};
        db.delete(Record.User_HighScores.TABLE_NAME, selection, selection_args);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATIONS", "Table created");

    }

    public void addInformations(String score, String date, String size, String mode, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Record.User_HighScores.USER_SCORE, score);
        contentValues.put(Record.User_HighScores.USER_DATE, date);
        contentValues.put(Record.User_HighScores.BOARD_SIZE, size);
        contentValues.put(Record.User_HighScores.GAME_MODE, mode);
        db.insert(Record.User_HighScores.TABLE_NAME, null, contentValues);
        Log.e("DATABASE OPERATIONS", "One row inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
