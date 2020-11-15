package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.database.local.typeConverters.DateConverter;
import com.hippovio.child.database.local.typeConverters.SourceConverter;
import com.hippovio.child.enums.Sources;
import com.hippovio.child.pojos.Message;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Raghav Agarwal
 *
 * Database entity for saved Message Checkpoints
 * Each entry has the start message and the end message of the window which is un-read.
 */

@Entity(tableName = TableName.MESSAGE_READ_CHECKPOINTS)
@Getter
@Setter
public class MessageReadCheckpoint {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer checkpointId;

    @ColumnInfo(name = "chatee_id")
    @NonNull
    private Long chateeId;

    @TypeConverters({SourceConverter.class})
    @ColumnInfo(name = "message_source")
    @NonNull
    private Sources source;

    @ColumnInfo(name = "start_message_id")
    private String startMessageId;

    @ColumnInfo(name = "start_message_hash")
    private String startMessageHash;

    @ColumnInfo(name = "start_message_date")
    @TypeConverters({DateConverter.class})
    @NonNull
    private Date startMessageDate;

    @ColumnInfo(name = "end_message_id")
    private String endMessageId;

    @ColumnInfo(name = "end_message_hash")
    private String endMessageHash;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "end_message_date")
    private Date endMessageDate;

    @Builder(builderMethodName = "MessageCheckpointsBuilder")
    public static MessageReadCheckpoint messageCheckpoints(Message startMessage, Message endMessage) {
        MessageReadCheckpoint messageReadCheckpoint = new MessageReadCheckpoint();
        messageReadCheckpoint.startMessageId = startMessage.getId();
        messageReadCheckpoint.startMessageHash = startMessage.getMessageHash();
        messageReadCheckpoint.startMessageDate = startMessage.getDateTime();
        messageReadCheckpoint.endMessageId = endMessage.getId();
        messageReadCheckpoint.endMessageHash = endMessage.getMessageHash();
        messageReadCheckpoint.endMessageDate = endMessage.getDateTime();
        messageReadCheckpoint.chateeId = startMessage.getChatee().getChateeId();
        messageReadCheckpoint.source = startMessage.getChatee().getChateeSource();

        return messageReadCheckpoint;
    }

    public boolean hasInBetweenIt(MessageReadCheckpoint checkpoint){
        if (endMessageId == null && startMessageDate.before(checkpoint.getStartMessageDate()))
            return true;
        return checkpoint.getStartMessageDate().after(startMessageDate) && checkpoint.getEndMessageDate().before(endMessageDate);
    }

    public void updateStartMessage(Message startMessage) {
        startMessageId = startMessage.getId();
        startMessageHash = startMessage.getMessageHash();
        startMessageDate = startMessage.getDateTime();
    }

    public void updateEndMessage(Message endMessage) {
        endMessageId = endMessage.getId();
        endMessageHash = endMessage.getMessageHash();
        endMessageDate = endMessage.getDateTime();
    }

    @Override
    public MessageReadCheckpoint clone() {
        MessageReadCheckpoint checkpoint = new MessageReadCheckpoint();
        checkpoint.startMessageId = startMessageId;
        checkpoint.startMessageDate = startMessageDate;
        checkpoint.startMessageHash = startMessageHash;
        checkpoint.endMessageHash = endMessageHash;
        checkpoint.endMessageId = endMessageId;
        checkpoint.endMessageDate = endMessageDate;
        checkpoint.source = source;
        checkpoint.chateeId = chateeId;
        return checkpoint;
    }
}
