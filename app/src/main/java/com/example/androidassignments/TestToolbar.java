package com.example.androidassignments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    String snackbarStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.testing_floating_msg), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        snackbarStr = getString(R.string.snackbar_str1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // invoked automatically by activity
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // invoked when an Item selected.
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.action_one:
                Log.d("Toolbar", getString(R.string.snackbar_str1));

                Snackbar.make(findViewById(R.id.action_one), snackbarStr, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", getString(R.string.snackbar_str2));

                case2();
            break;
            case R.id.action_three:
                Log.d("Toolbar", getString(R.string.snackbar_str3));

                case3();
            break;
            case R.id.action_about:
                Toast.makeText(getApplicationContext(), getString(R.string.version_msg), Toast.LENGTH_SHORT).show();
            break;
        }

        return true;
    }

    public void case2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
        builder.setTitle(R.string.alert_dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        TestToolbar.this.finish();
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void case3() {
        AlertDialog.Builder customDialog = new AlertDialog.Builder(TestToolbar.this);
        // Get the layout inflater
        LayoutInflater inflater = TestToolbar.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.custom_dialog, null);
        customDialog.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText edit = view.findViewById(R.id.dialog_message_box);
                        snackbarStr = edit.getText().toString();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        Dialog dialog = customDialog.create();
        dialog.show();
    }
}