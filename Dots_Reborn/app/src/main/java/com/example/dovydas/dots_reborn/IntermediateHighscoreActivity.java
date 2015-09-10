package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class IntermediateHighscoreActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.dovydas.dots_reborn.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_highscore);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMoveHighScore(View v){
        Intent intent = new Intent(this, DisplayHighScoresActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Move mode");
        startActivity(intent);
    }

    public void showTimeHighScore(View v){
        Intent intent = new Intent(this, DisplayHighScoresActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Time mode");
        startActivity(intent);
    }

}
