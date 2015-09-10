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

    public Record(int score, Date time){
        _score = score;
        _time = time;
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

}
