package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.enums.PackageName;
import com.hippovio.child.database.local.typeConverters.PackageNameConverter;
import com.hippovio.child.pojos.Message;

import java.util.Date;

import lombok.Builder;

/**
 * Author: Raghav Agarwal
 *
 * Database entity for saved Message Checkpoints
 * Each entry has the start message and the end message of the window which is un-read.
 */

@Entity(tableName = TableName.MESSAGE_READ_CHECKPOINTS)
public class MessageReadCheckpoint {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private String checkpointId;

    @ColumnInfo(name = "chatee_id")
    @NonNull
    private String chateeId;

    @TypeConverters({PackageNameConverter.class})
    @ColumnInfo(name = "message_source")
    private PackageName source;

    @ColumnInfo(name = "start_message_id")
    private String startMessageId;

    @ColumnInfo(name = "start_message_hash")
    private String startMessageHash;

    @ColumnInfo(name = "start_message_date")
    private Date startMessageDate;

    @ColumnInfo(name = "end_message_id")
    @NonNull
    private String endMessageId;

    @ColumnInfo(name = "end_message_hash")
    private String endMessageHash;

    @ColumnInfo(name = "end_message_date")
    private Date endMessageDate;

    @Builder(builderMethodName = "MessageCheckpointsBuilder")
    public static MessageReadCheckpoint messageCheckpoints(String startMessageId, String startMessageHash,
                                                           String endMessageId, String endMessageHash, String chateeId, PackageName source) {
        MessageReadCheckpoint messageReadCheckpoint = new MessageReadCheckpoint();
        messageReadCheckpoint.startMessageId = startMessageId;
        messageReadCheckpoint.startMessageHash = startMessageHash;
        messageReadCheckpoint.endMessageId = endMessageId;
        messageReadCheckpoint.endMessageHash = endMessageHash;
        messageReadCheckpoint.chateeId = chateeId;
        messageReadCheckpoint.source = source;

        return messageReadCheckpoint;
    }

    @NonNull
    public String getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(@NonNull String checkpointId) {
        this.checkpointId = checkpointId;
    }

    @NonNull
    public String getChateeId() {
        return chateeId;
    }

    public void setChateeId(@NonNull String chateeId) {
        this.chateeId = chateeId;
    }

    public PackageName getSource() {
        return source;
    }

    public void setSource(PackageName source) {
        this.source = source;
    }

    public String getStartMessageId() {
        return startMessageId;
    }

    public void setStartMessageId(String startMessageId) {
        this.startMessageId = startMessageId;
    }

    public String getStartMessageHash() {
        return startMessageHash;
    }

    public void setStartMessageHash(String startMessageHash) {
        this.startMessageHash = startMessageHash;
    }

    public Date getStartMessageDate() {
        return startMessageDate;
    }

    public void setStartMessageDate(Date startMessageDate) {
        this.startMessageDate = startMessageDate;
    }

    @NonNull
    public String getEndMessageId() {
        return endMessageId;
    }

    public void setEndMessageId(@NonNull String endMessageId) {
        this.endMessageId = endMessageId;
    }

    public String getEndMessageHash() {
        return endMessageHash;
    }

    public void setEndMessageHash(String endMessageHash) {
        this.endMessageHash = endMessageHash;
    }

    public Date getEndMessageDate() {
        return endMessageDate;
    }

    public void setEndMessageDate(Date endMessageDate) {
        this.endMessageDate = endMessageDate;
    }

    public boolean checkMessageInBetweenCheckpoint(Message message){
        return message.getDate().after(startMessageDate) && message.getDate().before(endMessageDate);
    }

    public boolean checkMessageBeforeCheckpoint(Message message){
        return message.getDate().before(startMessageDate);
    }

    public boolean checkMessageAfterCheckpoint(Message message){
        return message.getDate().after(endMessageDate);
    }

    public boolean checkCheckpointInBetweenCheckpoint(MessageReadCheckpoint checkpoint){
        return checkpoint.getStartMessageDate().after(startMessageDate) && checkpoint.getEndMessageDate().before(endMessageDate);
    }

    public boolean checkCheckpointOverlapBeforeCheckpoint(MessageReadCheckpoint checkpoint){
        return checkpoint.getStartMessageDate().before(startMessageDate) && checkpoint.getEndMessageDate().after(startMessageDate);
    }

    public boolean checkCheckpointOverlapAfterCheckpoint(MessageReadCheckpoint checkpoint){
        return checkpoint.getStartMessageDate().before(endMessageDate) && checkpoint.getEndMessageDate().after(endMessageDate);
    }

    public boolean checkCheckpointdisjointBeforeCheckpoint(MessageReadCheckpoint checkpoint){
        return checkpoint.getEndMessageDate().before(startMessageDate);
    }

    public boolean checkCheckpointdisjointAfterCheckpoint(MessageReadCheckpoint checkpoint){
        return checkpoint.getStartMessageDate().after(endMessageDate);
    }


}
