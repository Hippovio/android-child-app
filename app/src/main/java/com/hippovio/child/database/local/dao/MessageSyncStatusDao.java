package com.hippovio.child.database.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.database.local.entities.MessageSyncStatus;

import java.util.List;

import static com.hippovio.child.database.local.constants.TableName.MESSAGE_READ_CHECKPOINTS;
import static com.hippovio.child.database.local.constants.TableName.MESSAGE_SYNC_STATUS;

@Dao
public interface MessageSyncStatusDao {

    @Query("SELECT * FROM " + MESSAGE_SYNC_STATUS)
    List<MessageSyncStatus> getAll();

    @Insert
    void insertAll(MessageSyncStatus... messageSyncStatuses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMessageSyncStatus(MessageSyncStatus syncStatus);

    @Delete
    void delete(MessageSyncStatus syncStatus);

    @Query("SELECT * FROM " + MESSAGE_SYNC_STATUS + " WHERE firebase_id= :firebaseId LIMIT 1")
    MessageSyncStatus getMessageSycnStatus(String firebaseId);
}