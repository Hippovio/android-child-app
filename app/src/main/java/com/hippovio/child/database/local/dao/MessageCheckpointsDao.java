package com.hippovio.child.database.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hippovio.child.database.local.entities.MessageReadCheckpoint;
import com.hippovio.child.database.local.entities.MessageSyncStatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static com.hippovio.child.database.local.constants.TableName.MESSAGE_READ_CHECKPOINTS;

@Dao
public abstract class MessageCheckpointsDao {

    @Query("SELECT * FROM " + MESSAGE_READ_CHECKPOINTS)
    public abstract List<MessageReadCheckpoint> getAll();

    @Insert
    public abstract List<Long> insertAll(MessageReadCheckpoint... checkpoints);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateMessageReadCheckpoint(MessageReadCheckpoint messageReadCheckpoint);

    @Delete
    public abstract void delete(MessageReadCheckpoint... checkpoints);

    @Query("SELECT * FROM " + MESSAGE_READ_CHECKPOINTS + " WHERE chatee_id= :chateeId ORDER BY start_message_date DESC")
    public abstract List<MessageReadCheckpoint> getCheckpointsForChateeIdOrderedByLatest(String chateeId);

    @Transaction
    public void updateAndCreateCheckpoint(MessageReadCheckpoint update, MessageReadCheckpoint create){
        updateMessageReadCheckpoint(update);
        insertAll(create);
    }
}