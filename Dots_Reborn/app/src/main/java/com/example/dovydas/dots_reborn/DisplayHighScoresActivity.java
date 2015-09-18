package com.example.dovydas.dots_reborn;

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
    private String _selectedBoardSize = "6x6"; /*default board size*/

    private ListView _listView;
    private RecordAdapter _adapter;
    private ArrayList<Record> _data;
    private SQLiteDatabase _sqLiteDatabase;
    private UserDbHelper _userDbHelper;
    private Cursor _cursor;
    private String _gameMode;

    private SharedPreferences _sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_high_scores);

        // Get the message from the intent
        Intent intent = getIntent();
        _gameMode = intent.getStringExtra(IntermediateHighscoreActivity.EXTRA_MESSAGE);
        TextView title = (TextView) findViewById(R.id.highScoreTitle);
        if(_gameMode.equals("Move mode")){
            title.setText("Moves Mode High Scores");
        } else if (_gameMode.equals("Time mode")){
            title.setText("Timed Mode High Scores");
        }

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
                readRecords();
                _adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                _selectedBoardSize = _boardSizeSpinner.getSelectedItem().toString();
            }

        });

        _data = new ArrayList<>();
        _adapter = new RecordAdapter(this, _data);
        _listView.setAdapter(_adapter);

        /* initializing database properties */
        _userDbHelper = new UserDbHelper(getApplicationContext());
        _sqLiteDatabase = _userDbHelper.getReadableDatabase();


        /* fill mock data */
        //init();

        ActionBar action = getSupportActionBar();
        action.setDisplayShowHomeEnabled(true);
        action.setLogo(R.drawable.ic_scores);
        action.setDisplayUseLogoEnabled(true);
        action.setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onStart() {
        super.onStart();
        readRecords();
        _adapter.notifyDataSetChanged();
    }

/*
    private void init(){
        for(int i = 10; i > 0; i--){
            _data.add(new Record(i*4, new Date()));
        }
        _adapter.notifyDataSetChanged();
    }
*/

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
        _userDbHelper.deleteInformation(_selectedBoardSize, _gameMode, _sqLiteDatabase);
        _data.clear();
        _adapter.notifyDataSetChanged();
    }

    private void readRecords(){
        _data.clear();
        _cursor = _userDbHelper.getInformations(_selectedBoardSize, _gameMode, _sqLiteDatabase);
        if(_cursor.moveToFirst()){
            do {
                String score, date;
                score = _cursor.getString(0); /* column index */
                date = _cursor.getString(1);
                _data.add(new Record(score, date));

            } while(_cursor.moveToNext());
        }
    }


}
