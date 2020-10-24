package com.hippovio.databaseHelper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.hippovio.databaseHelper.entities.MessageCheckpoints;
import java.util.List;
import static com.hippovio.databaseHelper.constants.TableName.READ_MESSAGE_CHECKPOINTS;

@Dao
public interface MessageCheckpointsDao {

    @Query("SELECT * FROM " + READ_MESSAGE_CHECKPOINTS)
    List<MessageCheckpoints> getAll();

    @Insert
    void insertAll(MessageCheckpoints... checkpoints);

    @Delete
    void delete(MessageCheckpoints checkpoints);
}