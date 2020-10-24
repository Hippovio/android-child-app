package com.hippovio.child.database.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hippovio.child.database.local.dao.ChateeDao;
import com.hippovio.child.database.local.dao.MessageCheckpointsDao;
import com.hippovio.child.database.local.dao.MessageSyncStatusDao;
import com.hippovio.child.database.local.entities.MessageReadCheckpoint;

@Database(entities = {MessageReadCheckpoint.class}, version = 1)
public abstract class HippovioDatabase extends RoomDatabase {

    public abstract MessageCheckpointsDao messageCheckpointsDao();

    public abstract ChateeDao chateeDao();

    public abstract MessageSyncStatusDao messageSyncStatusDao();
}