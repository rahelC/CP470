package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        // Load data
        loadUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void onClicked(View v) {
        // Save user's email when user clicked 'Login' button
        saveUserData();

        // Go to the next activity (MainActivity)
        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mIntent);
    }

    // Load user's data saved in SharedPreference
    private void loadUserData() {
        String file_name = getString(R.string.preference_name);
        SharedPreferences myPrefs = getSharedPreferences(file_name, MODE_PRIVATE);

        String email_key = getString(R.string.key_email);
        String new_email_value = myPrefs.getString(email_key, "email@domain.com");
        ((EditText) findViewById(R.id.editTextTextPersonName)).setText(new_email_value);
    }

    // Save user's data in SharedPreference
    private void saveUserData() {
        String file_name = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(file_name, MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mPrefs.edit();
        myEditor.clear();

        String email_key  = getString(R.string.key_email);
        String new_email_entered = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
        myEditor.putString(email_key, new_email_entered);

        myEditor.commit();
    }
}