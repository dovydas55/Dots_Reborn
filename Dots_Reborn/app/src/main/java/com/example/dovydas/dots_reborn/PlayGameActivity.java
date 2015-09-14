package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.os.CountDownTimer;
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
    private int _gameScore = 0;
    private BoardView _gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        /* Extract game mode */
        Intent intent = getIntent();
        _gameMode = intent.getStringExtra(MainActivity.GAME_MODE);
        Toast.makeText(getApplicationContext(), _gameMode, Toast.LENGTH_SHORT).show();
        /* *** *** *** *** *** */

        _displayScore = (TextView) findViewById(R.id.play_display_score);
        _displayTimeOrMoves = (TextView) findViewById(R.id.play_display_time_or_moves);

        updateScore(); /* initializing score */


        _gameBoard = (BoardView) findViewById(R.id.gameCanvas);
        _gameBoard.setGeneralHandler(new GeneralEventHandler() {
            @Override
            public void onUpdateScore() {
                _gameScore += 1;
                updateScore();
            }
        });

        _gameBoard.setMovesOrTime((TextView)findViewById(R.id.play_display_time_or_moves), _gameMode);

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_moves);
        action.setDisplayUseLogoEnabled(true);

        if(_gameMode.equals("Time mode")) {
            action.setLogo(R.drawable.ic_timed);
        }

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
    }
}
