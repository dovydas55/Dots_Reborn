package com.example.dovydas.dots_reborn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private int _movesLeft = 30;
    private int _gameScore = 0;
    private BoardView _gameBoard;
    private int SPECIAL_OPS = 3;
    private TextView _displayCheats;
    private int _soundCounter = 0;
    private MediaPlayer _mySound1;
    private MediaPlayer _mySound2;
    private MediaPlayer _mySound3;
    private MediaPlayer _mySound4;
    private MediaPlayer _mySound5;
    private MediaPlayer _mySound6;
    private MediaPlayer _shuffleSound;
    private MediaPlayer _bigBomgSound;
    private MediaPlayer _smallBomb;
    private CountDownTimer _time;


    private Vibrator _vibrator;
    private boolean _use_vibration = false;
    private boolean _use_soundEffects = true;
    private SharedPreferences _sp;

    public final static String FINAL_SCORE = "com.example.dovydas.dots_reborn.FINAL_SCORE";
    public final static String GAMEMODE = "com.example.dovydas.dots_reborn.GAME_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

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
        //Toast.makeText(getApplicationContext(), _gameMode, Toast.LENGTH_SHORT).show();
        /* *** *** *** *** *** */

        _displayScore = (TextView) findViewById(R.id.play_display_score);
        _displayTimeOrMoves = (TextView) findViewById(R.id.play_display_time_or_moves);
        _gameBoard = (BoardView) findViewById(R.id.gameCanvas);
        _displayCheats = (TextView) findViewById(R.id.spec);

        initializeSound();
        updateScore(); /* initializing score */
        displayCheatView(); /* initialize cheats */
        if(_gameMode.equals("Move mode")){
            updateMoves();
        } else if(_gameMode.equals("Time mode")) {
            startTimeCounter();
            action.setLogo(R.drawable.ic_timed);
        }

        _vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

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

            @Override
            public void controlSpecialOps(){
                SPECIAL_OPS--;
                displayCheatView();
            }

            @Override
            public void playSound(){
                if(_use_soundEffects){
                    _soundCounter++;
                    playSoundEffects();
                }
            }

            @Override
            public void clearSound(){
                _soundCounter = 0;

            }

            @Override
            public void playBigBomb(){
                if(_use_soundEffects){
                    _bigBomgSound.start();
                }
            }

            @Override
            public void smallBomb(){
                if(_use_soundEffects){
                    _smallBomb.start();
                }
            }

        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        _use_vibration = _sp.getBoolean("vibrate", false);
        _use_soundEffects = _sp.getBoolean("sound", true);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        _mySound1.release();
        _mySound2.release();
        _mySound3.release();
        _mySound4.release();
        _mySound5.release();
        _mySound6.release();
        _shuffleSound.release();
        _bigBomgSound.release();
        _smallBomb.release();

    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit? Youre current score will not be saved!")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        if(_time != null){
                            _time.cancel();
                        }

                    }
                }).create().show();


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

    public void displayCheatView(){
        _displayCheats.setText("Cheats left: " + Integer.toString(SPECIAL_OPS));
    }

    public void shuffleBoard(View v){
        if(SPECIAL_OPS > 0){
            if(_use_soundEffects){
                _shuffleSound.start();
            }
            _gameBoard.shuffleBoard();
        }

    }

    public void explodeBoard(View v){
        if(SPECIAL_OPS > 0) {
            _gameBoard.setExplosion();
        }
    }

    public void explodeAdjacent(View v){
        if(SPECIAL_OPS > 0) {
            _gameBoard.setExplodeAdjancent();
        }
    }

    /***************************************************************************************/
    /********************************* PRIVATE METHODS *************************************/
    /***************************************************************************************/

    private void playSoundEffects(){
        if(_soundCounter == 1){
            _mySound1.start();
        } else if(_soundCounter == 2){
            _mySound2.start();
        } else if(_soundCounter == 3) {
            _mySound3.start();
        } else if(_soundCounter == 4){
            _mySound4.start();
        } else if(_soundCounter == 5){
            _mySound5.start();
        } else if(_soundCounter == 6){
            _mySound6.start();
        }
        else {
            _mySound1.start();
            _soundCounter = 1;

        }
    }

    private void updateScore(){
        _displayScore.setText("Score " + Integer.toString(_gameScore));

        if(_use_vibration && _gameScore > 0) {
            _vibrator.vibrate(500);
        }
    }

    private void updateMoves(){
        if(_gameMode.equals("Move mode")){
            _displayTimeOrMoves.setText("Moves " + Integer.toString(_movesLeft));
        }
    }

    private void startTimeCounter(){
        _time = new CountDownTimer(30000, 100) {
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

    private void initializeSound(){
        _mySound1 = MediaPlayer.create(this, R.raw.doshort);
        _mySound2 = MediaPlayer.create(this, R.raw.reshort);
        _mySound3 = MediaPlayer.create(this, R.raw.mishort);
        _mySound4 = MediaPlayer.create(this, R.raw.fashort);
        _mySound5 = MediaPlayer.create(this, R.raw.solshort);
        _mySound6 = MediaPlayer.create(this, R.raw.lashort);
        _shuffleSound = MediaPlayer.create(this, R.raw.shuffle);
        _bigBomgSound = MediaPlayer.create(this, R.raw.bigbomb);
        _smallBomb = MediaPlayer.create(this, R.raw.smallbomb);

    }



}
