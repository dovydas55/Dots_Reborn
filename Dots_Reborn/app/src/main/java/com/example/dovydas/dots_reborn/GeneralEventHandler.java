package com.example.dovydas.dots_reborn;

/**
 * Created by dovydas on 9/14/2015.
 */
public interface GeneralEventHandler {
    void onUpdateScore();
    void onUpdateMove();
    void controlSpecialOps();
    void playSound();
    void clearSound();
    void playBigBomb();
    void smallBomb();
}

