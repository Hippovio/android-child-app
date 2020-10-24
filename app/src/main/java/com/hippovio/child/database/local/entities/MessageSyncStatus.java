package com.hippovio.child.database.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.enums.MessageSyncStates;
import com.hippovio.child.enums.PackageName;

import java.util.Date;

@Entity(tableName = TableName.MESSAGE_SYNC_STATUS)
public class MessageSyncStatus {

    @ColumnInfo(name = "firebase_id")
    private String firebaseId;

    @ColumnInfo(name = "sync_state")
    private MessageSyncStates syncState;

    @ColumnInfo(name = "last_modified_time")
    private Date lastModified;

    public MessageSyncStatus(String firebaseId, MessageSyncStates syncState, Date lastModified) {
        this.firebaseId = firebaseId;
        this.syncState = syncState;
        this.lastModified = lastModified;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public MessageSyncStates getSyncState() {
        return syncState;
    }

    public void setSyncState(MessageSyncStates syncState) {
        this.syncState = syncState;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
