package com.hippovio.databaseHelper.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hippovio.databaseHelper.constants.TableName;
import com.hippovio.databaseHelper.typeConverters.PackageNameConverter;
import com.hippovio.entities.enums.PackageName;

import lombok.Builder;

/**
 * Author: Raghav Agarwal
 *
 * Database entity for saved Message Checkpoints
 * Each entry has the start message and the end message of the window which is un-read.
 */

@Entity(tableName = TableName.READ_MESSAGE_CHECKPOINTS)
public class MessageCheckpoints {

    @PrimaryKey
    @ColumnInfo(name = "start_message_id")
    @NonNull
    public String startMessageId;

    @ColumnInfo(name = "start_message_hash")
    public int startMessageHash;

//    @ColumnInfo(name = "start_message_date")
//    public Date startMessageDate;

    @ColumnInfo(name = "end_message_id")
    public String endMessageId;

    @ColumnInfo(name = "end_message_hash")
    public int endMessageHash;

//    @ColumnInfo(name = "end_message_date")
//    public Date endMessageDate;

    @ColumnInfo(name = "chatee_id")
    public String chateeId;

    @TypeConverters({PackageNameConverter.class})
    @ColumnInfo(name = "message_source")
    public PackageName source;

    @Builder(builderMethodName = "MessageCheckpointsBuilder")
    public static MessageCheckpoints messageCheckpoints(String startMessageId, int startMessageHash,
                                                 String endMessageId, int endMessageHash, String chateeId, PackageName source) {
        MessageCheckpoints messageCheckpoints = new MessageCheckpoints();
        messageCheckpoints.startMessageId = startMessageId;
        messageCheckpoints.startMessageHash = startMessageHash;
        messageCheckpoints.endMessageId = endMessageId;
        messageCheckpoints.endMessageHash = endMessageHash;
        messageCheckpoints.chateeId = chateeId;
        messageCheckpoints.source = source;

        return messageCheckpoints;
    }
}
