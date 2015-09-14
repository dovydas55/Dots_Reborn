package com.example.dovydas.dots_reborn;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by dovydas on 9/10/2015.
 */
public class Record implements Serializable {

    private int _score;
    private Date _time;

    private String _db_score;
    private String _db_time;

    public Record(int score, Date time){
        _score = score;
        _time = time;
    }

    public Record(String score, String time){
        this._db_score = score;
        this._db_time = time;
    }

    public String getDBscore(){
        return _db_score;
    }

    public String getDBtime(){
        return _db_time;
    }

    public String getHighScore(){
        return Integer.toString(_score);
    }

    public String getTime(){
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_time).toString();
    }

    @Override
    public String toString() {
        return _score + "  " + _time;
    }

    /* defining score table in database */
    public static abstract class User_HighScores{
        /* columns*/
        public static final String USER_SCORE = "user_score";
        public static final String USER_DATE = "user_date";

        /* table name */
        public static final String TABLE_NAME = "high_scores";

    }

}
