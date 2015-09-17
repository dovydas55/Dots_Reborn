package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGameActivity extends AppCompatActivity {

    private String _gameMode;
    private TextView _displayScore;
    private TextView _displayTimeOrMoves;
    private int _secondsLeft;
    private int _movesLeft = 5; /* MAKE SURE TO CHANGE IT BACK!!*/
    private int _gameScore = 0;
    private BoardView _gameBoard;

    private Vibrator _vibrator;
    private boolean _use_vibration = false;
    private SharedPreferences _sp;

    public final static String FINAL_SCORE = "com.example.dovydas.dots_reborn.FINAL_SCORE";
    public final static String GAMEMODE = "com.example.dovydas.dots_reborn.GAME_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_moves);
        action.setDisplayUseLogoEnabled(true);
        action.setDisplayShowTitleEnabled(false);

        /* Extract game mode */
        Intent intent = getIntent();
        _gameMode = intent.getStringExtra(MainActivity.GAME_MODE);
        if(_gameMode.isEmpty() || _gameMode == null){
            _gameMode = intent.getStringExtra(EndGameActivity.GAME_MODE);
        }
        Toast.makeText(getApplicationContext(), _gameMode, Toast.LENGTH_SHORT).show();
        /* *** *** *** *** *** */

        _displayScore = (TextView) findViewById(R.id.play_display_score);
        _displayTimeOrMoves = (TextView) findViewById(R.id.play_display_time_or_moves);
        _gameBoard = (BoardView) findViewById(R.id.gameCanvas);

        updateScore(); /* initializing score */
        if(_gameMode.equals("Move mode")){
            updateMoves();
        } else if(_gameMode.equals("Time mode")) {
            startTimeCounter();
            action.setLogo(R.drawable.ic_timed);
        }

        _vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        /* working with handlers */
        _gameBoard.setGeneralHandler(new GeneralEventHandler() {
            @Override
            public void onUpdateScore() {
                _gameScore += 10;
                updateScore();
            }

            @Override
            public void onUpdateMove() {
                if(_gameMode.equals("Move mode")){
                    _movesLeft -= 1;
                    updateMoves();
                    if(_movesLeft == 0){
                        endGame();
                    }
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_game, menu);
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

    public void shuffleBoard(View v){
        _gameBoard.shuffleBoard();
    }

    private void updateScore(){
        _displayScore.setText("Score " + Integer.toString(_gameScore));


        //vibrate every time score increases
        _use_vibration = _sp.getBoolean("vibrate",false);
        if(_use_vibration) {
            _vibrator.vibrate(500);
        }
    }

    private void updateMoves(){
        if(_gameMode.equals("Move mode")){
            _displayTimeOrMoves.setText("Moves " + Integer.toString(_movesLeft));
        }
    }

    private void startTimeCounter(){
        new CountDownTimer(30000, 100) {
            public void onTick(long ms) {
                if (Math.round((float)ms / 1000.0f) != _secondsLeft)
                {
                    _secondsLeft = Math.round((float)ms / 1000.0f);
                    _displayTimeOrMoves.setText("Time " + _secondsLeft );
                }
            }

            public void onFinish() {
                _displayTimeOrMoves.setText("Time 0");
                endGame();
            }
        }.start();
    }

    private void endGame(){
        Intent intent = new Intent(this, EndGameActivity.class);
        intent.putExtra(FINAL_SCORE, Integer.toString(_gameScore));
        intent.putExtra(GAMEMODE, _gameMode);
        startActivity(intent);
    }
}
