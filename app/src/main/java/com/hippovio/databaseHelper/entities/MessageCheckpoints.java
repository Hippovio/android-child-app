package com.hippovio.databaseHelper.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.hippovio.databaseHelper.constants.TableName;
import com.hippovio.entities.enums.PackageName;

import java.util.Date;

/**
 * Author: Raghav Agarwal
 *
 * Database entity for saved Message Checkpoints
 * Each entry has the start message and the end message of the window which is un-read.
 */


@Entity(tableName = TableName.READ_MESSAGE_CHECKPOINTS)
public class MessageCheckpoints {

    @ColumnInfo(name = "message_source")
    public PackageName source;

    @ColumnInfo(name = "start_message_id")
    public String startMessageId;

    @ColumnInfo(name = "start_message_hash")
    public int startMessageHash;

    @ColumnInfo(name = "start_message_date")
    public Date startMessageDate;

    @ColumnInfo(name = "end_message_id")
    public String endMessageId;

    @ColumnInfo(name = "end_message_hash")
    public int endMessageHash;

    @ColumnInfo(name = "end_message_date")
    public Date endMessageDate;

}
