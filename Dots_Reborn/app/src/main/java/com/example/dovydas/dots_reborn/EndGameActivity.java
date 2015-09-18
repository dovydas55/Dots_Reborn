package com.example.dovydas.dots_reborn;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EndGameActivity extends AppCompatActivity {

    public final static String GAME_MODE = "com.example.dovydas.dots_reborn.GAME_MODE";
    private TextView _finalScore;
    private TextView _bestResult;
    private TextView _afterGameMessage;
    private int _userScore;
    private String _gameMode;
    private ArrayList<Record> _data;

    private HashMap<Integer, String> _sizeMap;
    private SharedPreferences _sp;
    private String _boardSize;

    private Context context = this;
    private UserDbHelper _userDbHelper;
    private SQLiteDatabase _sqLiteDatabase;
    private Cursor _cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        _sizeMap = new HashMap<>();
        _sizeMap.put(4, "4x4");
        _sizeMap.put(6, "6x6");
        _sizeMap.put(8, "8x8");

        _boardSize = _sizeMap.get(Integer.parseInt(_sp.getString("boardPref", "6")));

        Intent intent = getIntent();

        _userScore = Integer.parseInt(intent.getStringExtra(PlayGameActivity.FINAL_SCORE));
        _finalScore = (TextView) findViewById(R.id.final_game_score);
        _bestResult = (TextView) findViewById(R.id.best_game_score);
        _afterGameMessage = (TextView) findViewById(R.id.msg);
        _gameMode = intent.getStringExtra(PlayGameActivity.GAMEMODE);
        _data = new ArrayList<>();

        _userDbHelper = new UserDbHelper(context);
        _sqLiteDatabase = _userDbHelper.getWritableDatabase();
        readRecords();
        addScoreToDb();

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setDisplayShowTitleEnabled(false);
        action.setLogo(R.drawable.ic_scores);
        action.setDisplayUseLogoEnabled(true);

    }

    @Override
    public void onStart(){
        super.onStart();
        startCountAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_game, menu);
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

    public void goHome(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void playMoves(View v){
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(GAME_MODE, "Move mode");
        startActivity(intent);
    }

    public void playTime(View v){
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(GAME_MODE, "Time mode");
        startActivity(intent);
    }

    /* so player canot just back out of the game */
    @Override
    public void onBackPressed() {
        //intentionally empty
    }

    /*
    * retrieved from stackoverflow
    * http://stackoverflow.com/questions/29443767/how-to-implement-increasing-number-animation-from-0-to-600-in-5-secs-on-textview
    */
    private void startCountAnimation() {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, _userScore);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                _finalScore.setText("" + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    /*adding score*/
    private void addScoreToDb(){
        Record rec = new Record(_userScore, new Date(), _boardSize, _gameMode);
        _userDbHelper.addInformations(rec.getHighScore(), rec.getTime(), rec.getBoardSize(), rec.getGameMode(), _sqLiteDatabase);
        Toast.makeText(getBaseContext(), "Data saved", Toast.LENGTH_LONG).show();
        _userDbHelper.close();
    }

    /*getting best score */
    private void readRecords(){
        _data.clear();
        _cursor = _userDbHelper.getInformations(_boardSize, _gameMode, _sqLiteDatabase);
        if(_cursor.moveToFirst()){
            do {
                String score, date;
                score = _cursor.getString(0); /* column index */
                date = _cursor.getString(1);
                _data.add(new Record(score, date));

            } while(_cursor.moveToNext());
        }

        if(!_data.isEmpty()){
            _bestResult.setText(_data.get(0).getDBscore());
        } else {
            _bestResult.setText("0");
        }

        if(_data.isEmpty() || _userScore > Integer.parseInt(_data.get(0).getDBscore())){
            /* new high score */
            _afterGameMessage.setText("NEW HIGH SCORE!");
        } else {
            _afterGameMessage.setText("You can do better!!");
        }

    }

}
