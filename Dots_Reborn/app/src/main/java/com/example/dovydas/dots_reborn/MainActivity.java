package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String GAME_MODE = "com.example.dovydas.dots_reborn.GAME_MODE";
    //private Vibrator _vibrator;
    //private boolean _use_vibration = false;
    private SquareIcon _ic_1;
    private SquareIcon _ic_2;
    private SquareIcon _ic_3;
    private SquareIcon _ic_4;
    private SharedPreferences _sp;
    private Intent _starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

        _starterIntent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //_vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        _ic_1 = (SquareIcon) findViewById(R.id.home_moves);
        _ic_2 = (SquareIcon) findViewById(R.id.home_time);
        _ic_3 = (SquareIcon) findViewById(R.id.home_highScore);
        _ic_4 = (SquareIcon) findViewById(R.id.home_options);

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.mipmap.ic_launcher);
        action.setDisplayUseLogoEnabled(true);
        action.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(_starterIntent);
    }

    //throw this out?
    @Override
    protected void onStart(){
        super.onStart();
        /*_use_vibration = _sp.getBoolean("vibrate",false);

        //not supposed to be here, decide what to link vibrate to
        if(_use_vibration) {
            _vibrator.vibrate(500);
        }
        */
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            /* animation settings */
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            final ScaleAnimation scaleAnimation2 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            final ScaleAnimation scaleAnimation3 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            final ScaleAnimation scaleAnimation4 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            scaleAnimation.setDuration(300);

            scaleAnimation2.setDuration(300);
            scaleAnimation2.setStartOffset(300);

            scaleAnimation3.setDuration(300);
            scaleAnimation3.setStartOffset(600);

            scaleAnimation4.setDuration(300);
            scaleAnimation4.setStartOffset(900);

            _ic_1.startAnimation(scaleAnimation);
            _ic_2.startAnimation(scaleAnimation2);
            _ic_3.startAnimation(scaleAnimation4);
            _ic_4.startAnimation(scaleAnimation3);
            /* end of animation settings */
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void showHighScores(View v){
        Intent intent = new Intent(this, IntermediateHighscoreActivity.class);
        startActivity(intent);
    }

    public void showOptions(View v){
        Intent intent = new Intent(this, StartGameOptionsActivity.class);
        startActivity(intent);
    }

    public void playMoveMode(View v){
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(GAME_MODE, "Move mode");
        startActivity(intent);
    }

    public void playTimeMode(View v){
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(GAME_MODE, "Time mode");
        startActivity(intent);
    }


}
