package com.hippovio.databaseHelper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hippovio.databaseHelper.dao.MessageCheckpointsDao;
import com.hippovio.databaseHelper.entities.MessageCheckpoints;

@Database(entities = {MessageCheckpoints.class}, version = 1)
public abstract class HippovioDatabase extends RoomDatabase {
    public abstract MessageCheckpointsDao MessageCheckpointsDao();
}