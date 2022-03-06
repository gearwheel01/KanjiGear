package com.example.kanjigear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static DatabaseOpenHelper dbHelper;
    private static boolean databaseOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseOpened = false;
        dbHelper = new DatabaseOpenHelper(this.getApplicationContext());
        if (dbHelper.checkDb()) {
            openDatabase();
        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }

    }

    protected static void openDatabase() {
        try {
            dbHelper.openDatabase();
            databaseOpened = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}