package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class IntermediateHighscoreActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.dovydas.dots_reborn.MESSAGE";

    ViewGroup _highScoreSelect;
    private SquareIcon _selectMoves;
    private SquareIcon _selectTimed;
    private SharedPreferences _sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_highscore);

        _highScoreSelect = (ViewGroup) findViewById(R.id.highScoreSelect);
        _selectMoves = (SquareIcon) findViewById(R.id.highScoreMoveMode);
        _selectTimed = (SquareIcon) findViewById(R.id.highScoreTimeMode);


        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_scores);
        action.setDisplayUseLogoEnabled(true);
        action.setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intermediate_highscore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, InGameOptionsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            int right = _highScoreSelect.getRight();


            TranslateAnimation transMoves = new TranslateAnimation(-1000, 0, 0, 0);
            transMoves.setDuration(800);
            transMoves.setFillAfter(true);

            TranslateAnimation transTimed = new TranslateAnimation(right + 200, 0, 0, 0);
            transTimed.setStartOffset(300);
            transTimed.setDuration(800);
            transTimed.setFillAfter(true);


            _selectMoves.startAnimation(transMoves);
            _selectTimed.startAnimation(transTimed);

        }
    }

    public void showMoveHighScore(View v){
        Intent intent = new Intent(this, DisplayHighScoresActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Move mode"); /*Moves Mode High Scores*/
        startActivity(intent);
    }

    public void showTimeHighScore(View v){
        Intent intent = new Intent(this, DisplayHighScoresActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Time mode"); /*Timed Mode High Scores*/
        startActivity(intent);
    }

}
