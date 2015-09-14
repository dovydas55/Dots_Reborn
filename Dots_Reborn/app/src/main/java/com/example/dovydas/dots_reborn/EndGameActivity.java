package com.example.dovydas.dots_reborn;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class EndGameActivity extends AppCompatActivity {

    public final static String GAME_MODE = "com.example.dovydas.dots_reborn.GAME_MODE";
    private TextView _finalScore;
    private int _userScore;

    private Context context = this;
    private UserDbHelper userDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent intent = getIntent();
        _userScore = Integer.parseInt(intent.getStringExtra(PlayGameActivity.FINAL_SCORE));
        _finalScore = (TextView) findViewById(R.id.final_game_score);

        addScoreToDb();

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

    private void addScoreToDb(){
        Record rec = new Record(_userScore, new Date());
        userDbHelper = new UserDbHelper(context);
        sqLiteDatabase = userDbHelper.getWritableDatabase();
        userDbHelper.addInformations(rec.getHighScore(), rec.getTime(), sqLiteDatabase);
        Toast.makeText(getBaseContext(), "Data saved", Toast.LENGTH_LONG).show();
        userDbHelper.close();
    }

}
