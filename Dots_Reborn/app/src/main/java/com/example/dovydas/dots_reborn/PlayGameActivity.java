package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGameActivity extends AppCompatActivity {

    private String _gameMode;
    private TextView _displayScore;
    private TextView _displayTimeOrMoves;
    private int _secondsLeft;
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
        _gameBoard = (BoardView) findViewById(R.id.gameCanvas);

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_moves);
        action.setDisplayUseLogoEnabled(true);

        if(_gameMode.equals("Time mode")){
            action.setLogo(R.drawable.ic_timed);

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
                }
            }.start();
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
}
