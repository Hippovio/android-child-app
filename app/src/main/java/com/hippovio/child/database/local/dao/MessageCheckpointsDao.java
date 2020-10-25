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

@Dao
public interface MessageCheckpointsDao {

    @Query("SELECT * FROM " + MESSAGE_READ_CHECKPOINTS)
    List<MessageReadCheckpoint> getAll();

    @Insert
    void insertAll(MessageReadCheckpoint... checkpoints);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMessageReadCheckpoint(MessageReadCheckpoint messageReadCheckpoint);

    @Delete
    void delete(MessageReadCheckpoint checkpoints);

    @Query("SELECT * FROM " + MESSAGE_READ_CHECKPOINTS + " WHERE chatee_id= :chateeId ORDER BY start_message_date DESC")
    List<MessageReadCheckpoint> getCheckpointsForChateeIdOrderedByLatest(String chateeId);
}