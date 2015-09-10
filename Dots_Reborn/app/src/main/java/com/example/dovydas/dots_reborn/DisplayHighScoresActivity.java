package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayHighScoresActivity extends AppCompatActivity {

    private Spinner _boardSizeSpinner;
    private String _selectedBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_high_scores);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(IntermediateHighscoreActivity.EXTRA_MESSAGE);
        TextView title = (TextView) findViewById(R.id.highScoreTitle);
        title.setText(message);

        _boardSizeSpinner = (Spinner) findViewById(R.id.boardSize);
        _boardSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                _selectedBoardSize = _boardSizeSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                _selectedBoardSize = _boardSizeSpinner.getSelectedItem().toString();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_high_scores, menu);
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

    public void clearHistory(View v){
        //TODO: clear history
        Toast.makeText(getApplicationContext(), "TODO: clear history for " + _selectedBoardSize, Toast.LENGTH_SHORT).show();
    }
}
