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
    private String _boardSize;
    private String _gameMode;

    private String _db_score;
    private String _db_time;


    public Record(int score, Date time, String boardSize, String gameMode){
        this._score = score;
        this._time = time;
        this._boardSize = boardSize;
        this._gameMode = gameMode;
    }

    public Record(String score, String time){
        this._db_score = score;
        this._db_time = time;
    }

    /**************************/
    public String getDBscore(){
        return _db_score;
    }

    public String getDBtime(){
        return _db_time;
    }
    /**************************/


    public String getBoardSize(){
        return _boardSize;
    }

    public String getGameMode(){
        return _gameMode;
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
        public static final String GAME_MODE = "game_mode";
        public static final String BOARD_SIZE = "board_size";

        /* table name */
        public static final String TABLE_NAME = "high_scores";

    }

}
