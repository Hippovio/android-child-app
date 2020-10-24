package com.hippovio.screenread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.hippovio.databaseHelper.HippovioDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HippovioDatabase db = Room.databaseBuilder(getApplicationContext(),
                HippovioDatabase.class, "database-name").build();
    }
}