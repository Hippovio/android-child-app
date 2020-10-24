package com.hippovio.child;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.hippovio.child.database.local.HippovioDatabase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HippovioDatabase db = Room.databaseBuilder(getApplicationContext(),
                HippovioDatabase.class, "database-name").build();
    }
}