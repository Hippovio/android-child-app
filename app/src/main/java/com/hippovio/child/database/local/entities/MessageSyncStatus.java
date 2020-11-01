package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.database.local.typeConverters.DateConverter;
import com.hippovio.child.database.local.typeConverters.syncStatusConverter;
import com.hippovio.child.enums.MessageSyncStates;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = TableName.MESSAGE_SYNC_STATUS)
public class MessageSyncStatus {

    @PrimaryKey
    @ColumnInfo(name = "firebase_id")
    @NonNull
    private String firebaseId;

    @ColumnInfo(name = "sync_state")
    @TypeConverters({syncStatusConverter.class})
    private MessageSyncStates syncState;

    @ColumnInfo(name = "last_modified_time")
    @TypeConverters({DateConverter.class})
    private Date lastModified;

    public MessageSyncStatus(String firebaseId, MessageSyncStates syncState, Date lastModified) {
        this.firebaseId = firebaseId;
        this.syncState = syncState;
        this.lastModified = lastModified;
    }
}
