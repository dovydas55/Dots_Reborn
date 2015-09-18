package com.example.dovydas.dots_reborn;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dovydas.dots_reborn.R;

public class StartGameOptionsActivity extends PreferenceActivity {
    private SharedPreferences _sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_entry = _sp.getString("themePref", "Dovy");
        setTheme(getResources().getIdentifier(theme_entry, "style", "com.example.dovydas.dots_reborn"));

        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.startgameoptions);
    }
}