package com.hippovio.child.database.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hippovio.child.database.local.constants.TableName;
import com.hippovio.child.database.local.typeConverters.DateConverter;
import com.hippovio.child.database.local.typeConverters.MessageSyncStatusConverter;
import com.hippovio.child.enums.MessageSyncStates;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

//study performance of firebase

@Getter
@Setter
@Entity(tableName = TableName.MESSAGE_SYNC_STATUS)
public class MessageSyncStatus {

    @PrimaryKey
    @ColumnInfo(name = "firebase_id")
    @NonNull
    private String firebaseId;

    @ColumnInfo(name = "sync_state")
    @TypeConverters({MessageSyncStatusConverter.class})
    private MessageSyncStates syncState;

    @ColumnInfo(name = "last_modified_time")
    @TypeConverters({DateConverter.class})
    private Date lastModified;

    @ColumnInfo(name = "created_datetime")
    @TypeConverters({DateConverter.class})
    private Date createdDatetime;

    public MessageSyncStatus(@NonNull String firebaseId, MessageSyncStates syncState, Date lastModified, Date createdDatetime) {
        this.firebaseId = firebaseId;
        this.syncState = syncState;
        this.lastModified = lastModified;
        this.createdDatetime = createdDatetime;
    }
}
