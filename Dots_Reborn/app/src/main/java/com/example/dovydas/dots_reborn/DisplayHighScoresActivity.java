package com.example.dovydas.dots_reborn;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class DisplayHighScoresActivity extends AppCompatActivity {

    private Spinner _boardSizeSpinner;
    private String _selectedBoardSize;

    private ListView _listView;
    private RecordAdapter _adapter;
    private ArrayList<Record> _data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_high_scores);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(IntermediateHighscoreActivity.EXTRA_MESSAGE);
        TextView title = (TextView) findViewById(R.id.highScoreTitle);
        title.setText(message);
        ///////////

        _boardSizeSpinner = (Spinner) findViewById(R.id.boardSize);
        _listView = (ListView) findViewById(R.id.records);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.highscore_menu, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        _boardSizeSpinner.setAdapter(spinnerAdapter);

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

        _data = new ArrayList<Record>();
        _adapter = new RecordAdapter(this, _data);
        _listView.setAdapter(_adapter);

        /* fill mock data */
        init();

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_scores);
        action.setDisplayUseLogoEnabled(true);

    }

    private void init(){
        for(int i = 10; i > 0; i--){
            _data.add(new Record(i*4, new Date()));
        }
        _adapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, InGameOptionsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearHistory(View v){
        //TODO: clear history
        Toast.makeText(getApplicationContext(), "TODO: clear history for " + _selectedBoardSize, Toast.LENGTH_SHORT).show();
    }
}
