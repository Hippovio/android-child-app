package com.hippovio.child.database.local.typeConverters;

import androidx.room.TypeConverter;

import com.hippovio.child.enums.MessageSyncStates;


public class MessageSyncStatusConverter {

    @TypeConverter
    public static MessageSyncStates toSyncStatus(int value) {
        for (MessageSyncStates status : MessageSyncStates.values()) {
            if (status.ordinal() == value)
                return status;
        }
        throw new IllegalArgumentException("Could not recognize messageSyncStatus value: " + value);
    }

    @TypeConverter
    public static Integer toInteger(MessageSyncStates status) {
        return status.ordinal();
    }
}